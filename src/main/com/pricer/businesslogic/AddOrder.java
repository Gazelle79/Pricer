package main.com.pricer.businesslogic;

public class AddOrder extends Order
{
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
    public orderType getOrderType() { return super.getThisOrderType(); }
    public String getId() { return super.getId(); }
    public int getSize() { return super.getOrderSize(); }

    private char side = 'A';
    private double price = 0.0;

    public AddOrder(int timeStamp, char orderType, String orderId, char side, double price, short orderSize)
    {
        super.setTimeStamp(timeStamp);
        super.setOrderType(orderType);
        super.setId(orderId);
        super.setOrderSize(orderSize);
        this.side = side;
        this.price = price;
    }
}
