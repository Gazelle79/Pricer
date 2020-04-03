package main.com.pricer.businesslogic;

import java.io.File;

public class PricerMain {

    public static void main(String[] args)
    {
        String pricerDataInfo = "";
        String currentPath = new File("").getAbsolutePath();

        String inputFileNameAndPath =  currentPath + "/InputFiles/PricerInput.txt";
        //String inputFileNameAndPath =  currentPath + "/InputFiles/pricer.in";
        String outputFileNameAndPath = currentPath + "/OutputFiles/PricerOutput.txt";

        if(args.length > 1)
        {
            inputFileNameAndPath =  args[0];
            outputFileNameAndPath = args[1];
        }

        //Get *all* market data. Read it in & display it.
        PricerParser dataParser = new PricerParser();
        try
        {
            pricerDataInfo = dataParser.readMarketData(inputFileNameAndPath);
            System.out.println("Reading from file: \n" + inputFileNameAndPath
                    + "\n\n Market data: \n\n" + pricerDataInfo + "\n");
        }
        catch(Exception e)
        {
            System.out.println("Something went wrong: " + e.getMessage());
        }


    }
}
