package com.hkcapital.portoflio.order;

public enum OrderTypes
{
    MANUAL("MANUAL"),

    AUTO("AUTO");

    private String orderType;

    OrderTypes(String orderType)
    {
        this.orderType = orderType;
    }

    public String getOrderType()
    {
        return orderType;
    }
}
