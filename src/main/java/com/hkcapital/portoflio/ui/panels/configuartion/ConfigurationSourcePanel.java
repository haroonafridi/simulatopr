package com.hkcapital.portoflio.ui.panels.configuartion;

import com.hkcapital.portoflio.model.Configuration;
import com.hkcapital.portoflio.service.ConfigurationService;
import com.hkcapital.portoflio.ui.fields.NumberTextField;
import com.hkcapital.portoflio.ui.panels.marketconditions.MarketConditionsSourcePanel;

import javax.swing.*;
import java.awt.*;

public class ConfigurationSourcePanel extends JPanel
{

    private final JLabel idLabel = new JLabel("Id");
    private final NumberTextField id = new NumberTextField(30);
    private final JLabel percentAllocationAllowedLabel = new JLabel("% Allocation Allowed");
    private final NumberTextField percentAllocationAllowed = new NumberTextField(30);

    private final JLabel noOfInstrumentsLabel = new JLabel("No Of Instruments");
    private final NumberTextField noOfInstrument = new NumberTextField(30);

    private final JLabel noOfPositionsPerInstrumentLabel = new JLabel("No Of Position per instrument");
    private final NumberTextField noOfPositionsPerInstrument = new NumberTextField(30);

    private final JLabel maxPercentAllowedPerInstrumentLabel = new JLabel("Max percent allowed per instrument");
    private final NumberTextField maxPercentAllowedPerInstrument = new NumberTextField(30);

    private final JLabel levLabel = new JLabel("Leverage");
    private final NumberTextField lev = new NumberTextField(30);


    public ConfigurationSourcePanel()
    {
        add(idLabel);
        add(id);
        id.setEnabled(false);
        add(percentAllocationAllowed);
        add(percentAllocationAllowedLabel);
        add(percentAllocationAllowed);
        percentAllocationAllowed.setEnabled(false);
        add(noOfInstrumentsLabel);
        add(noOfInstrument);
        noOfInstrument.setEnabled(false);
        add(noOfPositionsPerInstrumentLabel);
        add(noOfPositionsPerInstrument);
        noOfPositionsPerInstrument.setEnabled(false);
        add(maxPercentAllowedPerInstrumentLabel);
        add(maxPercentAllowedPerInstrument);
        maxPercentAllowedPerInstrument.setEnabled(false);
        add(levLabel);
        add(lev);
        lev.setEnabled(false);
    }

    public NumberTextField getPercentAllocationAllowed()
    {
        return percentAllocationAllowed;
    }

    public NumberTextField getNoOfInstrument()
    {
        return noOfInstrument;
    }

    public NumberTextField getNoOfPositionsPerInstrument()
    {
        return noOfPositionsPerInstrument;
    }

    public NumberTextField getMaxPercentAllowedPerInstrument()
    {
        return maxPercentAllowedPerInstrument;
    }

    public NumberTextField getLev()
    {
        return lev;
    }

    public NumberTextField getId()
    {
        return id;
    }
}
