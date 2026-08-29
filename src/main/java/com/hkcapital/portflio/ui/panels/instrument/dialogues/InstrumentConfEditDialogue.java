package com.hkcapital.portflio.ui.panels.instrument.dialogues;

import com.hkcapital.portflio.model.Instrument;
import com.hkcapital.portflio.model.InstrumentMarketStructureConf;
import com.hkcapital.portflio.service.instrument.InstrumentService;
import com.hkcapital.portflio.service.instrumentmarketstructureconf.InstrumentMarketStructureConfService;
import com.hkcapital.portflio.ui.fields.NumberTextField;

import javax.swing.*;
import java.awt.*;

public class InstrumentConfEditDialogue extends JDialog
{

    final InstrumentMarketStructureConfService instMarktStrConfService;
    final Integer instConfId;

    final InstrumentMarketStructureConf instMrktConf;

    private JComboBox<Instrument> instrumentList = new JComboBox<>();
    private final JLabel moduleLabel = new JLabel("Module:");
    private final NumberTextField module = new NumberTextField(30);
    private final JLabel subLabel = new JLabel("Sub:");
    private final NumberTextField sub = new NumberTextField(30);

    private final JLabel intervalLabel = new JLabel("Interval:");
    private final NumberTextField intrvl = new NumberTextField(30);
    private final JCheckBox active = new JCheckBox();

    JButton save = new JButton("Save");
    JButton cancel = new JButton("Cancel");

    JPanel panel1 = new JPanel();


    public InstrumentConfEditDialogue(JPanel owner , InstrumentMarketStructureConfService instMarktStrConfService, //
                                      Integer instConfId)
    {
        this.instMarktStrConfService = instMarktStrConfService;
        this.instConfId = instConfId;
        instMrktConf = instMarktStrConfService.findById(instConfId);
        module.setText(String.valueOf(instMrktConf.getModule()));
        sub.setText(String.valueOf(instMrktConf.getSub()));
        intrvl.setText(String.valueOf(instMrktConf.getIntrvl()));
        active.setSelected(instMrktConf.isActive());

        JDialog dialog = new JDialog((Frame) null, "Configuration for Instrument = [" + instMrktConf.getInstrument().getName() + "]", true);
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        panel1.setLayout(new BoxLayout(panel1, BoxLayout.Y_AXIS));
        panel1.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        createLayout(panel, instMrktConf);
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
        dialog.setVisible(true);
    }

    private void createLayout(JPanel panel, InstrumentMarketStructureConf instMrktStrctConf)
    {
        active.setSelected(instMrktStrctConf.isActive());
        panel1.add(moduleLabel);
        panel1.add(module);
        panel1.add(subLabel);
        panel1.add(sub);
        panel1.add(intervalLabel);
        panel1.add(intrvl);
        panel1.add(active);
        JPanel buttonPanel = new JPanel();
        panel.add(panel1);
        buttonPanel.add(save);
        buttonPanel.add(cancel);
        panel.add(buttonPanel);
    }

    public void save()
    {
//        instrument.setName(instrumentName.getText());
//        instrument.setActive(active.isSelected());
//        System.out.println("instrumentSymbol "+instrumentSymbol.getText());
//        instrument.setInstrumentTicker(instrumentSymbol.getText());
//        instrument.setInstrumentDesc(instrumentDescription.getText());
//        instrument.setUrl(instrumentUrl.getText());
//        if (maxSlippage.getText() == null)
//        {
//            instrument.setMaxSlippage(null);
//        } else if ("".equals((maxSlippage.getText())))
//        {
//            instrument.setMaxSlippage(null);
//        } else
//        {
//            instrument.setMaxSlippage(Double.parseDouble(maxSlippage.getText()));
//        }
//        if (etoroInstrumentId.getText() == null)
//        {
//            instrument.setEtoroInstrumentId(null);
//        } else if ("".equals((etoroInstrumentId.getText())))
//        {
//            instrument.setEtoroInstrumentId(null);
//        } else
//        {
//            instrument.setEtoroInstrumentId(Integer.parseInt(etoroInstrumentId.getText()));
//        }
//        instrumentService.updateInstrument(instrument);
//        SwingUtilities.getWindowAncestor(this).dispose();
    }

}
