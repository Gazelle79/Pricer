package main.com.pricer.businesslogic;

import java.io.File;

public class Pricer {

    public static void main(String[] args)
    {
        String currentPath = new File("").getAbsolutePath();
        int targetSize = 200;

        //String inputFileNameAndPath =  currentPath + "/InputFiles/PricerInput2.txt";
        String inputFileNameAndPath =  currentPath + "/InputFiles/pricer.in";

        try
        {
            if(args.length >= 1)
            {
                targetSize = Integer.parseInt(args[0]);
            }
            //else, targetSize = 200 by default

            //Get *all* market data. Read it in & display it.
            OrderBook shareOrderBook = new OrderBook(targetSize);
            shareOrderBook.readFinanceData(inputFileNameAndPath);
        }
        catch(Exception e)
        {
            System.err.println("Something went wrong: " + e.toString());
        }
    }
}
