package com.pricer.businesslogic;

import com.pricer.interfaces.IOrderType;

/**
 Parent class. Meant to be extended for an Add order, Reduce order, or other order.
 */
public abstract class Order implements IOrderType {

    protected int timeStamp = 0;
    protected orderType orderType = IOrderType.orderType.ADD;
    protected String id = "123";
    protected int orderSize = 0;


}
