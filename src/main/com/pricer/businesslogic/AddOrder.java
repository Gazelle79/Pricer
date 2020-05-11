package main.com.pricer.businesslogic;

public class AddOrder extends Order
{
    public enum side
    {
        BUY('B'),
        SELL('S');

        private char side;

        side(char sideCode)
        {
            this.side = sideCode;
        }
    }

    //private char side = 'B';
    private double price = 0.0;
    private char action = 'B';

    public side getSide()
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
        this.price = price;

        //Assign the 'side' char to an enumeration value here.
        // I don't know how to do it.



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
