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

        public char sideValue()
        {
            return this.side;
        }
    }

    public enum action
    {
        BUY('B'),
        SELL('S');

        private char action;

        action(char actionCode)
        {
            this.action = actionCode;
        }

        public char actionValue()
        {
            return this.action;
        }
    }

    private side thisSide = side.BUY;
    private double price = 0.0;
    private action thisAction = action.BUY;

    public side getSide()
    {
        return thisSide;
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

    public action getAction()
    {
       return thisAction;
    }



    public AddOrder(int timeStamp, char orderType, String orderId, char side, double price, int orderSize)
    {
        super.timeStamp = timeStamp;
        super.orderType  = orderType;
        super.id = orderId;
        super.orderSize = orderSize;
        this.price = price;

        switch (side)
        {
            case 'B':
            {
                thisSide = AddOrder.side.BUY;
                thisAction = AddOrder.action.SELL;
                break;
            }
            case 'S':
            {
                this.thisSide = AddOrder.side.SELL;
                thisAction = AddOrder.action.BUY;
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
