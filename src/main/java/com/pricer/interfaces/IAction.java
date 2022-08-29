package com.pricer.interfaces;

public interface IAction
{
    enum action implements IAction
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
}
