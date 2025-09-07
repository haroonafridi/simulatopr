package com.hkcapital.portoflio.ui.panels.marketconditions;

import javax.swing.*;
import java.awt.*;

public class MarketConditionsDialogue extends JDialog
{
    private final MarketConditionsPanel marketconditionsPanel;

    public MarketConditionsDialogue(Frame owner, final MarketConditionsPanel marketConditionsPanel
                                    )
    {
        super(owner, "Market conditions", true);
        this.marketconditionsPanel = marketConditionsPanel;
        getContentPane().add(marketConditionsPanel);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(owner);
        pack();
    }

}
