package main.com.pricer.businesslogic;

import java.util.Comparator;


class PriceCompare implements Comparator<AddOrder>
{
        public int compare(AddOrder orderA, AddOrder orderB)
        {
            if (orderA.getPrice() < orderB.getPrice()) return 1;
            if (orderA.getPrice() > orderB.getPrice()) return -1;
            else return 0;
        }
}

