package main.com.pricer.businesslogic;

import main.com.pricer.interfaces.*;

public abstract class Order implements IOrderType {

    /*
    protected enum orderType
    {
        ADD('A'),
        REDUCE('R');

        protected char orderType = 'A';

        orderType(char orderTypeCode)
        {
            this.orderType = orderTypeCode;
        }

        public char orderTypeValue()
        {
            return this.orderType;
        }

    }*/

    protected int timeStamp = 0;
    //protected char orderType = 'A';
    protected orderType orderType = IOrderType.orderType.ADD;
    protected String id = "123";
    protected int orderSize = 0;


}
