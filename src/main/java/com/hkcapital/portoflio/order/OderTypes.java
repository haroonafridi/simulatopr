package com.hkcapital.portoflio.order;

public enum OderTypes
{
    MANUAL("MANUAL"),

    AUTO("AUTO");

    private String orderType;

    OderTypes(String orderType)
    {
        this.orderType = orderType;
    }

    public String getOrderType()
    {
        return orderType;
    }
}
