package main.com.pricer.businesslogic;

import java.io.File;

public class PricerMain {

    public static void main(String[] args)
    {
        String pricerDataInfo = "";
        String currentPath = new File("").getAbsolutePath();
        int targetSize = 200;

        String inputFileNameAndPath =  currentPath + "/InputFiles/PricerInput.txt";
        //String inputFileNameAndPath =  currentPath + "/InputFiles/pricer.in";
        //String outputFileNameAndPath = currentPath + "/OutputFiles/PricerOutput.txt";

        if(args.length > 1)
        {
            targetSize = Integer.parseInt(args[0]);
        }

        //Get *all* market data. Read it in & display it.
        OrderBook dataParser = new OrderBook(targetSize);
        try
        {
            pricerDataInfo = dataParser.ReadMarketData(inputFileNameAndPath);
            //System.out.println("Reading from file: \n" + inputFileNameAndPath
                //+ "\n\n Market data: \n\n" + pricerDataInfo + "\n");
        }
        catch(Exception e)
        {
            System.out.println("Something went wrong: " + e.getMessage());
        }


    }
}
