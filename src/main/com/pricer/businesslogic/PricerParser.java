package main.com.pricer.businesslogic;

import java.io.BufferedReader;
import java.io.*;
import java.util.ArrayList;
import java.util.List;


public class PricerParser
{
    private List<Order> orderList = null;
    private final int targetSize = 200;

    public PricerParser()
    {
        orderList = new ArrayList<Order>();

    }

    public String readMarketData(String inputFileNameAndPath) throws IOException
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
                        Order addOrder = this.createAddOrder(orderData);
                        orderList.add(addOrder);
                        break;
                    }
                case "R":
                    {
                        //Make an Reduce order.
                        Order reduceOrder = this.createReduceOrder(orderData);
                        orderList.add(reduceOrder);
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

    public void calculateMarketData()
    {

    }

    public void WriteMarketData()
    {

    }

    /*Take a line of text. Convert it into an Order object by parsing data & putting that data into an
    * Order object. Put that Order object in a List.*/
    private Order createAddOrder(String[] marketDataText)
    {
        int timeStamp = Integer.parseInt(marketDataText[0]);
        char orderType = marketDataText[1].charAt(0);
        String orderId = marketDataText[2];
        char side = marketDataText[3].charAt(0);
        double price = Double.parseDouble(marketDataText[4]);
        short orderSize = Short.parseShort(marketDataText[5]);

        return new AddOrder(timeStamp, orderType, orderId, side, price, orderSize);
    }

    private Order createReduceOrder(String[] marketDataText)
    {
        int timeStamp = Integer.parseInt(marketDataText[0]);
        char orderType = marketDataText[1].charAt(0);
        String orderId = marketDataText[2];
        short orderSize = Short.parseShort(marketDataText[3]);

        return new ReduceOrder(timeStamp, orderType, orderId, orderSize);
    }
}
