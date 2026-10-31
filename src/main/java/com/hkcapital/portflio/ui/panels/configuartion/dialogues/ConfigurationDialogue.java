package com.hkcapital.portflio.ui.panels.configuartion.dialogues;

import com.hkcapital.portflio.ui.panels.configuartion.panels.ConfigurationPanel;

import javax.swing.*;
import java.awt.*;

public class ConfigurationDialogue extends JDialog
{

    public ConfigurationDialogue(Frame owner,
                                 final ConfigurationPanel configurationPanel)
    {
        super(owner, "Configuration", false);
        getContentPane().add(configurationPanel);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(owner);
        pack();
    }

}
