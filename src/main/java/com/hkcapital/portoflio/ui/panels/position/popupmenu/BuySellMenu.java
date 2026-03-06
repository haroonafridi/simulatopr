package com.hkcapital.portoflio.ui.panels.position.popupmenu;

import org.springframework.stereotype.Component;

import javax.swing.*;

public class BuySellMenu extends JPopupMenu
{
    public BuySellMenu(String title, JMenuItem ...items)
    {
        super(title);
        for(JMenuItem  component: items)
        {
            add(component);
        }
    }
}
