package com.hkcapital.portflio.ui.panels.indicators.dialogues;

import com.hkcapital.portflio.ui.panels.indicators.labels.Labels;
import com.hkcapital.portflio.ui.panels.indicators.panels.IndicatorsPanel;

import javax.swing.*;
import java.awt.*;

public class IndicatorsDialogue extends JDialog
{
    private final IndicatorsPanel indicatorsPanel;

    public IndicatorsDialogue(Frame owner,
                              final IndicatorsPanel indicatorsPanel)
    {
        super(owner, Labels.Indicators.getLabel(), false);
        this.indicatorsPanel = indicatorsPanel;
        getContentPane().add(indicatorsPanel);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(owner);
        pack();
    }

}
