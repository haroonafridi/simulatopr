package com.hkcapital.portflio.ui.panels.position.popupmenu;

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
