package main.com.pricer.businesslogic;

import java.io.BufferedReader;
import java.io.*;
import java.util.ArrayList;
import java.util.List;


public class PricerParser
{
    private List<Order> orderList = null;
    private int targetSize = 200;

    private int buyShareCount = 0;
    private double buyDollarAmount = 0.0;
    private int sellShareCount = 0;
    private double sellDollarAmount = 0.0;



    public PricerParser()
    {
        orderList = new ArrayList<Order>();

    }

    public String ReadMarketData(String inputFileNameAndPath) throws IOException
    {
        BufferedReader reader = null;
        String marketDataText = null;
        StringBuilder contents = new StringBuilder();

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
                        Order addOrder = this.CreateAddOrder(orderData);
                        orderList.add(addOrder);
                        this.CalculateMarketData(addOrder);
                        break;
                    }
                case "R":
                    {
                        //Make an Reduce order.
                        Order reduceOrder = this.CreateReduceOrder(orderData);
                        orderList.add(reduceOrder);
                        this.CalculateMarketData(reduceOrder);
                        break;
                    }
                default:
                    {
                        //Do nothing, throw an error, etc.
                        break;
                    }

            }
            contents.append(marketDataText + "\n");
        }
        reader.close();

        return contents.toString();
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
            case ADD:
            {
               AddOrder order = (AddOrder)orderToCalculate;

               if (order.getSide() == AddOrder.sideType.BUY)
               {
                   buyShareCount += order.getSize();
                   buyDollarAmount += (buyShareCount * order.getPrice());
               }
               else if (order.getSide() == AddOrder.sideType.SELL)
               {
                   sellShareCount += order.getSize();;
                   sellDollarAmount += (buyShareCount * order.getPrice());
               }
               else{}

               if(buyShareCount >= targetSize || sellShareCount >= targetSize)
               {
                   this.WriteMarketData();
               }

                break;
            }

            /* REDUCE ORDER:
             * Match this order to one that's in the list. Reduce the number of shares accordingly. When
             * shares are reduced to zero, remove that order from the list / book.
             * Reduce Total Share count and total dollar amount accordingly.
             * */
            case REDUCE:
            {
                ReduceOrder order = (ReduceOrder)orderToCalculate;

                this.WriteMarketData();
                orderList.remove(order.getId());

                break;
            }

            default:
            {
                break;
            }
        }
    }

    //Write out Market data, where it's appropriate.
    public void WriteMarketData()
    {
        //Write something!!
    }

    /*Take a line of text. Convert it into an Order object by parsing data & putting that data into an
    * Order object. Put that Order object in a List.*/
    private Order CreateAddOrder(String[] marketDataText)
    {
        int timeStamp = Integer.parseInt(marketDataText[0]);
        char orderType = marketDataText[1].charAt(0);
        String orderId = marketDataText[2];
        char sideType = marketDataText[3].charAt(0);
        double price = Double.parseDouble(marketDataText[4]);
        short orderSize = Short.parseShort(marketDataText[5]);

        return new AddOrder(timeStamp, orderType, orderId, sideType, price, orderSize);
    }

    private Order CreateReduceOrder(String[] marketDataText)
    {
        int timeStamp = Integer.parseInt(marketDataText[0]);
        char orderType = marketDataText[1].charAt(0);
        String orderId = marketDataText[2];
        short orderSize = Short.parseShort(marketDataText[3]);

        return new ReduceOrder(timeStamp, orderType, orderId, orderSize);
    }
}
