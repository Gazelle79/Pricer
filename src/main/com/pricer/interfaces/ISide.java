package main.com.pricer.interfaces;

public interface ISide {

    public enum side implements ISide {
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
