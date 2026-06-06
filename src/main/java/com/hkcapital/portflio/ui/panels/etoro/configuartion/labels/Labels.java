package com.hkcapital.portflio.ui.panels.etoro.configuartion.labels;

public enum Labels
{
    OrdersTitle("Etoro Orders"),
    Id("Etoro Order Id"),
    Type("OrderType"),
    Status("Status"),
    Instrument("Instrument"),
    TimeFrame("Timeframe"),
    TimeFrameUnit("Timeframe Unit"),
    Info("Order Info"),
    Amount("Amount");

    private String label;

    Labels(String label)
    {
        this.label = label;
    }

    public String getLabel()
    {
        return label;
    }
}
