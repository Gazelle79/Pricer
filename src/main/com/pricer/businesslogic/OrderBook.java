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
    ArrayList<AddOrder> askList = null;
    ArrayList<AddOrder> bidList = null;
    private int bidShareCount = 0;
    private int askShareCount = 0;
    private double previousIncome = 0.0;
    private double previousExpense = 0.0;
    private double income = 0.0;
    private double expense = 0.0;
    private StringBuilder stringToReturn = null;

    public OrderBook(int targetSize)
    {
        this.targetSize = targetSize;
        orderMap = new HashMap<>();  //Contains all ADD orders read in from the data file. Does not contain Reduce orders.
        askList = new ArrayList();  //Contains all SELL orders in OrderMap.
        bidList = new ArrayList();  //Contains all BUY orders in OrderMap.
        stringToReturn = new StringBuilder();
    }

    public String ReadFinanceData(String inputFileNameAndPath) throws IOException
    {
        BufferedReader reader = null;
        String financeDataText = null;

        /*Not put in try / catch statement so error could be sent back to method caller.*/
        InputStream is = new FileInputStream(inputFileNameAndPath);
        reader = new BufferedReader(new InputStreamReader(is));

        while ((financeDataText = reader.readLine()) != null)
        {
                /*
                 * Make a new Order object.
                 * Parse each character in this line of text.
                 * Assign each character to its' specific piece of data
                 * in an Order.
                 * Put that order object in a list of Order objects.
                 * */
                String[] orderData = financeDataText.split(" ");
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
                            this.CalculateFinanceData(addOrder);
                        }
                        break;
                    }

                    case "R":
                    {
                        //Make an Reduce order.
                        ReduceOrder reduceOrder = this.CreateReduceOrder(orderData);
                        if(reduceOrder != null)
                        {
                            this.CalculateFinanceData(reduceOrder);
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
        }
        reader.close();
        return stringToReturn.toString();
    }

    public void CalculateFinanceData(Order orderToCalculate)
    {
        switch (orderToCalculate.orderType)
        {
            case ADD:
            {
               this.CalculateAddOrders((AddOrder)orderToCalculate);
                break;
            }

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
                this.WriteFinanceData(insertedOrder.getTimeStamp(), insertedOrder.getAction().actionValue(), income);
                previousIncome = income;
            }
        }
        else if (insertedOrder.getSide() == side.SELL)
        {
            askShareCount += insertedOrder.getSize();

            if (askShareCount >= targetSize)
            {
                double expense = this.CalculateExpense();
                this.WriteFinanceData(insertedOrder.getTimeStamp(), insertedOrder.getAction().actionValue(), expense);
                previousExpense = expense;
            }
        }
        else{}
    }

    private void CalculateReduceOrders(ReduceOrder reduceOrder)
    {
        String reduceOrderId = reduceOrder.getId();
        AddOrder addOrderToReduce = null;

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

                if(bidShareCount >= targetSize) //bid shares >= targetSize
                {
                    //print the price
                    income = this.CalculateIncome();
                    this.WriteFinanceData(reduceOrder.getTimeStamp(), addOrderToReduce.getAction().actionValue(), income);
                    previousIncome = income;
                }
                else
                {
                    income = 0.0;
                    if (previousIncome != income) //the price changed
                    {
                        //print NA, because less than targetSize shares
                        this.WriteFinanceData(reduceOrder.getTimeStamp(), addOrderToReduce.getAction().actionValue(), 0.0);
                        previousIncome = 0.0;
                    }
                    else //price didn't change
                    {
                        //blank (do nothing, print nothing)
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

                if(askShareCount >= targetSize) //bid shares >= targetSize
                {
                    //print the price
                    expense = this.CalculateExpense();
                    this.WriteFinanceData(reduceOrder.getTimeStamp(), addOrderToReduce.getAction().actionValue(), expense);
                    previousExpense = expense;
                }
                else
                {
                    expense = 0.0;
                    if (previousExpense != expense) //the price changed
                    {
                        //print NA, because less than targetSize shares
                        this.WriteFinanceData(reduceOrder.getTimeStamp(), addOrderToReduce.getAction().actionValue(), 0.0);
                        previousExpense = 0.0;
                    }
                    else //price didn't change
                    {
                        //blank (do nothing, print nothing)
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
                currentShareCount += adjustedSharesToBuy;
            }
            else
            {
                expense += (orderToBuy.getSize() * orderToBuy.getPrice());
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
                currentShareCount += adjustedShareSize;
            }
            else
            {
                income += (orderToSell.getSize() * orderToSell.getPrice());
                currentShareCount += orderToSell.getSize();
            }
        }
        return income;
    }

    //Write out Market data, where it's appropriate.
    public void WriteFinanceData(int timestamp, char orderAction, double expenditure)
    {
        String expenseString = (expenditure > 0.0 ? Double.toString(expenditure) :  "NA");
        System.out.println(timestamp + " " + orderAction + " " + expenseString);
        stringToReturn.append(timestamp + " " + orderAction + " " + expenseString + "\n");
    }


    private AddOrder CreateAddOrder(String[] marketDataText)
    {
        AddOrder newAddOrder = null;

        try
        {
            int timeStamp = Integer.parseInt(marketDataText[0]);
            IOrderType.orderType orderType = IOrderType.orderType.ADD;
            String orderId = marketDataText[2];
            char side = Character.toUpperCase(marketDataText[3].charAt(0));
            double price = Double.parseDouble(marketDataText[4]);
            short orderSize = Short.parseShort(marketDataText[5]);

            newAddOrder = new AddOrder(timeStamp, orderType, orderId, side, price, orderSize);
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
            IOrderType.orderType orderType = IOrderType.orderType.REDUCE;
            String orderId = marketDataText[2];
            short orderSize = Short.parseShort(marketDataText[3]);

            newReduceOrder = new ReduceOrder(timeStamp, orderType, orderId, orderSize);
        }

        catch(Exception e)
        {
            System.err.println("Invalid input data: " + e);
        }
        return newReduceOrder;
    }
}

