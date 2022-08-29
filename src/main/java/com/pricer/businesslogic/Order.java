package com.pricer.businesslogic;

import main.java.com.pricer.interfaces.IOrderType;

public abstract class Order implements IOrderType {

    protected int timeStamp = 0;
    protected orderType orderType = IOrderType.orderType.ADD;
    protected String id = "123";
    protected int orderSize = 0;


}
