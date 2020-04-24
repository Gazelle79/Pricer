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
    private List<AddOrder> sellBidList = null; //A list to sort the orders in bidMap.
    private List<AddOrder> buyBidList = null; //A list to sort the orders in bidMap.

    private final int targetSize = 200;
    private int remainingShares = targetSize;

    private int buyShareCount = 0;
    private int sellShareCount = 0;


    public PricerParser()
    {
        orderMap = new HashMap<>();
        sellBidList = new ArrayList();
        buyBidList = new ArrayList();
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
                    case "A": {
                        //Make an Add order.
                        AddOrder addOrder = this.CreateAddOrder(orderData);
                        orderMap.put(addOrder.getId(), addOrder);

                        //Create a seperate Map for bids. Contains only order id & bid price.
                        //Vector will sort this later to keep track of highest & lowest bids.
                        ///bidMap.put(addOrder.getId(), addOrder.getPrice());

                        if (addOrder.getSide() == 'B')
                        {
                            buyBidList.add(addOrder);
                        }
                        else if (addOrder.getSide() == 'S')
                        {
                            sellBidList.add(addOrder);
                        }

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
               AddOrder insertedOrder = (AddOrder)orderToCalculate;

               if (insertedOrder.getSide() == 'B')
               {
                   buyShareCount += insertedOrder.getSize();
                   if (buyShareCount >= targetSize)
                   {
                       //Action = SELL: Sort ALL bids from highest to lowest.
                       Collections.sort(buyBidList, new PriceCompare());

                       int currentShareCount = 0;
                       double expense = 0.0;
                       AddOrder orderToBuy = null;

                       for(int i = 0; i < buyBidList.size(); i++)
                       {
                           String thisId = buyBidList.get(i).getId();
                           orderToBuy = orderMap.get(thisId);

                           if((orderToBuy.getSize() + currentShareCount) >= targetSize)
                           {
                               int adjustedSharesToBuy = targetSize - currentShareCount;

                               expense += (adjustedSharesToBuy * orderToBuy.getPrice());
                               currentShareCount += adjustedSharesToBuy;
                               remainingShares = (currentShareCount + orderToBuy.getSize()) - targetSize;
                           }
                           else
                           {
                               expense += (orderToBuy.getSize() * orderToBuy.getPrice());
                               currentShareCount += orderToBuy.getSize();
                               remainingShares = targetSize - currentShareCount;
                           }

                       }
                       this.WriteMarketData(insertedOrder.getTimeStamp(), insertedOrder.getAction(), expense);
                   }
               }
               else if (insertedOrder.getSide() == 'S')
               {
                   sellShareCount += insertedOrder.getSize();
                   if (sellShareCount >= targetSize)
                   {
                       //Action = BUY: Sort ALL bids from lowest to highest.
                       Collections.sort(sellBidList, Collections.reverseOrder());

                       int currentShareCount = 0;
                       double income = 0.0;
                       AddOrder orderToSell = null;

                       for(int i = 0; i < sellBidList.size(); i++)
                       {
                           String thisId = sellBidList.get(i).getId();
                           orderToSell = orderMap.get(thisId);

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
                       this.WriteMarketData(insertedOrder.getTimeStamp(), insertedOrder.getAction(), income);
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
                double incomeOrExpense = 0.0;

                if(orderMap.containsKey(reduceOrderId))
                {
                    addOrderToReduce = orderMap.get(reduceOrderId);

                    //Remove shares from the Add order.
                    int sharesToRemove = reduceOrder.getSize();
                    addOrderToReduce.reduceShares(sharesToRemove);

                    if(addOrderToReduce.getSide() == 'B')
                    {
                        AddOrder buyOrderToReduce = buyBidList.get(buyBidList.indexOf(addOrderToReduce));
                        buyOrderToReduce.reduceShares(reduceOrder.getSize());

                        //Share sizes have changed. Re-sort the list.
                        //Action = SELL: Sort ALL bids from highest to lowest.
                        Collections.sort(buyBidList, new PriceCompare());

                        buyShareCount -= sharesToRemove;
                        remainingShares += sharesToRemove;
                        incomeOrExpense = (buyOrderToReduce.getSize() * buyOrderToReduce.getPrice());

                        if(buyOrderToReduce.getSize() == 0)
                        {
                            buyBidList.remove(buyOrderToReduce);
                            orderMap.remove(reduceOrderId);
                        }
                    }
                    else if(addOrderToReduce.getSide() == 'S')
                    {
                        AddOrder sellOrderToReduce = sellBidList.get(sellBidList.indexOf(addOrderToReduce));
                        sellOrderToReduce.reduceShares(reduceOrder.getSize());

                        //Share sizes have changed. Re-sort the list.
                        //Action = BUY: Sort ALL bids from lowest to highest.
                        Collections.sort(sellBidList, Collections.reverseOrder());

                        sellShareCount -= sharesToRemove;
                        remainingShares += sharesToRemove;
                        incomeOrExpense = (sellOrderToReduce.getSize() * sellOrderToReduce.getPrice());

                        if(sellOrderToReduce.getSize() == 0)
                        {
                            buyBidList.remove(sellOrderToReduce);
                            orderMap.remove(reduceOrderId);
                        }
                    }
                    else
                    {}

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

