package com.hkcapital.portoflio.ui.panels.marketconditions.dialogues;

import com.hkcapital.portoflio.ui.panels.marketconditions.labels.Labels;
import com.hkcapital.portoflio.ui.panels.marketconditions.panels.MarketConditionsPanel;

import javax.swing.*;
import java.awt.*;

public class MarketConditionsDialogue extends JDialog
{
    private final MarketConditionsPanel marketconditionsPanel;

    public MarketConditionsDialogue(Frame owner, final MarketConditionsPanel marketConditionsPanel
                                    )
    {
        super(owner, Labels.MarketConditions.getLabel(), false);
        this.marketconditionsPanel = marketConditionsPanel;
        getContentPane().add(marketConditionsPanel);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(owner);
        pack();
    }

}
