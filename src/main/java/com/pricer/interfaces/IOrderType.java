package com.pricer.interfaces;

/**
 Indicates if this order is an Add or Reduce order.
 */
public interface IOrderType
{
    enum orderType
    {
        ADD('A'),
        REDUCE('R');

        protected char orderType = 'A';

        /**
         Constructor. Determines if this is an Add ('A') or Reduce ('R') order, based on the line of financial data.
         */
        orderType(char orderTypeCode)
        {
            this.orderType = orderTypeCode;
        }

        /**
         Reads whether this is an Add ('A') or Reduce ('R') order, based on the line of financial data.
         */
        public char orderTypeValue()
        {
            return this.orderType;
        }

    }

}
