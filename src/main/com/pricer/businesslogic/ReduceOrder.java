package main.com.pricer.businesslogic;

public class ReduceOrder extends Order
{
    public int getTimeStamp()
    {
        return super.timeStamp;
    }
    public char getOrderType() { return super.orderType; }
    public String getId() { return super.id; }
    public int getSize() { return super.orderSize; }



    public ReduceOrder(int timeStamp, char orderType, String orderId, short orderSize)
    {
        super.timeStamp  = timeStamp;
        super.orderType = orderType;
        super.id = orderId;
        super.orderSize = orderSize;
    }

}
