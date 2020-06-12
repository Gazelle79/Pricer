package main.com.pricer.businesslogic;

import java.io.File;

public class PricerMain {

    public static void main(String[] args)
    {
        String pricerDataInfo = "";
        String currentPath = new File("").getAbsolutePath();
        int targetSize = 200;

        //String inputFileNameAndPath =  currentPath + "/InputFiles/PricerInput.txt";
        String inputFileNameAndPath =  currentPath + "/InputFiles/PricerInput2.txt";
        //String inputFileNameAndPath =  currentPath + "/InputFiles/pricer.in";
        //String outputFileNameAndPath = currentPath + "/OutputFiles/PricerOutput.txt";

        if(args.length > 1)
        {
            targetSize = Integer.parseInt(args[0]);
        }

        //Get *all* market data. Read it in & display it.
        OrderBook shareOrderBook = new OrderBook(targetSize);
        try
        {
            pricerDataInfo = shareOrderBook.ReadFinanceData(inputFileNameAndPath);
        }
        catch(Exception e)
        {
            System.err.println("Something went wrong: " + e.toString());
        }


    }
}
