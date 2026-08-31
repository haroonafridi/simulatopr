package com.hkcapital.portflio.ui.panels.instrument.dialogues;

import com.hkcapital.portflio.model.Instrument;
import com.hkcapital.portflio.service.instrument.InstrumentService;
import com.hkcapital.portflio.ui.fields.NumberTextField;

import javax.swing.*;
import java.awt.*;

public class InstrumentEditDialogue extends JDialog
{

    final InstrumentService instrumentService;
    final Integer instrumentId;

    final Instrument instrument;

    JLabel instrumentSymbolLabel = new JLabel("Symbol:");
    JTextField instrumentSymbol = new JTextField(20);
    JLabel instrumentNameLabel = new JLabel("Name:");
    JTextField instrumentName = new JTextField(20);

    JLabel instrumentUrlLabel = new JLabel("Url:");
    JTextField instrumentUrl = new JTextField(60);
    JTextArea instrumentDescription = new JTextArea(40,80);
    JLabel etoroInstrumentIdLabel = new JLabel("Etoro Instrument Id:");
    JTextField etoroInstrumentId = new NumberTextField(30);
    JLabel maxSlippageLabel = new JLabel("Slippage:");
    JTextField maxSlippage = new NumberTextField(30);

    JCheckBox withCandle = new JCheckBox("With Candle");
    JCheckBox active = new JCheckBox("Active");

    JCheckBox withFeed = new JCheckBox("With Feed");

    JCheckBox withBand = new JCheckBox("With Band");

    JButton save = new JButton("Save");
    JButton cancel = new JButton("Cancel");

    JPanel panel1 = new JPanel();

    JPanel panel2 = new JPanel();
    JScrollPane scroll = new JScrollPane(instrumentDescription);


    public InstrumentEditDialogue(JPanel owner ,InstrumentService instrumentService, Integer instrumentId)
    {
        this.instrumentService = instrumentService;
        this.instrumentId = instrumentId;
        instrument = instrumentService.findById(instrumentId);
        instrumentName.setText(instrument.getName());
        instrumentSymbol.setText(instrument.getInstrumentTicker());
        instrumentUrl.setText(instrument.getUrl());
        instrumentDescription.setText(instrument.getInstrumentDesc());
        etoroInstrumentId.setText(instrument.getEtoroInstrumentId() != null ? "" + instrument.getEtoroInstrumentId() : "");
        maxSlippage.setText(instrument.getMaxSlippage() != null ? "" + instrument.getMaxSlippage() : "");
        active.setSelected(instrument.getActive() == null ? false : instrument.getActive());
        withCandle.setSelected(instrument.getWithCandle() == null ? false : instrument.getWithCandle());
        withFeed.setSelected(instrument.getWithFeed() == null ? false : instrument.getWithFeed());
        withBand.setSelected(instrument.getWithBand() == null ? false : instrument.getWithFeed());
        JDialog dialog = new JDialog((Frame) null, "Instrument = [" + instrument.getName() + "]", true);
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        panel1.setLayout(new BoxLayout(panel1, BoxLayout.Y_AXIS));
        panel1.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        panel2.setLayout(new BoxLayout(panel2, BoxLayout.PAGE_AXIS));
        panel2.setBorder(BorderFactory.createEmptyBorder(15, 0, 15, 15));
        createLayout(panel, instrument);
        dialog.setLayout(new BorderLayout());
        dialog.add(panel, BorderLayout.CENTER);
        save.addActionListener(s ->
        {
            save();
        });

        cancel.addActionListener(s ->
        {
            dispose();
        });
        dialog.pack();
        dialog.setSize(1200, 800);
       // dialog.setContentPane(owner);
        dialog.setVisible(true);
    }

    private void createLayout(JPanel panel, Instrument instrument)
    {
        active.setSelected(instrument.getActive());
        panel1.add(instrumentSymbolLabel);
        panel1.add(instrumentSymbol);
        panel1.add(instrumentNameLabel);
        panel1.add(instrumentName);
        panel1.add(instrumentUrlLabel);
        panel1.add(instrumentUrl);
        panel1.add(etoroInstrumentIdLabel);
        panel1.add(etoroInstrumentId);
        panel1.add(maxSlippageLabel);
        panel1.add(maxSlippage);
        panel1.add(active);
        panel1.add(withCandle);
        panel1.add(withFeed);
        panel1.add(withBand);
        panel2.add(scroll);
        JPanel buttonPanel = new JPanel();
        panel.add(panel1);
        panel.add(panel2);
        buttonPanel.add(save);
        buttonPanel.add(cancel);
        panel.add(buttonPanel);
    }

    public void save()
    {
        instrument.setName(instrumentName.getText());
        instrument.setActive(active.isSelected());
        instrument.setWithCandle(withCandle.isSelected());
        instrument.setWithFeed(withFeed.isSelected());
        instrument.setWithBand(withBand.isSelected());
        instrument.setInstrumentTicker(instrumentSymbol.getText());
        instrument.setInstrumentDesc(instrumentDescription.getText());
        instrument.setUrl(instrumentUrl.getText());
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
