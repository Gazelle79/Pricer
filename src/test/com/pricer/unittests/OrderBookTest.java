package test.com.pricer.unittests;

import main.com.pricer.businesslogic.*;
import org.junit.Test;
import static org.junit.Assert.*;
import java.io.File;

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

    public OrderBookTest()
    {
        helper = new File(".").getAbsolutePath();
        pathToPricer = helper.substring(0, helper.indexOf("/Pricer"));

        inputFileNameAndPath =  pathToPricer + "/Pricer/InputFiles/PricerInput.txt";
        outputFileNameAndPath = pathToPricer + "/Pricer/OutputFiles/PricerOutput.txt";
        filePathWithoutFile = pathToPricer + "/Pricer/InputFiles/";
        fileNameWithoutPath = "/PricerOutput.txt";

    }

    @Test
    public void readMarketDataTest()
    {
        int thisTest = 1;
        assertEquals(thisTest, 1);
    }

    @Test
    public void calculateMarketDataTest()
    {
        int thisOtherTest = 2;
        assertEquals(thisOtherTest, 2);
    }

    @Test
    public void calculateAddOrdersTest()
    {
        int thisOtherTest = 2;
        assertEquals(thisOtherTest, 2);
    }

    @Test
    public void calculateReduceOrdersTest()
    {
        int thisOtherTest = 2;
        assertEquals(thisOtherTest, 2);
    }

    @Test
    public void calculateIncomeTest()
    {
        int thisOtherTest = 2;
        assertEquals(thisOtherTest, 2);
    }

    @Test
    public void calculateExpenseTest()
    {
        int thisOtherTest = 2;
        assertEquals(thisOtherTest, 2);
    }

    @Test
    public void writeMarketDataTest()
    {
        int thisOtherTest = 2;
        assertEquals(thisOtherTest, 2);
    }

    @Test
    public void createReduceOrderTest()
    {
        int thisOtherTest = 2;
        assertEquals(thisOtherTest, 2);
    }

    @Test
    public void createAddOrderTest()
    {
        int thisOtherTest = 2;
        assertEquals(thisOtherTest, 2);
    }

}