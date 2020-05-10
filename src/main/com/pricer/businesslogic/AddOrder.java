package main.com.pricer.businesslogic;
import java.util.ArrayList;
import java.util.HashMap;


public class AddOrder extends Order
{
    //public enum side {BUY, SELL}

    private char side = 'B';
    private double price = 0.0;
    private char action = 'B';

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
        return super.timeStamp;
    }

    public char getOrderType()
    {
        return super.orderType;
    }

    public String getId()
    {
        return super.id;
    }

    public int getSize()
    {
        return super.orderSize;
    }

    public char getAction()
    {
       return action;
    }



    public AddOrder(int timeStamp, char orderType, String orderId, char side, double price, int orderSize)
    {
        super.timeStamp = timeStamp;
        super.orderType  = orderType;
        super.id = orderId;
        super.orderSize = orderSize;
        this.side = side;
        this.price = price;

        if( this.side == 'B')
        {
            this.action = 'S';
        }
        else if(this.side == 'S')
        {
            this.action = 'B';
        }
        else
        { /* TODO: throw an exception. */  }

    }

    public void ReduceShares(int sharesToRemove)
    {
        this.orderSize -= sharesToRemove;
    }
}
