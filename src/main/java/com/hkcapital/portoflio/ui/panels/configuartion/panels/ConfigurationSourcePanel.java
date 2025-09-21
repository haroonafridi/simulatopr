package com.hkcapital.portoflio.ui.panels.configuartion.panels;

import com.hkcapital.portoflio.model.Configuration;
import com.hkcapital.portoflio.service.ConfigurationService;
import com.hkcapital.portoflio.ui.UIBag;
import com.hkcapital.portoflio.ui.fields.NumberTextField;
import com.hkcapital.portoflio.ui.panels.configuartion.labels.Labels;

import javax.swing.*;

public class ConfigurationSourcePanel extends UIBag
{

    private final ConfigurationService configurationService;
    private final JLabel idLabel = new JLabel((Labels.Id.getLabel()));
    private final NumberTextField id = new NumberTextField(60);
    private final JLabel percentAllocationAllowedLabel = new JLabel(Labels.PercentAllowedAllocation.getLabel());
    private final NumberTextField percentAllocationAllowed = new NumberTextField(60);

    private final JLabel noOfInstrumentsLabel = new JLabel(Labels.NoOfInstruments.getLabel());
    private final NumberTextField noOfInstrument = new NumberTextField(60);

    private final JLabel noOfPositionsPerInstrumentLabel = new JLabel(Labels.NoofPositionsPerinstruments.getLabel());
    private final NumberTextField noOfPositionsPerInstrument = new NumberTextField(60);

    private final JLabel maxPercentAllowedPerInstrumentLabel = new JLabel(Labels.MaxPercentAllowedPerPosition.getLabel());
    private final NumberTextField maxPercentAllowedPerInstrument = new NumberTextField(60);

    private final JLabel levLabel = new JLabel(Labels.Lev.getLabel());
    private final NumberTextField lev = new NumberTextField(60);


    public ConfigurationSourcePanel(ConfigurationService configurationService)
    {
        super(ConfigurationSourcePanel.class);
        this.configurationService = configurationService;
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

    public Configuration getConfiguration()
    {

        if(id != null && id.getText() != "" && id.intValue() != null)
        {
            return configurationService.findById(id.intValue());
        }

        return null;
    }
}
