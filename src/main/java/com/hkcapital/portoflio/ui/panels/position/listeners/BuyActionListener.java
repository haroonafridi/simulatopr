package com.hkcapital.portoflio.ui.panels.position.listeners;

import com.hkcapital.portoflio.model.Position;
import com.hkcapital.portoflio.service.PositionService;
import com.hkcapital.portoflio.ui.panels.position.dialogue.PositionBuyDialogue;
import com.hkcapital.portoflio.ui.panels.position.dialogue.PositionSellDialogue;
import com.hkcapital.portoflio.ui.panels.position.tablemodels.PositionTableModel;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class BuyActionListener implements ActionListener
{
    /**
     * Invoked when an action occurs.
     *
     * @param e the event to be processed
     */
    private PositionTableModel<Position> tableModel;

    private final PositionService positionService;

    private final JTable positionTableTable;

    public BuyActionListener(PositionTableModel<Position> tableModel, //
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
        new PositionBuyDialogue(positionService, id);
    }
}
