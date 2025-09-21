package com.hkcapital.portoflio.ui.panels.tradingsessions;

import com.hkcapital.portoflio.model.Configuration;
import com.hkcapital.portoflio.model.TradingSessions;
import com.hkcapital.portoflio.repository.ServiceRegistery;
import com.hkcapital.portoflio.service.ConfigurationService;
import com.hkcapital.portoflio.service.Service;
import com.hkcapital.portoflio.service.TradingSessionsService;
import com.hkcapital.portoflio.ui.UIBag;
import com.hkcapital.portoflio.ui.buttons.ButtonLabels;

import javax.swing.*;
import java.awt.*;

public class TradingSessionPanel extends UIBag
{

    private final ServiceRegistery<Service> serviceRegistery;
    private final TradingSessionsService tradingSessionsService;
    private final JLabel tradingSessionNameLabel = new JLabel("Trading Session Name:");
    private final JTextField tradingSessionName = new JTextField(10);

    private final JLabel tradingSessionDescriptionLabel = new JLabel("Trading Session Description.");
    private final JTextField tradingSessionDescription = new JTextField(20);

    private final JLabel startTimeLabel = new JLabel("Start Time:");
    private final JTextField startTime = new JTextField(5);

    private final JLabel endTimeLabel = new JLabel("End Time:");
    private final JTextField endTime = new JTextField(5);

    private final JTable tradingSessionTable;
    private final TradingSessionTableModel tableModel;

    private final JButton saveButton = new JButton(ButtonLabels.Save.getLabel());
    private final JButton cancelButton = new JButton(ButtonLabels.Cancel.getLabel());
    private final JButton closeButton = new JButton(ButtonLabels.Close.getLabel());
    private final JButton removeButton = new JButton(ButtonLabels.Remove.getLabel());

    //private final JButton selectButton = new JButton("Select");

    public TradingSessionPanel(final ServiceRegistery<Service> serviceRegistery)
    {
        super(TradingSessionPanel.class);
        this.serviceRegistery = serviceRegistery;
        this.tradingSessionsService = (TradingSessionsService) this.serviceRegistery.getService(Service.TradingSessionsService);

        tableModel = new TradingSessionTableModel<TradingSessions>(tradingSessionsService.findAll());

        setLayout(new GridBagLayout());
        setBorder(BorderFactory.createTitledBorder("Trading Session Panel"));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Configuration Fields Panel
        JPanel tradingSessionPanel = new JPanel(new GridBagLayout());
        GridBagConstraints innerGbc = new GridBagConstraints();
        innerGbc.insets = new Insets(4, 4, 4, 4);
        innerGbc.anchor = GridBagConstraints.WEST;
        innerGbc.fill = GridBagConstraints.HORIZONTAL;
        innerGbc.gridx = 0;
        innerGbc.gridy = 0;

        tradingSessionPanel.add(tradingSessionNameLabel, innerGbc);
        innerGbc.gridx = 1;
        tradingSessionPanel.add(tradingSessionName, innerGbc);
        tradingSessionPanel.add(tradingSessionDescriptionLabel);
        tradingSessionPanel.add(tradingSessionDescription);

        tradingSessionPanel.add(startTimeLabel);
        tradingSessionPanel.add(startTime);

        tradingSessionPanel.add(endTimeLabel);
        tradingSessionPanel.add(endTime);

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.weightx = 1.0;

        add(tradingSessionPanel, gbc);

        // Buttons Panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        buttonPanel.add(saveButton);
        buttonPanel.add(removeButton);
        buttonPanel.add(cancelButton);
        buttonPanel.add(closeButton);
      //  buttonPanel.add(selectButton);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        gbc.weightx = 0;
        gbc.anchor = GridBagConstraints.EAST;
        add(buttonPanel, gbc);

        // Table
        tradingSessionTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(tradingSessionTable);

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
        removeButton.addActionListener(e -> remove());
       // selectButton.addActionListener(e -> select());
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

    public JButton getRemoveButton()
    {
        return removeButton;
    }

    public void save()
    {
        TradingSessions tradingSession = new TradingSessions(
                tradingSessionName.getText(),
                tradingSessionDescription.getText(),
                startTime.getText(),
                endTime.getText()
        );

        tradingSessionsService.add(tradingSession);
        tradingSessionName.setText(null);
        tradingSessionDescription.setText(null);
        startTime.setText(null);
        endTime.setText(null);
        tableModel.addRow(tradingSession);
    }

    public void remove()
    {
        TradingSessions tradingSessions = (TradingSessions) tableModel.removeRow(tradingSessionTable.getSelectedRow());
        tradingSessionsService.remove(tradingSessions);
    }

    public void clear()
    {
        tradingSessionName.setText(null);
        tradingSessionDescription.setText(null);
        SwingUtilities.getWindowAncestor(this).dispose();
    }


    public void select()
    {
        Configuration configuration = (Configuration) tableModel.getElements().get(tradingSessionTable.getSelectedRow());
        SwingUtilities.getWindowAncestor(this).dispose();
    }

    @Override
    protected void paintComponent(Graphics g)
    {
        super.paintComponent(g); // always call super
    }
}
