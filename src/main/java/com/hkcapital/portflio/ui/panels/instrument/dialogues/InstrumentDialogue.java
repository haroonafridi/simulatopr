package com.hkcapital.portflio.ui.panels.instrument.dialogues;

import com.hkcapital.portflio.ui.panels.instrument.labels.Labels;
import com.hkcapital.portflio.ui.panels.instrument.panels.InstrumentPanel;

import javax.swing.*;
import java.awt.*;

public class InstrumentDialogue extends JDialog
{
    private final InstrumentPanel instrumentPanel;

    public InstrumentDialogue(Frame owner, final InstrumentPanel instrumentPanel)
    {
        super(owner, Labels.Instrument.getLabel(), false);
        this.instrumentPanel = instrumentPanel;
        getContentPane().add(instrumentPanel);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(owner);
        pack();
    }

}
