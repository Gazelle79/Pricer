package main.com.pricer.businesslogic;

public class AddOrder extends Order
{
    public enum sideType {BUY, SELL}

    public sideType getSide()
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
    public orderType getOrderType() { return super.getOrderType(); }
    public String getId() { return super.getId(); }
    public int getSize() { return super.getOrderSize(); }



    public void setSide(sideType side)
    {
        this.side = side;
    }
    public void setPrice(double price)
    {
        this.price = price;
    }

    private sideType side = sideType.BUY;
    private double price = 0.0;

    public AddOrder(int timeStamp, char orderType, String orderId, sideType side, double price, short orderSize)
    {
        super.setTimeStamp(timeStamp);
        super.setOrderType(orderType);
        super.setId(orderId);
        super.setOrderSize(orderSize);
        this.side = side;
        this.price = price;
    }
}
