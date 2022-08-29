package main.java.com.pricer.interfaces;

public interface ISide {

    enum side implements ISide
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
}
