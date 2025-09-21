package com.hkcapital.portoflio.ui.panels.marketconditions.messages;

public enum MessagesLabel
{
    NoInstrumentSelected("NoItemSelected", "No Instrument Selected", "Please select an Instrument");


    private String id, title, message;

    MessagesLabel(String id, String title, String message)
    {
        this.id = id;
        this.title = title;
        this.message = message;
    }

    public String getId()
    {
        return id;
    }

    public String getTitle()
    {
        return title;
    }

    public String getMessage()
    {
        return message;
    }
}
