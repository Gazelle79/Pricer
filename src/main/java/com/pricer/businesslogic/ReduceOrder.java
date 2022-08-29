package com.pricer.businesslogic;

public class ReduceOrder extends Order
{
    public int getTimeStamp()
    {
        return super.timeStamp;
    }
    public orderType getOrderType() { return super.orderType; }
    public String getId() { return super.id; }
    public int getSize() { return super.orderSize; }



    public ReduceOrder(int timeStamp, orderType reduceOrderType, String orderId, short orderSize)
    {
        super.timeStamp  = timeStamp;
        super.orderType = reduceOrderType;
        super.id = orderId;
        super.orderSize = orderSize;
    }

}
