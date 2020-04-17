package main.com.pricer.businesslogic;
//import java.util.Comparator;

public class AddOrder extends Order implements Comparable<AddOrder>
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

    // override equals and hashCode
    @Override
    public int compareTo(AddOrder addOrder)
    {
        //return (this.price - addOrder.price);
        if(this.price > addOrder.price) return -1;
        if(this.price < addOrder.price) return 1;
        else return 0;
    }


}
