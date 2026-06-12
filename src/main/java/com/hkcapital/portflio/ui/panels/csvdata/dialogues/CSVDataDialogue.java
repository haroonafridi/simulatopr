package com.hkcapital.portflio.ui.panels.csvdata.dialogues;

import com.hkcapital.portflio.ui.panels.csvdata.labels.Labels;
import com.hkcapital.portflio.ui.panels.csvdata.panels.CSVDataPanel;

import javax.swing.*;
import java.awt.*;

public class CSVDataDialogue extends JDialog
{
    private final CSVDataPanel csvDataPanel;

    public CSVDataDialogue(Frame owner, final CSVDataPanel instrumentPanel)
    {
        super(owner, Labels.CandleData.getLabel(), false);
        this.csvDataPanel = instrumentPanel;
        getContentPane().add(instrumentPanel);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(owner);
        pack();
    }

}
