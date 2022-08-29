package com.pricer.interfaces;

public interface IOrderType
{
    enum orderType
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

    }

}
