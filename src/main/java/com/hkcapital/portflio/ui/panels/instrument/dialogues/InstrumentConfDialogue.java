package com.hkcapital.portflio.ui.panels.instrument.dialogues;

import com.hkcapital.portflio.ui.panels.instrument.labels.Labels;
import com.hkcapital.portflio.ui.panels.instrument.panels.InstrumentConfPanel;
import com.hkcapital.portflio.ui.panels.instrument.panels.InstrumentPanel;

import javax.swing.*;
import java.awt.*;

public class InstrumentConfDialogue extends JDialog
{
    private final InstrumentConfPanel instConfPanel;

    public InstrumentConfDialogue(Frame owner, final InstrumentConfPanel instConfPanel)
    {
        super(owner, Labels.InstrumentConfiguration.getLabel(), false);
        this.instConfPanel = instConfPanel;
        getContentPane().add(instConfPanel);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(owner);
        pack();
    }

}
