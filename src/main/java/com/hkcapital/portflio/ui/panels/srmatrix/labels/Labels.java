package com.hkcapital.portflio.ui.panels.srmatrix.labels;

public enum Labels
{
    Id("Id:"),
    SRMatrix("SRMatrix:"),
    Name("Name"),
    Support("Support:"),
    LSupportTolerance("L.Support Tolerance:"),
    RSupportTolerance("R.Support Tolerance:"),
    Resistance("Resistance:"),
    LResistanceTolerance("L.Resistance Tolerance:"),
    RResistanceTolerance("R.Resistance Tolerance:"),

    LSupportTolerancePercent("L.Support Tolerance %:"),
    RSupportTolerancePercent("R.Support Tolerance %:"),
    LResistanceTolerancePercent("L.Resistance Tolerance %:"),
    RResistanceTolerancePercent("R.Resistance Tolerance %:"),

    TakeProfitPercent("Take Profit %:"),
    StopLossPercent("Stop Loss %:"),

    TimeFrame("TimeFrame:"),
    TimeFrameUnit("TimeFrame Unit:"),

    Active("Active:"),
    SRMatrixPanel("SRMatrix Panel:");

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
