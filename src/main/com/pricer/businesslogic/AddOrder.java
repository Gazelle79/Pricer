package main.com.pricer.businesslogic;

import main.com.pricer.interfaces.*;


public class AddOrder extends Order implements IOrderType, ISide, IAction
{
    //declarations & initial values.
    private side thisSide = null;
    private double price = 0.0;
    private action thisAction = null;
    private orderType thisOrderType = orderType;


    public side getSide()
    {
        return thisSide;
    }

    public double getPrice() { return price; }

    public int getTimeStamp()
    {
        return super.timeStamp;
    }

    public orderType getOrderType()
    {
        return thisOrderType;
    }

    public String getId()
    {
        return super.id;
    }

    public int getSize()
    {
        return super.orderSize;
    }

    public action getAction()
    {
       return thisAction;
    }



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

    public void ReduceShares(int sharesToRemove)
    {
        this.orderSize -= sharesToRemove;
    }
}
