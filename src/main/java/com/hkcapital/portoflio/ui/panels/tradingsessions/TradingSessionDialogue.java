package com.hkcapital.portoflio.ui.panels.tradingsessions;

import javax.swing.*;
import java.awt.*;

public class TradingSessionDialogue extends JDialog
{
    private final TradingSessionPanel configurationPanel;

    public TradingSessionDialogue(Frame owner,
                                  final TradingSessionPanel configurationPanel)
    {
        super(owner, "Configuration", true);
        this.configurationPanel = configurationPanel;
        getContentPane().add(configurationPanel);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(owner);
        pack();
    }

}
