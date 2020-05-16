package main.com.pricer.businesslogic;

import main.com.pricer.interfaces.*;

public abstract class Order implements IOrderType {

    protected int timeStamp = 0;
    protected orderType orderType = IOrderType.orderType.ADD;
    protected String id = "123";
    protected int orderSize = 0;


}
