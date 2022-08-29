package com.pricer.unittests;

import com.pricer.businesslogic.AddOrder;
import com.pricer.businesslogic.ReduceOrder;
import main.java.com.pricer.interfaces.IOrderType;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;


public class ReduceOrderTest
{

    private String reduceOrder1Text = null;
    private String reduceOrder2Text = null;
    private String addOrder1Text = null;
    private String addOrder2Text = null;

    private AddOrder newAddOrder1 = null;
    private AddOrder newAddOrder2 = null;
    private ReduceOrder newReduceOrder1 = null;
    private ReduceOrder newReduceOrder2 = null;


    public ReduceOrderTest()
    {
        reduceOrder1Text = "28800744 R b 100";
        reduceOrder2Text = "28800796 R d 157";
        addOrder1Text = "28800538 A b S 44.26 100";
        addOrder2Text = "28800758 A d B 44.18 157";
    }

    @Test
    public void createReduceOrderTest()
    {
        String[] reduceOrder1Fields = reduceOrder1Text.split(" ");
        newReduceOrder1 = createReduceOrder(reduceOrder1Fields);

        assertEquals(newReduceOrder1.getTimeStamp(), 28800744);
        assertEquals(newReduceOrder1.getOrderType(), IOrderType.orderType.REDUCE);
        assertEquals(newReduceOrder1.getId(), "b");
        assertEquals(newReduceOrder1.getSize(), 100);

        String[] reduceOrder2Fields = reduceOrder2Text.split(" ");
        newReduceOrder2 = createReduceOrder(reduceOrder2Fields);

        assertEquals(newReduceOrder2.getTimeStamp(), 28800796);
        assertEquals(newReduceOrder2.getOrderType(), IOrderType.orderType.REDUCE);
        assertEquals(newReduceOrder2.getId(), "d");
        assertEquals(newReduceOrder2.getSize(), 157);
    }

    @Test
    public void reduceSharesTest()
    {
        int sharesToReduce = 0;

        String[] reduceOrder1Fields = reduceOrder1Text.split(" ");
        newReduceOrder1 = createReduceOrder(reduceOrder1Fields);

        String[] addOrder1Fields = addOrder1Text.split(" ");
        newAddOrder1 = createAddOrder(addOrder1Fields);

        sharesToReduce = newReduceOrder1.getSize(); //100 shares
        newAddOrder1.ReduceShares(sharesToReduce);

        assertEquals(newAddOrder1.getSize(), 0);

        String[] reduceOrder2Fields = reduceOrder2Text.split(" ");
        newReduceOrder2 = createReduceOrder(reduceOrder2Fields);

        String[] addOrder2Fields = addOrder2Text.split(" ");
        newAddOrder2 = createAddOrder(addOrder2Fields);

        sharesToReduce = newReduceOrder2.getSize(); //157 shares
        newAddOrder2.ReduceShares(sharesToReduce);

        assertEquals(newAddOrder1.getSize(), 0);
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

    private ReduceOrder createReduceOrder(String[] reduceOrderData)
    {
        ReduceOrder newReduceOrder = null;

        int timeStamp = Integer.parseInt(reduceOrderData[0]);
        IOrderType.orderType orderType = IOrderType.orderType.REDUCE;
        String orderId = reduceOrderData[2];
        short orderSize = Short.parseShort(reduceOrderData[3]);

        newReduceOrder = new ReduceOrder(timeStamp, orderType, orderId, orderSize);

        return newReduceOrder;
    }

}