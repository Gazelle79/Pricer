package main.com.pricer.businesslogic;

public class AddOrder extends Order
{
    //public enum sideType {BUY, SELL}

    private char side = 'B';
    private double price = 0.0;

    public char getSide()
    {
        return side;
    }

    public double getPrice()
    {
        return price;
    }

    public int getTimeStamp()
    {
        return super.getTimeStamp();
    }

    public char getOrderType()
    {
        return super.getOrderType();
    }

    public String getId()
    {
        return super.getId();
    }

    public int getSize()
    {
        return super.getOrderSize();
    }



    public AddOrder(int timeStamp, char orderType, String orderId, char side, double price, int orderSize)
    {
        super.setTimeStamp(timeStamp);
        super.setOrderType(orderType);
        super.setId(orderId);
        super.setOrderSize(orderSize);
        this.side = side;
        this.price = price;
    }
}
