package com.hkcapital.portoflio.ui.panels.srmatrix.labels;

public enum Labels
{
    Id("Id"),
    SRMatrix("SRMatrix"),
    Name("Name"),
    SRMatrixPanel("SRMatrix Panel");

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
