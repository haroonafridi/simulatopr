package com.hkcapital.portoflio.ui.panels.configuartion.dialogues;

import com.hkcapital.portoflio.ui.panels.configuartion.panels.ConfigurationPanel;

import javax.swing.*;
import java.awt.*;

public class ConfigurationDialogue extends JDialog
{
    private final ConfigurationPanel configurationPanel;

    public ConfigurationDialogue(Frame owner,
                                 final ConfigurationPanel configurationPanel)
    {
        super(owner, "Configuration", true);
        this.configurationPanel = configurationPanel;
        getContentPane().add(configurationPanel);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(owner);
        pack();
    }

}
