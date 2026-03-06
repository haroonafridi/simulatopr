package com.hkcapital.portoflio.ui.panels.position.listeners;

import com.hkcapital.portoflio.model.Position;
import com.hkcapital.portoflio.service.PositionService;
import com.hkcapital.portoflio.ui.panels.position.dialogue.PositionBuyDialogue;
import com.hkcapital.portoflio.ui.panels.position.tablemodels.PositionTableModel;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ImmediateBuyOrderActionListener implements ActionListener
{
    /**
     * Invoked when an action occurs.
     *
     * @param e the event to be processed
     */
    private PositionTableModel<Position> tableModel;

    private final PositionService positionService;

    private final JTable positionTableTable;

    public ImmediateBuyOrderActionListener(PositionTableModel<Position> tableModel, //
                                           PositionService positionService, //
                                           JTable positionTableTable)
    {
        this.tableModel = tableModel;
        this.positionService = positionService;
        this.positionTableTable = positionTableTable;
    }

    @Override
    public void actionPerformed(ActionEvent e)
    {
       Integer id = (Integer) tableModel.getValueAt(positionTableTable.getSelectedRow(), 0);
        int result = JOptionPane.showConfirmDialog(
                null,
                "Are you sure you want to place buy order!",
                "Confirm",
                JOptionPane.YES_NO_OPTION
        );

        if (result == JOptionPane.YES_OPTION) {
            System.out.println("Placing buy order");
        } else {
            System.out.println("Placing buy order cancelled");
        }
    }
}
