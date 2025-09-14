package com.hkcapital.portoflio.ui.panels.marketconditions;

import com.hkcapital.portoflio.model.Instrument;
import com.hkcapital.portoflio.model.MarketConditions;
import com.hkcapital.portoflio.repository.ServiceRegistery;
import com.hkcapital.portoflio.service.InstrumentService;
import com.hkcapital.portoflio.service.MarketConditionsService;
import com.hkcapital.portoflio.service.Service;
import com.hkcapital.portoflio.ui.buttons.ButtonLabels;
import com.hkcapital.portoflio.ui.fields.NumberTextField;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class MarketConditionsPanel extends JPanel
{

    private final ServiceRegistery<Service> serviceRegistery;
    private final MarketConditionsService marketconditionsService;
    private final InstrumentService instrumentService;

    private JComboBox<Instrument> instrumentList = new JComboBox<>();

    private final JTable marketConditionsTableTable;
    private final MarketConditionsTableModel tableModel;
    private final JLabel dayLowLabel = new JLabel("Day Low");
    private final NumberTextField dayLow = new NumberTextField(35);

    private final JLabel dayHighLabel = new JLabel("Day High");
    private final NumberTextField dayHigh = new NumberTextField(35);
    private final JLabel percentMoveLabel = new JLabel("% Move");
    private final NumberTextField percentMove = new NumberTextField(35);

    private final JButton saveButton = new JButton(ButtonLabels.Save.getLabel());
    private final JButton cancelButton = new JButton(ButtonLabels.Cancel.getLabel());
    private final JButton closeButton = new JButton(ButtonLabels.Close.getLabel());
    private final JButton removeButton = new JButton(ButtonLabels.Remove.getLabel());

    private final JButton selectionMarketConditionsButton = new JButton(ButtonLabels.Select.getLabel());

    private final MarketConditionsSourcePanel marketConditionsSourcePanel;

    public MarketConditionsPanel(final ServiceRegistery<Service> serviceRegistery,
                                 final MarketConditionsSourcePanel marketConditionsSourcePanel)
    {
        this.serviceRegistery = serviceRegistery;
        this.instrumentService = (InstrumentService)serviceRegistery.getService("InstrumentService");
        this.marketconditionsService =  (MarketConditionsService)this.serviceRegistery.getService("MarketConditionsService");
        this.marketConditionsSourcePanel = marketConditionsSourcePanel;

        List<Instrument> instrumentList = instrumentService.findAll();

        for (Instrument instrument : instrumentList)
        {
            this.instrumentList.addItem(instrument);
        }

        tableModel = new MarketConditionsTableModel(new String[]{"Id", "Instrument Name", "Day low",
        "Day high", "% move"}, marketconditionsService.findAll());

        setLayout(new GridBagLayout());
        setBorder(BorderFactory.createTitledBorder("Instrument Panel"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);

        // Row 0: Market conditions label + text field
        JPanel marketconditionsInputPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        marketconditionsInputPanel.add(this.instrumentList);
        marketconditionsInputPanel.add(dayLowLabel);
        marketconditionsInputPanel.add(dayLow);
        marketconditionsInputPanel.add(dayHighLabel);
        marketconditionsInputPanel.add(dayHigh);
        marketconditionsInputPanel.add(percentMoveLabel);
        marketconditionsInputPanel.add(percentMove);


        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.weightx = 1.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        add(marketconditionsInputPanel, gbc);

        // Row 1: Buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        buttonPanel.add(saveButton);
        buttonPanel.add(removeButton);
        buttonPanel.add(cancelButton);
        buttonPanel.add(closeButton);
        buttonPanel.add(selectionMarketConditionsButton);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        gbc.weightx = 0;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.EAST;
        add(buttonPanel, gbc);

        // Row 2: Table inside scroll pane
        marketConditionsTableTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(marketConditionsTableTable);

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0; // allows table to expand vertically
        gbc.fill = GridBagConstraints.BOTH;
        add(scrollPane, gbc);
        saveButton.addActionListener(e -> save());
        removeButton.addActionListener(e -> remove());
        cancelButton.addActionListener(e -> clear());
        closeButton.addActionListener(e ->
        {
            // Close the dialog (find top-level window)
            SwingUtilities.getWindowAncestor(this).dispose();
        });

        selectionMarketConditionsButton.addActionListener(e-> {
            selectConfiguration();
            SwingUtilities.getWindowAncestor(this).dispose();
        });
    }

    public void save()
    {
        Instrument instrument = (Instrument) instrumentList.getModel().getSelectedItem();
        MarketConditions marketConditions = new MarketConditions(instrument, dayLow.getDoubleValue(), dayHigh.getDoubleValue(),
                percentMove.getDoubleValue());
        marketconditionsService.addMarketCondition(marketConditions);
        tableModel.addRow(marketConditions);
        dayLow.setText(null);
        dayHigh.setText(null);
        percentMove.setText(null);
    }


    public void selectConfiguration()
    {
        int selectedRow = marketConditionsTableTable.getSelectedRow();
        MarketConditions marketConditions = (MarketConditions) tableModel.getElements().get(selectedRow);
        marketConditionsSourcePanel.getPositionId().setText(marketConditions.getId().toString());
        marketConditionsSourcePanel.getDayHigh().setText(marketConditions.getDayHigh().toString());
        marketConditionsSourcePanel.getDayLow().setText(marketConditions.getDayLow().toString());
        marketConditionsSourcePanel.getPercentMove().setText(marketConditions.getPercentMove().toString());
        marketConditionsSourcePanel.getInstrumentName().setText(marketConditions.getInstrument().getName());
    }
    public void remove()
    {
        int selectedRow = marketConditionsTableTable.getSelectedRow();
        if (selectedRow >= 0)
        {
            MarketConditions marketCondition = (MarketConditions) tableModel.removeRow(selectedRow);
            marketconditionsService.removeMarketCondition(marketCondition);
        } else
        {
            JOptionPane.showMessageDialog(this, "Please select an instrument to remove.",
                    "No Selection", JOptionPane.WARNING_MESSAGE);
        }
    }

    public void clear()
    {
        //instrumentName.setText(null);
    }

    @Override
    protected void paintComponent(Graphics g)
    {
        super.paintComponent(g); // always call super
    }
}
