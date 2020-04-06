package main.com.pricer.businesslogic;

public class ReduceOrder extends Order
{
    public int getTimeStamp()
    {
        return super.getTimeStamp();
    }
    public orderType getOrderType() { return super.getOrderType(); }
    public String getId() { return super.getId(); }
    public int getSize() { return super.getOrderSize(); }


    public ReduceOrder(int timeStamp, char orderType, String orderId, short orderSize)
    {
        super.setTimeStamp(timeStamp);
        super.setOrderType(orderType);
        super.setId(orderId);
        super.setOrderSize(orderSize);
    }

}
