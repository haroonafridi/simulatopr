package com.hkcapital.portoflio.ui.panels.configuartion;

import com.hkcapital.portoflio.model.Configuration;
import com.hkcapital.portoflio.service.ConfigurationService;
import com.hkcapital.portoflio.ui.fields.NumberTextField;
import com.hkcapital.portoflio.ui.panels.marketconditions.MarketConditionsSourcePanel;

import javax.swing.*;
import java.awt.*;

public class ConfigurationPanel extends JPanel
{

    private final ConfigurationService configurationService;
    private final ConfigurationSourcePanel configurationSourcePanel;
    private final JLabel percentAllocationAllowedLabel = new JLabel("% Allocation Allowed");
    private final NumberTextField percentAllocationAllowed = new NumberTextField(30);

    private final JLabel noOfInstrumentsLabel = new JLabel("No Of Instruments");
    private final NumberTextField noOfInstrument = new NumberTextField(30);

    private final JLabel noOfPositionsPerInstrumentLabel = new JLabel("No Of Position per instrument");
    private final NumberTextField noOfPositionsPerInstrument = new NumberTextField(30);

    private final JLabel maxPercentAllowedPerInstrumentLabel = new JLabel("Max percent allowed per position");
    private final NumberTextField maxPercentAllowedPerInstrument = new NumberTextField(30);

    private final JLabel levLabel = new JLabel("Leverage");
    private final NumberTextField lev = new NumberTextField(30);

    private final JTable configurationTable;
    private final ConfiguarionTableModel tableModel;

    private final JButton saveButton = new JButton("Save");
    private final JButton cancelButton = new JButton("Cancel");
    private final JButton closeButton = new JButton("Close");
    private final JButton removeButton = new JButton("Remove");

    private final JButton selectButton = new JButton("Select");

    public ConfigurationPanel(final ConfigurationService configurationService,
                              final ConfigurationSourcePanel configurationSourcePanel) {
        this.configurationService = configurationService;
        this.configurationSourcePanel = configurationSourcePanel;

        tableModel = new ConfiguarionTableModel<Configuration>(configurationService.findAll());

        setLayout(new GridBagLayout());
        setBorder(BorderFactory.createTitledBorder("Instrument Panel"));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Configuration Fields Panel
        JPanel configurationPanel = new JPanel(new GridBagLayout());
        GridBagConstraints innerGbc = new GridBagConstraints();
        innerGbc.insets = new Insets(4, 4, 4, 4);
        innerGbc.anchor = GridBagConstraints.WEST;
        innerGbc.fill = GridBagConstraints.HORIZONTAL;
        innerGbc.gridx = 0;
        innerGbc.gridy = 0;

        configurationPanel.add(percentAllocationAllowedLabel, innerGbc);
        innerGbc.gridx = 1;
        configurationPanel.add(percentAllocationAllowed, innerGbc);

        innerGbc.gridx = 0;
        innerGbc.gridy++;
        configurationPanel.add(noOfInstrumentsLabel, innerGbc);
        innerGbc.gridx = 1;
        configurationPanel.add(noOfInstrument, innerGbc);

        innerGbc.gridx = 0;
        innerGbc.gridy++;
        configurationPanel.add(noOfPositionsPerInstrumentLabel, innerGbc);
        innerGbc.gridx = 1;
        configurationPanel.add(noOfPositionsPerInstrument, innerGbc);

        innerGbc.gridx = 0;
        innerGbc.gridy++;
        configurationPanel.add(maxPercentAllowedPerInstrumentLabel, innerGbc);
        innerGbc.gridx = 1;
        configurationPanel.add(maxPercentAllowedPerInstrument, innerGbc);

        innerGbc.gridx = 0;
        innerGbc.gridy++;
        configurationPanel.add(levLabel, innerGbc);
        innerGbc.gridx = 1;
        configurationPanel.add(lev, innerGbc);

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.weightx = 1.0;
        add(configurationPanel, gbc);

        // Buttons Panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        buttonPanel.add(saveButton);
        buttonPanel.add(removeButton);
        buttonPanel.add(cancelButton);
        buttonPanel.add(closeButton);
        buttonPanel.add(selectButton);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        gbc.weightx = 0;
        gbc.anchor = GridBagConstraints.EAST;
        add(buttonPanel, gbc);

        // Table
        configurationTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(configurationTable);

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        add(scrollPane, gbc);

        saveButton.addActionListener(e -> save());
        cancelButton.addActionListener(e -> remove());
        closeButton.addActionListener(e -> clear());
        removeButton.addActionListener(e -> getGraphics().dispose());
        selectButton.addActionListener(e-> select());
    }

    public JButton getSaveButton() { return saveButton; }
    public JButton getCancelButton() { return cancelButton; }
    public JButton getCloseButton() { return closeButton; }
    public JButton getRemoveButton() { return removeButton; }

    public void save() {
        Configuration configuration = new Configuration(
                percentAllocationAllowed.getDoubleValue(),
                noOfInstrument.getIntValue(),
                noOfPositionsPerInstrument.getIntValue(),
                maxPercentAllowedPerInstrument.getDoubleValue(),
                lev.getIntValue()
        );

        configurationService.addConfiguration(configuration);

        percentAllocationAllowed.setText(null);
        noOfInstrument.setText(null);
        noOfPositionsPerInstrument.setText(null);
        maxPercentAllowedPerInstrument.setText(null);
        lev.setText(null);

        tableModel.addRow(configuration);
    }

    public void remove() {
        Configuration configuration = (Configuration) tableModel.removeRow(configurationTable.getSelectedRow());
        configurationService.removeConfiguration(configuration);
    }

    public void clear() {
        percentAllocationAllowed.setText(null);
        noOfInstrument.setText(null);
        noOfPositionsPerInstrument.setText(null);
        maxPercentAllowedPerInstrument.setText(null);
        lev.setText(null);
        SwingUtilities.getWindowAncestor(this).dispose();
    }


    public void select() {
        Configuration configuration = (Configuration) tableModel.getElements().get(configurationTable.getSelectedRow());
        configurationSourcePanel.getId().setText(configuration.getId().toString());
        configurationSourcePanel.getLev().setText(configuration.getLev().toString());
        configurationSourcePanel.getPercentAllocationAllowed().setText(configuration.getPercentAllocationAllowed().toString());
        configurationSourcePanel.getNoOfInstrument().setText(configuration.getNoOfInsutrments().toString());
        configurationSourcePanel.getNoOfPositionsPerInstrument().setText(configuration.getNoOfPositionsPerInstruments().toString());
        configurationSourcePanel.getMaxPercentAllowedPerInstrument().setText(configuration.getMaxPercentAllowedPerInstrument().toString());
        SwingUtilities.getWindowAncestor(this).dispose();
    }

    @Override
    protected void paintComponent(Graphics g)
    {
        super.paintComponent(g); // always call super
    }
}
