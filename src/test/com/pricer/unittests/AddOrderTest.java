package test.com.pricer.unittests;

import main.com.pricer.businesslogic.AddOrder;
import main.com.pricer.interfaces.IOrderType;
import main.com.pricer.interfaces.ISide;
import org.junit.Test;
import static org.junit.Assert.*;

public class AddOrderTest
{
    private String addOrder1Text = null;
    private String addOrder2Text = null;
    private AddOrder newAddOrder1 = null;
    private AddOrder newAddOrder2 = null;

    public AddOrderTest()
    {
        addOrder1Text = "28800538 A b S 44.26 100";
        addOrder2Text = "28800562 A c B 44.10 100";
    }

    @Test
    public void createAddOrderTest()
    {
        String[] addOrder1Fields = addOrder1Text.split(" ");
        newAddOrder1 = this.createAddOrder(addOrder1Fields);

        assertEquals(newAddOrder1.getTimeStamp(), 28800538);
        assertEquals(newAddOrder1.getOrderType(), IOrderType.orderType.ADD);
        assertEquals(newAddOrder1.getId(), "b");
        assertEquals(newAddOrder1.getSide(), ISide.side.SELL);
        assertEquals(newAddOrder1.getPrice(), 44.26, 0.0);
        assertEquals(newAddOrder1.getSize(), 100);


        String[] addOrder2Fields = addOrder2Text.split(" ");
        newAddOrder2 = this.createAddOrder(addOrder2Fields);

        assertEquals(newAddOrder2.getTimeStamp(), 28800562);
        assertEquals(newAddOrder2.getOrderType(), IOrderType.orderType.ADD);
        assertEquals(newAddOrder2.getId(), "c");
        assertEquals(newAddOrder2.getSide(), ISide.side.BUY);
        assertEquals(newAddOrder2.getPrice(), 44.10, 0.0);
        assertEquals(newAddOrder2.getSize(), 100);

    }

    @Test
    public void reduceSharesTest()
    {
        String[] addOrder1Fields = addOrder1Text.split(" ");
        newAddOrder1 = this.createAddOrder(addOrder1Fields);

        int previousShares = newAddOrder1.getSize();
        int sharesToReduce = 75;
        assertEquals(previousShares, 100);

        newAddOrder1.ReduceShares(sharesToReduce);

        assertEquals((previousShares - sharesToReduce), 25);
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