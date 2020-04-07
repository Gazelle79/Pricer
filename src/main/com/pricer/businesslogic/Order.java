package main.com.pricer.businesslogic;


public abstract class Order
{
    //public enum orderType {ADD, REDUCE}

    private int timeStamp = 0;
    private char orderType = 'A';
    private String id = "123";
    private int orderSize = 0;

    public void setTimeStamp(int timeStamp)
    {
        this.timeStamp = timeStamp;
    }

    public void setOrderType(char orderType)
    {
        this.orderType = orderType;
    }

    public void setId(String id)
    {
        this.id = id;
    }

    public void setOrderSize(int orderSize)
    {
        this.orderSize = orderSize;
    }

    public int getTimeStamp()
    {
        return timeStamp;
    }

    public char getOrderType()
    {
        return orderType;
    }

    public String getId()
    {
        return id;
    }

    public int getOrderSize()
    {
        return orderSize;
    }

}
