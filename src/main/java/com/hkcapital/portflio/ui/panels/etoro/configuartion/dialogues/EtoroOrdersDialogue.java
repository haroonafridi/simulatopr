package com.hkcapital.portflio.ui.panels.etoro.configuartion.dialogues;

import com.hkcapital.portflio.ui.panels.configuartion.panels.ConfigurationPanel;
import com.hkcapital.portflio.ui.panels.etoro.configuartion.panels.EtoroOrdersPanel;

import javax.swing.*;
import java.awt.*;

public class EtoroOrdersDialogue extends JDialog
{
    private final EtoroOrdersPanel etoroOrdersPanel;

    public EtoroOrdersDialogue(Frame owner,
                               final EtoroOrdersPanel etoroOrdersPanel)
    {
        super(owner, "Etoro Orders", false);
        this.etoroOrdersPanel = etoroOrdersPanel;
        getContentPane().add(etoroOrdersPanel);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(owner);
        pack();
    }

}
