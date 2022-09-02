package com.pricer.businesslogic;

import java.io.File;

public class Pricer {

    /**
     Main point of entry. It keeps track of the profit from selling all shares of an imaginary stock, or the cost of buying
     all shares.
     * <br><br>Buying any number of shares is an "Add" order. <br>Selling any number of shares is a "Reduce" order.
     * <br>
     * <li>When "Add" orders reach a certain amount of shares, you calculate the profit you'd make from selling them all.</li>
     * <li>When "Reduce" orders reach a certain amount of shares, you calculate the expense you'd incur from buying them all.</li>
     *
     * @param args The "target" number of shares to reach before you buy or sell the imaginary stock.
     * @return None. All data is printed to the screen.
     */
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
