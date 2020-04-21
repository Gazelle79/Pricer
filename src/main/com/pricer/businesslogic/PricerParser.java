package main.com.pricer.businesslogic;

import java.io.BufferedReader;
import java.io.*;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class PricerParser
{
    private HashMap<String, AddOrder> orderMap = null;
    //private HashMap<String, Double> bidMap = null; //A HashMap that takes an ID and a bid price. Corresponds to an object in orderMap.
    private List<AddOrder> bidList = null; //A list to sort the orders in bidMap.

    private final int targetSize = 200;
    private int remainingShares = targetSize;

    private int buyShareCount = 0;
    private int sellShareCount = 0;


    public PricerParser()
    {
        orderMap = new HashMap<>();
        bidList = new ArrayList();
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
                        orderMap.put(addOrder.getId(), addOrder);

                        //Create a seperate Map for bids. Contains only order id & bid price.
                        //Vector will sort this later to keep track of highest & lowest bids.
                        ///bidMap.put(addOrder.getId(), addOrder.getPrice());
                        bidList.add(addOrder);

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
        switch (orderToCalculate.orderType)
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
                   if (buyShareCount >= targetSize)
                   {
                       /*Look at the sorted bid list. Use the id # to get the corresponding bid in
                       OrderMap. In descending order, hit each bid:
                        - # of shares they wanted
                        - the price they wanted
                        Multiply each bid price times the number of shares. Add this number to a total.
                        (BuyDollarAmount)

                        Do this for all bids in the list until the number of bids calculated is equal
                        to targetSize.*/

                       Collections.sort(bidList, Collections.reverseOrder()); //BUY: Sort ALL bids from lowest to highest.

                       int currentShareCount = 0;
                       double income = 0.0;
                       AddOrder orderToBuy = null;

                       for(int i = 0; i < bidList.size(); i++)
                       {
                           String thisId = bidList.get(i).getId();
                           orderToBuy = orderMap.get(thisId);

                           if((orderToBuy.getSize() + currentShareCount) > targetSize)
                           {
                               int adjustedSharesToBuy = targetSize - currentShareCount;

                               income += (adjustedSharesToBuy * orderToBuy.getPrice());
                               remainingShares = (currentShareCount + orderToBuy.getSize()) - targetSize;
                               currentShareCount += adjustedSharesToBuy;
                           }
                           else
                           {
                               income += (orderToBuy.getSize() * orderToBuy.getPrice());
                               remainingShares = targetSize - currentShareCount;
                               currentShareCount += orderToBuy.getSize();
                           }

                           if( currentShareCount == 200 )
                           { break; }

                           this.WriteMarketData(order.getTimeStamp(), order.getSide(), income);
                       }

                   }
               }
               else if (order.getSide() == 'S')
               {
                   sellShareCount += order.getSize();
                   if (sellShareCount >= targetSize)
                   {
                       Collections.sort(bidList, new PriceCompare()); //SELL: Sort ALL bids from highest to lowest.

                       int currentShareCount = 0;
                       double expense = 0.0;
                       AddOrder orderToSell = null;

                       for(int i = 0; i < bidList.size(); i++)
                       {
                           String thisId = bidList.get(i).getId();
                           orderToSell = orderMap.get(thisId);

                           if((orderToSell.getSize() + currentShareCount) > targetSize)
                           {
                               int adjustedShareSize = targetSize - currentShareCount;

                               expense += (adjustedShareSize * orderToSell.getPrice());
                               remainingShares = (currentShareCount + orderToSell.getSize()) - targetSize;
                               currentShareCount += adjustedShareSize;
                           }
                           else {
                               expense += (orderToSell.getSize() * orderToSell.getPrice());
                               remainingShares = targetSize - currentShareCount;
                               currentShareCount += orderToSell.getSize();
                           }

                           if( currentShareCount == 200 )
                           { break; }

                           this.WriteMarketData(order.getTimeStamp(), order.getSide(), expense);
                       }
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

                if(orderMap.containsKey(reduceOrderId))
                {
                    addOrderToReduce = orderMap.get(reduceOrderId);

                    //Remove shares from the Add order.
                    int sharesToRemove = reduceOrder.getSize();
                    addOrderToReduce.removeShares(reduceOrder.getSize());

                    if(addOrderToReduce.getSide() == 'B')
                    {
                        buyShareCount -= sharesToRemove;
                        remainingShares += sharesToRemove;
                    }
                    else if(addOrderToReduce.getSide() == 'S')
                    {
                        sellShareCount -= sharesToRemove;
                        remainingShares += sharesToRemove;
                    }
                    else
                    {}

                    if(addOrderToReduce.getSize() == 0)
                    {
                        orderMap.remove(addOrderToReduce);
                        bidList.remove(addOrderToReduce);
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
    public void WriteMarketData(int timestamp, char side, double expense)
    {
        char action = (side == 'B' ? 'S' : 'B');

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

