package com.pricer.unittests;

import com.pricer.businesslogic.AddOrder;
import com.pricer.businesslogic.OrderBook;
import main.java.com.pricer.interfaces.IOrderType;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import java.io.File;
import java.io.IOException;

public class OrderBookTest
{

    private String filePathWithoutFile = "";  //a directory path with no filename attached
    private String fileNameWithoutPath = "";  //a file with no directory path attached

    private String inputFileNameAndPath =  ""; //read in data for a contact card

    private String helper = "";
    private String pathToPricer = "";

    String goodTargetSize = "200";
    String badTargetSize = "Terrier";
    int actualTargetSize = 200;

    private OrderBook orderBook = null;

    private String addOrder1Text = "";
    private String addOrder2Text = "";

    private AddOrder newAddOrder1 = null;
    private AddOrder newAddOrder2 = null;

    public OrderBookTest()
    {
        helper = new File(".").getAbsolutePath();
        pathToPricer = helper.substring(0, helper.indexOf("/Pricer"));

        inputFileNameAndPath =  pathToPricer + "/Pricer/InputFiles/PricerInput2.txt";
        filePathWithoutFile = pathToPricer + "/Pricer/InputFiles/";
        fileNameWithoutPath = "/PricerOutput.txt";

        addOrder1Text = "28800538 A b S 44.26 100";
        addOrder2Text = "28800562 A c B 44.10 100";

    }

    @Test
    public void readFinanceDataTest() throws IOException
    {
        int targetSize = Integer.parseInt(goodTargetSize);
        assertEquals(targetSize, actualTargetSize);
        orderBook = new OrderBook(targetSize);

        File inputFile = new File(inputFileNameAndPath);
        assertNotNull(inputFile);
        assertTrue(inputFile.exists());

        orderBook.readFinanceData(inputFileNameAndPath);
    }

    private AddOrder createAddOrder(String[] addOrderData)
    {
        AddOrder newAddOrder = null;

        int timeStamp = Integer.parseInt(addOrderData[0]);
        IOrderType.orderType orderType = IOrderType.orderType.ADD;
        String orderId = addOrderData[2];
        char side = Character.toUpperCase(addOrderData[3].charAt(0));
        double price = Double.parseDouble(addOrderData[4]);
        short orderSize = Short.parseShort(addOrderData[5]);

        newAddOrder = new AddOrder(timeStamp, orderType, orderId, side, price, orderSize);

        return newAddOrder;
    }

}