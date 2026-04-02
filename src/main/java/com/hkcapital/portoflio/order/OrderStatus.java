package com.hkcapital.portoflio.order;

public enum OrderStatus
{
    SENT("SENT"),

    CLOSED("CLOSED");

    private String orderStatus;

    OrderStatus(String orderType)
    {
        this.orderStatus = orderType;
    }

    public String getOrderStatus()
    {
        return orderStatus;
    }
}
