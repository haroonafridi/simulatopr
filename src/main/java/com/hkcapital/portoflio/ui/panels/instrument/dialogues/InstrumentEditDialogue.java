package com.hkcapital.portoflio.ui.panels.instrument.dialogues;

import com.hkcapital.portoflio.model.Instrument;
import com.hkcapital.portoflio.service.InstrumentService;
import com.hkcapital.portoflio.ui.fields.NumberTextField;

import javax.swing.*;
import java.awt.*;

public class InstrumentEditDialogue extends JDialog
{

    final InstrumentService instrumentService;
    final Integer instrumentId;

    final Instrument instrument;

    JLabel instrumentNameLabel = new JLabel("Name:");
    JTextField instrumentName = new JTextField(20);
    JLabel etoroInstrumentIdLabel = new JLabel("Etoro Instrument Id:");
    JTextField etoroInstrumentId = new NumberTextField(30);
    JLabel maxSlippageLabel = new JLabel("Slippage:");
    JTextField maxSlippage = new NumberTextField(30);
    JCheckBox active = new JCheckBox("Active");

    JButton save = new JButton("Save");
    JButton cancel = new JButton("Cancel");

    public InstrumentEditDialogue(InstrumentService instrumentService, Integer instrumentId)
    {
        this.instrumentService = instrumentService;
        this.instrumentId = instrumentId;
        instrument = instrumentService.findById(instrumentId);
        instrumentName.setText(instrument.getName());
        etoroInstrumentId.setText(instrument.getEtoroInstrumentId() != null ? "" + instrument.getEtoroInstrumentId() : "");
        maxSlippage.setText(instrument.getMaxSlippage() != null ? "" + instrument.getMaxSlippage() : "");
        active.setSelected(instrument.isActive());
        JDialog dialog = new JDialog((Frame) null, "Instrument = [" + instrument.getName() + "]", true);
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        createLayout(panel, instrument);
        dialog.setLayout(new BorderLayout());
        dialog.add(panel, BorderLayout.CENTER);
        save.addActionListener(s ->
        {
            save();
        });

        cancel.addActionListener(s ->
        {
            SwingUtilities.getWindowAncestor(this).dispose();
        });
        dialog.setSize(400, 230);
        dialog.setLocationRelativeTo(null);
        dialog.setVisible(true);
        dialog.pack();

    }

    private void createLayout(JPanel panel, Instrument instrument)
    {
        active.setSelected(instrument.isActive());
        panel.add(instrumentNameLabel);
        panel.add(instrumentName);
        panel.add(etoroInstrumentIdLabel);
        panel.add(etoroInstrumentId);
        panel.add(maxSlippageLabel);
        panel.add(maxSlippage);
        panel.add(active);
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(save);
        buttonPanel.add(cancel);
        panel.add(buttonPanel);
    }

    public void save()
    {
        instrument.setName(instrumentName.getText());
        instrument.setActive(active.isSelected());

        if (maxSlippage.getText() == null)
        {
            instrument.setMaxSlippage(null);
        } else if ("".equals((maxSlippage.getText())))
        {
            instrument.setMaxSlippage(null);
        } else
        {
            instrument.setMaxSlippage(Double.parseDouble(maxSlippage.getText()));
        }

        if (etoroInstrumentId.getText() == null)
        {
            instrument.setEtoroInstrumentId(null);
        } else if ("".equals((etoroInstrumentId.getText())))
        {
            instrument.setEtoroInstrumentId(null);
        } else
        {
            instrument.setEtoroInstrumentId(Integer.parseInt(etoroInstrumentId.getText()));
        }

        instrumentService.updateInstrument(instrument);
        SwingUtilities.getWindowAncestor(this).dispose();
    }

}
