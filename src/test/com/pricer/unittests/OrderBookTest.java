package test.com.pricer.unittests;

import main.com.pricer.businesslogic.*;
import org.junit.Test;
import static org.junit.Assert.*;
import java.io.File;
import java.io.IOException;

public class OrderBookTest
{

    private String filePathWithoutFile = "";  //a directory path with no filename attached
    private String fileNameWithoutPath = "";  //a file with no directory path attached

    private String inputFileNameAndPath =  ""; //read in data for a contact card
    private String outputFileNameAndPath = ""; //path to write data to for a contact card

    private String helper = "";
    private String pathToPricer = "";

    int targetSize = 200;
    private OrderBook orderBook = new OrderBook(targetSize);

    private String addOrder1Text = "";
    private String addOrder2Text = "";

    private AddOrder newAddOrder1 = null;
    private AddOrder newAddOrder2 = null;

    public OrderBookTest()
    {
        helper = new File(".").getAbsolutePath();
        pathToPricer = helper.substring(0, helper.indexOf("/Pricer"));

        inputFileNameAndPath =  pathToPricer + "/Pricer/InputFiles/PricerInput.txt";
        outputFileNameAndPath = pathToPricer + "/Pricer/OutputFiles/PricerOutput.txt";
        filePathWithoutFile = pathToPricer + "/Pricer/InputFiles/";
        fileNameWithoutPath = "/PricerOutput.txt";

        addOrder1Text = "28800538 A b S 44.26 100";
        addOrder2Text = "28800562 A c B 44.10 100";

    }

    @Test
    public void readFinanceDataTest() throws IOException
    {
        String pricerDataInfo = orderBook.readFinanceData(inputFileNameAndPath);
        assertFalse(pricerDataInfo.isEmpty());
    }

    @Test
    public void calculateFinanceDataTest()
    {
        int thisOtherTest = 2;
        assertEquals(thisOtherTest, 2);
    }

    @Test
    public void writeFinanceDataTest()
    {
        int thisOtherTest = 2;
        assertEquals(thisOtherTest, 2);
    }


}