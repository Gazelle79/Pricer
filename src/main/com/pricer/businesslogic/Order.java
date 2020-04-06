package main.com.pricer.businesslogic;


public abstract class Order
{
    public enum orderType {ADD, REDUCE}

    public void setTimeStamp(int timeStamp)
    {
        this.timeStamp = timeStamp;
    }

    public void setOrderType(char thisOrderType)
    {
        if (thisOrderType == 'A')
        {
            this.thisOrderType = orderType.ADD;
        }
        else if(thisOrderType == 'R')
        {
            this.thisOrderType = orderType.REDUCE;
        }
        else
        {}
    }

    public void setId(String id)
    {
        this.id = id;
    }

    public void setOrderSize(short orderSize)
    {
        this.orderSize = orderSize;
    }

    public int getTimeStamp()
    {
        return timeStamp;
    }

    public orderType getOrderType()
    {
        return thisOrderType;
    }

    public String getId()
    {
        return id;
    }

    public short getOrderSize()
    {
        return orderSize;
    }

    private int timeStamp = 0;
    private orderType thisOrderType = orderType.ADD;
    private String id = "123";
    private short orderSize = 0;

}
