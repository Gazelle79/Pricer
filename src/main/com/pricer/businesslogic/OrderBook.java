package main.com.pricer.businesslogic;

import java.io.BufferedReader;
import java.io.*;
import java.util.HashMap;
import java.util.ArrayList;
import main.com.pricer.interfaces.*;


public class OrderBook implements ISide, IOrderType, IAction
{
    private HashMap<String, AddOrder> orderMap = null;

    private int targetSize = 0;
    private int remainingShares = targetSize;
    ArrayList<AddOrder> askList = null;
    ArrayList<AddOrder> bidList = null;
    private int bidShareCount = 0;
    private int askShareCount = 0;
    private double previousIncome = 0.0;
    private double previousExpense = 0.0;


    public OrderBook(int targetSize)
    {
        this.targetSize = targetSize;
        orderMap = new HashMap<>();  //Contains all ADD orders read in from the data file. Does not contain Reduce orders.
        askList = new ArrayList();  //Contains all SELL orders in OrderMap.
        bidList = new ArrayList();  //Contains all BUY orders in OrderMap.

    }

    public String ReadMarketData(String inputFileNameAndPath) throws IOException
    {
        BufferedReader reader = null;
        String marketDataText = null;
        StringBuilder inputDataString = new StringBuilder();

        /*Not put in try / catch statement so error could be sent back to method caller.*/
        InputStream is = new FileInputStream(inputFileNameAndPath);
        reader = new BufferedReader(new InputStreamReader(is));

        while ((marketDataText = reader.readLine()) != null)
        {
                /*
                 * Make a new Order object.
                 * Parse each character in this line of text.
                 * Assign each character to its' specific piece of data
                 * in an Order.
                 * Put that order object in a list of Order objects.
                 * */
                String[] orderData = marketDataText.split(" ");
                switch (orderData[1])
                {
                    case "A":
                    {
                        //Make an Add order.
                        AddOrder addOrder = this.CreateAddOrder(orderData);
                        if(addOrder != null)
                        {
                            orderMap.put(addOrder.getId(), addOrder);

                            if (addOrder.getSide() == side.BUY)
                            {
                                bidList.add(addOrder);
                            }
                            else if (addOrder.getSide() == side.SELL)
                            {
                                askList.add(addOrder);
                            }
                            this.CalculateMarketData(addOrder);
                        }
                        break;
                    }

                    case "R":
                    {
                        //Make an Reduce order.
                        ReduceOrder reduceOrder = this.CreateReduceOrder(orderData);
                        if(reduceOrder != null)
                        {
                            this.CalculateMarketData(reduceOrder);
                        }
                        break;
                    }
                    default:
                    {
                        //Do nothing, throw an error, etc.
                        System.err.println("Invalid input data at position 1:" + orderData[1]);
                        break;
                    }
                }
                inputDataString.append(marketDataText + "\n");
        }
        reader.close();
        return inputDataString.toString();
    }


    public void CalculateMarketData(Order orderToCalculate)
    {
        switch (orderToCalculate.orderType)
        {
            /*
             * ADD ORDER:
             * Keep a running tab of the dollar totals & share count totals for Buy (b) and Sell (s) for each
             * order. When share count exceeds the target_size, print the share amount and the
             * dollar total as output.
             * */
            case ADD:
            {
               this.CalculateAddOrders((AddOrder)orderToCalculate);
                break;
            }

            /* REDUCE ORDER:
             * Match this order to one that's in the list. Reduce the number of overall buy / sell shares accordingly.
             * Reduce number of shares from the order that this reduce order is pointing to.
             * When this order's shares are reduced to zero, remove that order from the book.
             * Reduce Total Share count and total dollar amount accordingly.
             * */
            case REDUCE:
            {
                this.CalculateReduceOrders((ReduceOrder)orderToCalculate);
                break;
            }
            default:
            {
                break;
            }
        }
    }


    private void CalculateAddOrders(AddOrder insertedOrder)
    {
        if (insertedOrder.getSide() == side.BUY)
        {
            bidShareCount += insertedOrder.getSize();

            if (bidShareCount >= targetSize)
            {
                double income = this.CalculateIncome();
                this.WriteMarketData(insertedOrder.getTimeStamp(), insertedOrder.getAction().actionValue(), income);
            }
        }
        else if (insertedOrder.getSide() == side.SELL)
        {
            askShareCount += insertedOrder.getSize();

            if (askShareCount >= targetSize)
            {
                double expense = this.CalculateExpense();
                this.WriteMarketData(insertedOrder.getTimeStamp(), insertedOrder.getAction().actionValue(), expense);
            }
        }
        else{}
    }

    private void CalculateReduceOrders(ReduceOrder reduceOrder)
    {
        String reduceOrderId = reduceOrder.getId();
        AddOrder addOrderToReduce = null;
        double income = 0.0;
        double expense = 0.0;

        if(orderMap.containsKey(reduceOrderId))
        {
            addOrderToReduce = orderMap.get(reduceOrderId);
            int sharesToRemove = reduceOrder.getSize();

            //Remove shares from the Add order.
            addOrderToReduce.ReduceShares(sharesToRemove);

            //Remove shares from the buy share count.
            if(addOrderToReduce.getSide() == side.BUY)
            {
                bidShareCount -= sharesToRemove;

                if(addOrderToReduce.getSize() == 0)
                {
                    bidList.remove(addOrderToReduce);
                    orderMap.remove(reduceOrderId);
                }

                income = this.CalculateIncome();
                if (bidShareCount >= targetSize)
                {
                    previousIncome = income;
                    this.WriteMarketData(reduceOrder.getTimeStamp(), addOrderToReduce.getAction().actionValue(), income);
                }
                else if(bidShareCount < targetSize)
                {
                    if(previousIncome != income)
                    {
                        this.WriteMarketData(reduceOrder.getTimeStamp(), addOrderToReduce.getAction().actionValue(), 0.0);
                    }
                    else
                    {
                        //do nothing
                    }
                }
            }

            else if(addOrderToReduce.getSide() == side.SELL)
            {
                askShareCount -= sharesToRemove;

                if(addOrderToReduce.getSize() == 0)
                {
                    askList.remove(addOrderToReduce);
                    orderMap.remove(reduceOrderId);
                }

                expense = this.CalculateExpense();
                if (askShareCount >= targetSize)
                {
                    previousExpense = expense;
                    this.WriteMarketData(reduceOrder.getTimeStamp(), addOrderToReduce.getAction().actionValue(), expense);
                }
                else if(askShareCount < targetSize)
                {
                    if(previousExpense != expense)
                    {
                        this.WriteMarketData(reduceOrder.getTimeStamp(), addOrderToReduce.getAction().actionValue(), 0.0);
                    }
                    else
                    {
                        //do nothing
                    }
                }
            }
            else
            {}

        }
    }

    private double CalculateExpense()
    {
        //Sort ALL bids from lowest to highest.
        askList.sort(new SortSharePriceAscending());

        int currentShareCount = 0;
        double expense = 0.0;

        for(int i = 0; i < askList.size(); i++)
        {
            String thisId = askList.get(i).getId();
            AddOrder orderToBuy = orderMap.get(thisId);

            if((orderToBuy.getSize() + currentShareCount) >= targetSize)
            {
                int adjustedSharesToBuy = targetSize - currentShareCount;
                expense += (adjustedSharesToBuy * orderToBuy.getPrice());
                remainingShares = (currentShareCount + orderToBuy.getSize()) - targetSize;
                currentShareCount += adjustedSharesToBuy;
            }
            else
            {
                expense += (orderToBuy.getSize() * orderToBuy.getPrice());
                remainingShares = targetSize - currentShareCount;
                currentShareCount += orderToBuy.getSize();
            }
        }
        return expense;
    }

    private double CalculateIncome()
    {
        //Sort ALL bids from highest to lowest.
        bidList.sort(new SortSharePriceDescending());

        int currentShareCount = 0;
        double income = 0.0;

        for(int i = 0; i < bidList.size(); i++)
        {
            String thisId = bidList.get(i).getId();
            AddOrder orderToSell = orderMap.get(thisId);

            if((orderToSell.getSize() + currentShareCount) >= targetSize)
            {
                int adjustedShareSize = targetSize - currentShareCount;
                income += (adjustedShareSize * orderToSell.getPrice());
                remainingShares = (currentShareCount + orderToSell.getSize()) - targetSize;
                currentShareCount += adjustedShareSize;
            }
            else
            {
                income += (orderToSell.getSize() * orderToSell.getPrice());
                remainingShares = targetSize - currentShareCount;
                currentShareCount += orderToSell.getSize();
            }
        }
        return income;
    }

    //Write out Market data, where it's appropriate.
    public void WriteMarketData(int timestamp, char orderAction, double expenditure)
    {
        String expenseString = (expenditure > 0.0 ? Double.toString(expenditure) :  "NA");
        System.out.println(timestamp + " " + orderAction + " " + expenseString);
    }

    private AddOrder CreateAddOrder(String[] marketDataText)
    {
        AddOrder newAddOrder = null;

        try
        {
            int timeStamp = Integer.parseInt(marketDataText[0]);
            //char orderType = Character.toUpperCase(marketDataText[1].charAt(0));
            String orderId = marketDataText[2];
            char side = Character.toUpperCase(marketDataText[3].charAt(0));
            double price = Double.parseDouble(marketDataText[4]);
            short orderSize = Short.parseShort(marketDataText[5]);

            newAddOrder = new AddOrder(timeStamp, IOrderType.orderType.ADD, orderId, side, price, orderSize);
        }

        catch(Exception e)
        {
            System.err.println("Invalid / missing input data: " + e);
        }
        return newAddOrder;
    }

    private ReduceOrder CreateReduceOrder(String[] marketDataText)
    {
        ReduceOrder newReduceOrder = null;

        try
        {
            int timeStamp = Integer.parseInt(marketDataText[0]);
            //char orderType = marketDataText[1].charAt(0);
            String orderId = marketDataText[2];
            short orderSize = Short.parseShort(marketDataText[3]);

            newReduceOrder = new ReduceOrder(timeStamp, IOrderType.orderType.REDUCE, orderId, orderSize);
        }

        catch(Exception e)
        {
            System.err.println("Invalid input data: " + e);
        }
        return newReduceOrder;
    }
}

