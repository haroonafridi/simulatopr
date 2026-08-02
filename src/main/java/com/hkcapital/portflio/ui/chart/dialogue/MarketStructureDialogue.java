package com.hkcapital.portflio.ui.chart.dialogue;

import com.hkcapital.portflio.ui.chart.panel.MarketStructureChartPanel;
import com.hkcapital.portflio.ui.panels.csvdata.labels.Labels;

import javax.swing.*;
import java.awt.*;

public class MarketStructureDialogue extends JDialog
{
    private final MarketStructureChartPanel marketStructureChartPanel;

    public MarketStructureDialogue(Frame owner, final MarketStructureChartPanel marketStructureChartPanel)
    {
        super(owner, Labels.CandleData.getLabel(), false);
        this.marketStructureChartPanel = marketStructureChartPanel;
        getContentPane().add(marketStructureChartPanel);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(owner);
        this.marketStructureChartPanel.setFrame(owner);
        pack();
    }

}
