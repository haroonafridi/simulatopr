package com.hkcapital.portoflio.ui.panels.instrument;

import com.hkcapital.portoflio.model.Instrument;
import com.hkcapital.portoflio.service.InstrumentService;

import javax.swing.*;
import java.awt.*;

public class InstrumentPanel extends JPanel
{

    private final InstrumentService instrumentService;

    private final JLabel instrumentLabel = new JLabel("Instrument Name:");
    private final JTextField instrumentName = new JTextField(30); // 20 columns looks better

    private final JTable instrumentTable;
    private final InstrumentTableModel tableModel;
    private final JButton saveButton = new JButton("Save");
    private final JButton cancelButton = new JButton("Cancel");
    private final JButton closeButton = new JButton("Close");
    private final JButton removeButton = new JButton("Remove");

    public InstrumentPanel(final InstrumentService instrumentService)
    {
        this.instrumentService = instrumentService;

        tableModel = new InstrumentTableModel<Instrument>(new String[]{"Id", "Instrument Name"}, instrumentService.findAll());

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

        JPanel instrumentPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        instrumentPanel.add(instrumentLabel);
        instrumentPanel.add(instrumentName);

        add(instrumentPanel);

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


        instrumentTable = new JTable(tableModel);

        JScrollPane scrollPane = new JScrollPane(instrumentTable);

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

        String name = instrumentName.getText();

        if (name != null && !name.trim().isEmpty())
        {
            Instrument instrument = new Instrument(name.trim());
            instrumentService.addInstrument(instrument);
            instrumentName.setText(null);
            tableModel.addRow(instrument);
            instrumentService.addInstrument(instrument);
        } else
        {
            JOptionPane.showMessageDialog(this, "Please enter an instrument name.",
                    "Validation Error", JOptionPane.WARNING_MESSAGE);
        }
    }

    public void remove()
    {
       Instrument instrument =  (Instrument) tableModel.removeRow(instrumentTable.getSelectedRow());
       instrumentService.removeInstrument(instrument);
    }

    public void clear()
    {
        instrumentName.setText(null);
    }

    public JButton getRemoveButton()
    {
        return removeButton;
    }
}
