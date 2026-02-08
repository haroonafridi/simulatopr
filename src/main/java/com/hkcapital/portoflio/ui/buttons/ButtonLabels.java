package com.hkcapital.portoflio.ui.buttons;

public enum ButtonLabels
{
    Save("Save"),
    Remove("Remove"),

    Cancel("Cancel"),

    Close("Close"),

    Add("Add"),

    Select("Select"),

    RemoveAll("Remove All"),

    Refresh("Read");

    private String label;

    ButtonLabels(String label)
    {
        this.label = label;
    }

    public String getLabel()
    {
        return label;
    }
}
