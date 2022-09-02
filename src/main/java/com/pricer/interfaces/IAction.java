package com.pricer.interfaces;

/**
 Indicates if this order is supposed to Buy or Sell this order of the stock.
 */
public interface IAction
{
    enum action implements IAction
    {
        BUY('B'),
        SELL('S');

        private char action;

        /**
         Constructor. Determines if we should Buy('B') or Sell('S') this stock, based on the line of financial data.
         */
        action(char actionCode)
        {
            this.action = actionCode;
        }

        /**
         Reads whether to buy or sell this stock, based on the line of financial data.
         */
        public char actionValue()
        {
            return this.action;
        }
    }
}
