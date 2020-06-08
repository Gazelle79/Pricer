package test.com.pricer.unittests;

import org.junit.Test;
import static org.junit.Assert.*;
import java.io.File;


public class ReduceOrderTest
{

    private String filePathWithoutFile = "";  //a directory path with no filename attached
    private String fileNameWithoutPath = "";  //a file with no directory path attached

    private String inputFileNameAndPath =  ""; //read in data for a contact card
    private String outputFileNameAndPath = ""; //path to write data to for a contact card

    private String helper = "";
    private String pathToPricer = "";

    //private ReduceOrder reduceOrder = new ReduceOrder();

    public ReduceOrderTest()
    {
        helper = new File(".").getAbsolutePath();
        pathToPricer = helper.substring(0, helper.indexOf("/Pricer"));

        inputFileNameAndPath =  pathToPricer + "/Pricer/InputFiles/PricerInput.txt";
        outputFileNameAndPath = pathToPricer + "/Pricer/OutputFiles/PricerOutput.txt";
        filePathWithoutFile = pathToPricer + "/Pricer/InputFiles/";
        fileNameWithoutPath = "/PricerOutput.txt";

    }

    @Test
    public void createReduceOrderTest()
    {
        int thisTest = 1;
        assertEquals(thisTest, 1);
    }

    @Test
    public void doSomethingElseTest()
    {
        int thisOtherTest = 2;
        assertEquals(thisOtherTest, 2);
    }

}