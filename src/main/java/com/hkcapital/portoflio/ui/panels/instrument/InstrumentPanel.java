package com.hkcapital.portoflio.ui.panels.instrument;

import com.hkcapital.portoflio.model.Instrument;
import com.hkcapital.portoflio.repository.ServiceRegistery;
import com.hkcapital.portoflio.service.InstrumentService;
import com.hkcapital.portoflio.service.Service;
import com.hkcapital.portoflio.ui.buttons.ButtonLabels;

import javax.swing.*;
import java.awt.*;

public class InstrumentPanel extends JPanel
{

    private final ServiceRegistery<Service> serviceRegistery;

    private final InstrumentService instrumentService;

    private final JLabel instrumentLabel = new JLabel("Instrument Name:");
    private final JTextField instrumentName = new JTextField(30);

    private final JTable instrumentTable;
    private final InstrumentTableModel tableModel;

    private final JButton saveButton = new JButton(ButtonLabels.Save.getLabel());
    private final JButton cancelButton = new JButton(ButtonLabels.Cancel.getLabel());
    private final JButton closeButton = new JButton(ButtonLabels.Close.getLabel());
    private final JButton removeButton = new JButton(ButtonLabels.Remove.getLabel());

    public InstrumentPanel(final ServiceRegistery serviceRegistery)
    {
        this.serviceRegistery = serviceRegistery;
        this.instrumentService = (InstrumentService) this.serviceRegistery.getService(Service.InstrumentService);

        tableModel = new InstrumentTableModel<>(new String[]{Labels.Id.getLabel(), Labels.Name.getLabel()}, //
                instrumentService.findAll());

        setLayout(new GridBagLayout());
        setBorder(BorderFactory.createTitledBorder(Labels.InstrumentPanel.getLabel()));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);

        // Row 0: Instrument label + text field
        JPanel instrumentInputPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        instrumentInputPanel.add(instrumentLabel);
        instrumentInputPanel.add(instrumentName);

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.weightx = 1.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        add(instrumentInputPanel, gbc);

        // Row 1: Buttons
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

        // Row 2: Table inside scroll pane
        instrumentTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(instrumentTable);

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
    }

    public void save()
    {
        String name = instrumentName.getText();
        if (name != null && !name.trim().isEmpty())
        {
            Instrument instrument = new Instrument(name.trim());
            instrumentService.addInstrument(instrument);
            tableModel.addRow(instrument);
            instrumentName.setText(null);
        } else
        {
            JOptionPane.showMessageDialog(this, "Please enter an instrument name.",
                    "Validation Error", JOptionPane.WARNING_MESSAGE);
        }
    }

    public void remove()
    {
        int selectedRow = instrumentTable.getSelectedRow();
        if (selectedRow >= 0)
        {
            Instrument instrument = (Instrument) tableModel.removeRow(selectedRow);
            instrumentService.removeInstrument(instrument);
        } else
        {
            JOptionPane.showMessageDialog(this, "Please select an instrument to remove.",
                    "No Selection", JOptionPane.WARNING_MESSAGE);
        }
    }

    public void clear()
    {
        instrumentName.setText(null);
    }

    @Override
    protected void paintComponent(Graphics g)
    {
        super.paintComponent(g); // always call super
    }
}
