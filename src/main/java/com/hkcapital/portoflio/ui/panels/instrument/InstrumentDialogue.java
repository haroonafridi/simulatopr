package com.hkcapital.portoflio.ui.panels.instrument;

import javax.swing.*;
import java.awt.*;

public class InstrumentDialogue extends JDialog
{
    private final InstrumentPanel instrumentPanel;

    public InstrumentDialogue(Frame owner, final InstrumentPanel instrumentPanel)
    {
        super(owner, "Instrument", true);
        this.instrumentPanel = instrumentPanel;
        getContentPane().add(instrumentPanel);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(owner);
        pack();
    }

}
