package com.hkcapital.portoflio.ui.panels.configuartion;

import com.hkcapital.portoflio.model.Configuration;
import com.hkcapital.portoflio.service.ConfigurationService;
import com.hkcapital.portoflio.ui.fields.NumberTextField;

import javax.swing.*;
import java.awt.*;

public class ConfigurationPanel extends JPanel
{

    private final ConfigurationService configurationService;

    private final JLabel percentAllocationAllowedLabel = new JLabel("% Allocation Allowed");
    private final NumberTextField percentAllocationAllowed = new NumberTextField(10); // 20 columns looks better


    private final JLabel noOfInstrumentsLabel = new JLabel("No Of Instruments");
    private final NumberTextField noOfInstrument = new NumberTextField(10); // 20 columns looks better

    private final JLabel noOfPositionsPerInstrumentLabel = new JLabel("No Of Position per instrument");
    private final NumberTextField noOfPositionsPerInstrument = new NumberTextField(10); // 20 columns looks better

    private final JLabel maxPercentAllowedPerInstrumentLabel = new JLabel("Max percent allowed per position");
    private final NumberTextField maxPercentAllowedPerInstrument = new NumberTextField(10);


    private final JLabel levLabel = new JLabel("Leverage");
    private final NumberTextField lev = new NumberTextField(10);

    private final JTable configurationTable;
    private final ConfiguarionTableModel tableModel;
    private final JButton saveButton = new JButton("Save");
    private final JButton cancelButton = new JButton("Cancel");
    private final JButton closeButton = new JButton("Close");
    private final JButton removeButton = new JButton("Remove");

    public ConfigurationPanel(final ConfigurationService configurationService)
    {
        this.configurationService = configurationService;

        tableModel = new ConfiguarionTableModel<Configuration>(configurationService.findAll());

        setLayout(new GridBagLayout());
        setBorder(BorderFactory.createTitledBorder("Instrument Panel"));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8); // spacing around components
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Row 0: Text field
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.weightx = 1.0; // text field expands horizontally

        JPanel configurationPanel = new JPanel(new GridLayout(2, 2));
        configurationPanel.add(percentAllocationAllowedLabel);
        configurationPanel.add(percentAllocationAllowed);

        configurationPanel.add(noOfInstrumentsLabel);
        configurationPanel.add(noOfInstrument);

        configurationPanel.add(noOfPositionsPerInstrumentLabel);
        configurationPanel.add(noOfPositionsPerInstrument);


        configurationPanel.add(maxPercentAllowedPerInstrumentLabel);
        configurationPanel.add(maxPercentAllowedPerInstrument);

        configurationPanel.add(levLabel);
        configurationPanel.add(lev);

        add(configurationPanel);

        // Row 1: Buttons (in a sub-panel for better alignment)
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        buttonPanel.add(saveButton);
        buttonPanel.add(removeButton);
        buttonPanel.add(cancelButton);
        buttonPanel.add(closeButton);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        gbc.weightx = 0;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.EAST;
        add(buttonPanel, gbc);


        configurationTable = new JTable(tableModel);

        JScrollPane scrollPane = new JScrollPane(configurationTable);

        add(scrollPane, gbc);

    }

    public JButton getSaveButton()
    {
        return saveButton;
    }

    public JButton getCancelButton()
    {
        return cancelButton;
    }

    public JButton getCloseButton()
    {
        return closeButton;
    }

    public void save()
    {


        Configuration configuration = new Configuration(percentAllocationAllowed.getDoubleValue(),
                noOfInstrument.getIntValue(), noOfPositionsPerInstrument.getIntValue(),
                maxPercentAllowedPerInstrument.getDoubleValue(), lev.getIntValue());
        configurationService.addConfiguration(configuration);
        percentAllocationAllowed.setText(null);
        noOfInstrument.setText(null);
        noOfPositionsPerInstrument.setText(null);
        maxPercentAllowedPerInstrument.setText(null);
        lev.setText(null);
        tableModel.addRow(configuration);
        configurationService.addConfiguration(configuration);

    }

    public void remove()
    {
        Configuration configuration = (Configuration) tableModel.removeRow(configurationTable.getSelectedRow());
        configurationService.removeConfiguration(configuration);
    }

    public void clear()
    {
        percentAllocationAllowed.setText(null);
    }

    public JButton getRemoveButton()
    {
        return removeButton;
    }
}
