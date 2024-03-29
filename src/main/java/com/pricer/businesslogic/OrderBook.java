package com.pricer.businesslogic;

import com.pricer.interfaces.IAction;
import com.pricer.interfaces.IOrderType;
import com.pricer.interfaces.ISide;

import java.io.*;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;

/**
 Calculates all Add orders & Reduce orders. Prints profits from selling,
 or expense incurred from buying.
 */
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

    private DecimalFormat twoDigitFormat = new DecimalFormat("0.00");

    /**
     Constructor. Initializes lists for buy orders & sell orders.
     * @param targetSize The "target" number of shares to reach before you buy or sell the imaginary stock.
     */
    public OrderBook(int targetSize)
    {
        this.targetSize = targetSize;
        orderMap = new HashMap<>();  //Contains all ADD orders read in from the data file. Does not contain Reduce orders.
        askList = new ArrayList();  //Contains all SELL orders in OrderMap.
        bidList = new ArrayList();  //Contains all BUY orders in OrderMap.
    }

    /**
     Reads all lines of financial data from a file. Breaks down each line of data into components to buy or sell shares.
     *
     * @param inputFileNameAndPath The filepath & name of the file you read financial data from.
     * @return None.
     */
    public void readFinanceData(String inputFileNameAndPath) throws IOException
    {
        BufferedReader reader = null;
        String financeDataText = null;

        /*Not put in try / catch statement so error could be sent back to method caller.*/
        InputStream is = new FileInputStream(inputFileNameAndPath);
        reader = new BufferedReader(new InputStreamReader(is));

        while ((financeDataText = reader.readLine()) != null)
        {
                /*
                 * Parse each character in this line of text.
                 * Assign it to a specific piece of data in an Order.
                 */
                String[] orderData = financeDataText.split(" ");
                switch (orderData[1])
                {
                    case "A":
                    {
                        //Make an Add order.
                        AddOrder addOrder = this.createAddOrder(orderData);
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
                            this.calculateFinanceData(addOrder);
                        }
                        break;
                    }

                    case "R":
                    {
                        //Make a Reduce order.
                        ReduceOrder reduceOrder = this.createReduceOrder(orderData);
                        if(reduceOrder != null)
                        {
                            this.calculateFinanceData(reduceOrder);
                        }
                        break;
                    }
                    default:
                    {
                        //Throw an error. There are only Add & Reduce orders.
                        System.err.println("Invalid input data at position 1:" + orderData[1]);
                        break;
                    }
                }
        }
        reader.close();
    }

    /**
     Reads a single lines of financial data from a file. Determines if the order that was read is an Add or Reduce order,
     & sends the order to its' appropriate method for calculating.
     *
     * @param orderToCalculate The specific line of financial data from a file.
     * @return None.
     */
    public void calculateFinanceData(Order orderToCalculate)
    {
        switch (orderToCalculate.orderType)
        {
            case ADD:
            {
               this.calculateAddOrders((AddOrder)orderToCalculate);
                break;
            }

            case REDUCE:
            {
                this.calculateReduceOrders((ReduceOrder)orderToCalculate);
                break;
            }
            default:
            {
                break;
            }
        }
    }


    /**
     Reads a single Add Order. If it's a buy order, the number of bids are updated, along with the income from selling
     shares. If it's a sell order, the number of asks are updated, along with the expense of buying shares.
     *
     * @param insertedOrder  The Add Order to be added to a tally of Add orders.
     * @return None.
     */
    private void calculateAddOrders(AddOrder insertedOrder)
    {
        if (insertedOrder.getSide() == side.BUY)
        {
            bidShareCount += insertedOrder.getSize();

            if (bidShareCount >= targetSize) //The bid count is greater than the target size.
            {
                //If the income changed, print it. If it didn't, print / do nothing.
                income = this.calculateIncome();
                if(previousIncome != income)
                {
                    this.writeFinanceData(insertedOrder.getTimeStamp(), insertedOrder.getAction().actionValue(), income);
                    previousIncome = income;
                }
            }
        }
        else if (insertedOrder.getSide() == side.SELL)
        {
            askShareCount += insertedOrder.getSize();

            if (askShareCount >= targetSize) //The ask count is greater than the target size.
            {
                //If the expense changed, print it. If it didn't, print / do nothing.
                expense = this.calculateExpense();
                if(previousExpense != expense)
                {
                    this.writeFinanceData(insertedOrder.getTimeStamp(), insertedOrder.getAction().actionValue(), expense);
                    previousExpense = expense;
                }
            }
        }
    }


    /**
     Reads a single Reduce Order. Subtracts the number of shares to buy from an existing Add Order.
     *
     * @param reduceOrder  An Order used to subtract shares from an existing Add Order.
     * @return None.
     */
    private void calculateReduceOrders(ReduceOrder reduceOrder)
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

                if(bidShareCount >= targetSize) //The bid count is greater than the target size.
                {
                    //If the income changed, print it. If it didn't, print / do nothing.
                    income = this.calculateIncome();
                    if(previousIncome != income)
                    {
                        this.writeFinanceData(reduceOrder.getTimeStamp(), addOrderToReduce.getAction().actionValue(), income);
                        previousIncome = income;
                    }
                }
                else //The bid count is less than the target size.
                {
                    //If the income changed, print it. If it didn't, print / do nothing.
                    income = 0.0;
                    if (previousIncome != income)
                    {
                        this.writeFinanceData(reduceOrder.getTimeStamp(), addOrderToReduce.getAction().actionValue(), 0.0);
                        previousIncome = 0.0;
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

                if(askShareCount >= targetSize)  //The ask count is greater than the target size.
                {
                    //If the expense changed, print it. If it didn't, print / do nothing.
                    expense = this.calculateExpense();
                    if(expense != previousExpense)
                    {
                        this.writeFinanceData(reduceOrder.getTimeStamp(), addOrderToReduce.getAction().actionValue(), expense);
                        previousExpense = expense;
                    }
                }
                else //The ask count is less than the target size.
                {
                    //If the expense changed, print NA. If it didn't, print / do nothing.
                    expense = 0.0;
                    if (previousExpense != expense)
                    {
                        this.writeFinanceData(reduceOrder.getTimeStamp(), addOrderToReduce.getAction().actionValue(), 0.0);
                        previousExpense = 0.0;
                    }
                }
            }
            else
            {}

        }
    }

    /**
     Calculate the expense of buying shares of this stock. Multiplies the share price by the number of shares
     that will be bought.
     *
     * @return The expense of buying shares of this stock.
     */
    private double calculateExpense()
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

    /**
     Calculate the income from selling shares of this stock. Multiplies the share price by the number of shares
     that will be sold.
     *
     * @return The income from selling shares of this stock.
     */
    private double calculateIncome()
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

    /**
     Outputs new finance data, but only if the price changed.
     *
     * @return None. Data is displayed as output.
     */
    public void writeFinanceData(int timestamp, char orderAction, double expenditure)
    {
        String expenseString = (expenditure > 0.0 ? twoDigitFormat.format(expenditure) :  "NA");
        System.out.println(timestamp + " " + orderAction + " " + expenseString);
    }


    /**
     Creates a single Add Order from a single line of financial data.
     *
     * @param marketDataText A single line of finance data read in from a text file.
     * @return Fully populated Add Order.
     */
    private AddOrder createAddOrder(String[] marketDataText)
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


    /**
     Creates a single Reduce Order from a single line of financial data.
     *
     * @param marketDataText A single line of finance data read in from a text file.
     * @return Fully populated Reduce Order.
     */
    private ReduceOrder createReduceOrder(String[] marketDataText)
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

