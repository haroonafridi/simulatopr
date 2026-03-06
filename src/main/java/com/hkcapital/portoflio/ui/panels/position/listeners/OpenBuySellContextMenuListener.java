package com.hkcapital.portoflio.ui.panels.position.listeners;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class OpenBuySellContextMenuListener extends MouseAdapter
{
    private final JTable table;

    private final JPopupMenu popupMenu;

    public OpenBuySellContextMenuListener(final JTable table, final JPopupMenu popupMenu)
    {
        this.table = table;
        this.popupMenu = popupMenu;
    }

    @Override
    public void mousePressed(MouseEvent e)
    {
        showMenu(e);
    }

    @Override
    public void mouseReleased(MouseEvent e)
    {
        showMenu(e);
    }

    private void showMenu(MouseEvent e) //
    {
        if (e.isPopupTrigger())
        {
            int row = table.rowAtPoint(e.getPoint());

            if (row >= 0 && row < table.getRowCount())
            {
                table.setRowSelectionInterval(row, row);
            } else
            {
                table.clearSelection();
            }
            popupMenu.show(e.getComponent(), e.getX(), e.getY());
        }
    }
}
