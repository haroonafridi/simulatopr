package com.hkcapital.portoflio.ui.panels.srmatrix.dialogues;

import com.hkcapital.portoflio.ui.panels.instrument.labels.Labels;
import com.hkcapital.portoflio.ui.panels.instrument.panels.InstrumentPanel;

import javax.swing.*;
import java.awt.*;

public class SRMatrixDialogue extends JDialog
{
    private final InstrumentPanel instrumentPanel;

    public SRMatrixDialogue(Frame owner, final InstrumentPanel instrumentPanel)
    {
        super(owner, Labels.Instrument.getLabel(), false);
        this.instrumentPanel = instrumentPanel;
        getContentPane().add(instrumentPanel);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(owner);
        pack();
    }

}
