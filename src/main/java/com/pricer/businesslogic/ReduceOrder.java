package com.pricer.businesslogic;

/**
 An order to reduce the number of shares to buy for this stock.
 */
public class ReduceOrder extends Order
{
    /**
     Determines the time this order was placed, in seconds.

     @return The timestamp this order was placed, in seconds.
     */
    public int getTimeStamp()
    {
        return super.timeStamp;
    }

    /**
     Determines whether this is an Add or Reduce order.

     @return An enumeration, indicating if this is an Add or Reduce order.
     */
    public orderType getOrderType() { return super.orderType; }

    /**
     Returns a unique identifier, specific to this order.

     @return A unique identifier, specific to this order.
     */
    public String getId() { return super.id; }

    /**
     Returns the number of shares for this order.

     @return The number of shares for this order.
     */
    public int getSize() { return super.orderSize; }


    /**
     Creates a Reduce order. Removes the number of shares bought for this stock.

     @param timeStamp the time, in seconds, when this order was placed.
     @param reduceOrderType The type of order this is. A Reduce order reduces the number of shares of this stock.
     @param orderId Unique id to identify this Reduce order.
     @param orderSize Number of shares to subtract from the order.
     @return ReduceOrder
     @see Order
     */
    public ReduceOrder(int timeStamp, orderType reduceOrderType, String orderId, short orderSize)
    {
        super.timeStamp  = timeStamp;
        super.orderType = reduceOrderType;
        super.id = orderId;
        super.orderSize = orderSize;
    }

}
