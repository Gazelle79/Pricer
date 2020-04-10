package main.com.pricer.businesslogic;

import java.io.BufferedReader;
import java.io.*;
import java.util.Hashtable;


public class PricerParser
{
    //private List<Order> orderList = null;
    private Hashtable<String, AddOrder> orderTable = null;
    private Hashtable<String, Double> sellTable = null; //sort Descending
    private Hashtable<String, Double> buyTable = null; //sort Ascending
    private final int targetSize = 200;
    private int remainingShares = targetSize;

    private int buyShareCount = 0;
    private double buyDollarAmount = 0.0;
    private int sellShareCount = 0;
    private double sellDollarAmount = 0.0;


    public PricerParser()
    {
        orderTable = new Hashtable<>();
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
            switch(orderData[1])
            {
                case "A":
                    {
                        //Make an Add order.
                        AddOrder addOrder = this.CreateAddOrder(orderData);
                        orderTable.put(addOrder.getId(), addOrder);
                        this.CalculateMarketData(addOrder);
                        break;
                    }
                case "R":
                    {
                        //Make an Reduce order.
                        ReduceOrder reduceOrder = this.CreateReduceOrder(orderData);
                        //Do NOT add a Reduce order to the book! Doing so means that a unique string ID isn't unique
                        // anymore, because an add and a reduce order both refer to it.
                        this.CalculateMarketData(reduceOrder);
                        break;
                    }
                default:
                    {
                        //Do nothing, throw an error, etc.
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
        switch (orderToCalculate.getOrderType())
        {
            /*
             * ADD ORDER:
             * Keep a running tab of the dollar totals & share count totals for Buy (b) and Sell (s) for each
             * order. When share count exceeds the target_size, print the share amount and the
             * dollar total as output.
             * */
            case 'A':
            {
               AddOrder order = (AddOrder)orderToCalculate;

               if (order.getSide() == 'B')
               {
                   buyShareCount += order.getSize();
                   buyDollarAmount += (buyShareCount * order.getPrice());
                   if (buyShareCount >= targetSize)
                   {
                       this.WriteMarketData(order.getTimeStamp(), order.getSide(), buyDollarAmount);
                   }
               }
               else if (order.getSide() == 'S')
               {
                   sellShareCount += order.getSize();
                   sellDollarAmount += (sellShareCount * order.getPrice());
                   if (sellShareCount >= targetSize)
                   {
                       this.WriteMarketData(order.getTimeStamp(), order.getSide(), sellDollarAmount);
                   }
               }
               else{}

                break;
            }

            /* REDUCE ORDER:
             * Match this order to one that's in the list. Reduce the number of overall buy / sell shares accordingly.
             * Reduce number of shares from the order that this reduce order is pointing to.
             * When this order's shares are reduced to zero, remove that order from the book.
             * Reduce Total Share count and total dollar amount accordingly.
             * */
            case 'R':
            {
                ReduceOrder reduceOrder = (ReduceOrder)orderToCalculate;
                String reduceOrderId = reduceOrder.getId();
                AddOrder addOrderToReduce = null;

                if(orderTable.containsKey(reduceOrderId))
                {
                    addOrderToReduce = orderTable.get(reduceOrderId);
                    int sharesToRemove = addOrderToReduce.getSize() - reduceOrder.getSize();

                    if(addOrderToReduce.getSide() == 'B')
                    {
                        buyShareCount -= addOrderToReduce.getSize();
                        buyDollarAmount -= (buyShareCount * addOrderToReduce.getPrice());
                        remainingShares -= sharesToRemove;
                    }
                    else if(addOrderToReduce.getSide() == 'S')
                    {
                        sellShareCount -= addOrderToReduce.getSize();
                        sellDollarAmount -= (sellShareCount * addOrderToReduce.getPrice());
                        remainingShares -= sharesToRemove;
                    }
                    else
                    {}

                    if(remainingShares == 0)
                    {
                        orderTable.remove(addOrderToReduce);
                    }
                }
                break;
            }
            default:
            {
                break;
            }
        }
    }

    //Write out Market data, where it's appropriate.
    public void WriteMarketData(int timestamp, char action, double expense)
    {
        System.out.println(timestamp + " " + action + " " + expense);
    }

    /*Take a line of text. Convert it into an Order object by parsing data & putting that data into an
    * Order object. Put that Order object in a List.*/
    private AddOrder CreateAddOrder(String[] marketDataText)
    {
        int timeStamp = Integer.parseInt(marketDataText[0]);
        char orderType = marketDataText[1].charAt(0);
        String orderId = marketDataText[2];
        char side = marketDataText[3].charAt(0);
        double price = Double.parseDouble(marketDataText[4]);
        short orderSize = Short.parseShort(marketDataText[5]);

        return new AddOrder(timeStamp, orderType, orderId, side, price, orderSize);
    }

    private ReduceOrder CreateReduceOrder(String[] marketDataText)
    {
        int timeStamp = Integer.parseInt(marketDataText[0]);
        char orderType = marketDataText[1].charAt(0);
        String orderId = marketDataText[2];
        short orderSize = Short.parseShort(marketDataText[3]);

        return new ReduceOrder(timeStamp, orderType, orderId, orderSize);
    }
}
