package com.pricer.interfaces;

/**
 Indicates if this order is a Buy or Sell order.
 */
public interface ISide {

    enum side implements ISide
    {
        BUY('B'),
        SELL('S');

        private char side;

        /**
         Constructor. Determines if this is a Buy ('B') or Sell ('S') order, based on the line of financial data.
         */
        side(char sideCode)
        {
            this.side = sideCode;
        }

        /**
         Reads whether this is a Buy ('B') or Sell ('S') order, based on the line of financial data.
         */
        public char sideValue()
        {
            return this.side;
        }
    }
}
