package com.pricer.businesslogic;

//import com.pricer.interfaces.*;
import com.pricer.interfaces.IAction;
import com.pricer.interfaces.IOrderType;
import com.pricer.interfaces.ISide;

/**
 An order to increase the number of shares to buy for this stock.
 */
public class AddOrder extends Order implements IOrderType, ISide, IAction
{
    //declarations & initial values.
    private side thisSide = null;
    private double price = 0.0;
    private action thisAction = null;
    private orderType thisOrderType = orderType;

    /**
     Determines if this is a Buy or Sell order.

     @return An enumeration to determine if this is a Buy or Sell Order.
     */
    public side getSide()
    {
        return thisSide;
    }

    /**
     Determines the price of this specific order.

     @return The price of this stock.
     */
    public double getPrice() { return price; }

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
    public orderType getOrderType()
    {
        return thisOrderType;
    }

    /**
     Returns a unique identifier, specific to this order.

     @return A unique identifier, specific to this order.
     */
    public String getId()
    {
        return super.id;
    }

    /**
     Returns the number of shares for this order.

     @return The number of shares for this order.
     */
    public int getSize()
    {
        return super.orderSize;
    }

    /**
     Returns if this order is intended to Buy or Sell stock.

     @return An enumeration, indicating if this is a Buy or Sell order.
     */
    public action getAction()
    {
       return thisAction;
    }


    /**
     Creates an Add order. Removes the number of shares bought for this stock.

     @param timeStamp the time, in seconds, when this order was placed.
     @param addOrderType The type of order this is. An Add order increases the number of shares of this stock.
     @param orderId Unique id to identify this Add order.
     @param orderSize Number of shares to add to the order.
     @return AddOrder
     @see Order
     */
    public AddOrder(int timeStamp, orderType addOrderType, String orderId, char side, double price, int orderSize)
    {
        super.timeStamp = timeStamp;
        super.id = orderId;
        super.orderSize = orderSize;
        this.price = price;
        super.orderType = addOrderType;

        switch (side)
        {
            case 'B':
            {
                thisSide = ISide.side.BUY;
                thisAction = IAction.action.SELL;
                break;
            }
            case 'S':
            {
                thisSide = ISide.side.SELL;
                thisAction = IAction.action.BUY;
                break;
            }
            default:
            {
                /* TODO: throw an exception. */
                break;
            }
        }

    }

    /**
     Remove a specific number of shares from this order.

     @param sharesToRemove Number of shares to remove from this order.
     */
    public void ReduceShares(int sharesToRemove)
    {
        this.orderSize -= sharesToRemove;
    }
}
