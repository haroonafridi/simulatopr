package com.hkcapital.portoflio.ui.panels.position.listeners;

import com.hkcapital.portoflio.model.Position;
import com.hkcapital.portoflio.service.PositionService;
import com.hkcapital.portoflio.ui.panels.position.tablemodels.PositionTableModel;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class RemovePositionButtonListener implements ActionListener
{

    private final PositionService positionService;
    private final PositionTableModel model;

    private final  JTable table;


    public RemovePositionButtonListener(final PositionService positionService,
                                        final PositionTableModel model,
                                        final  JTable table)
    {
        this.positionService = positionService;
        this.model = model;
        this.table = table;
    }
    /**
     * Invoked when an action occurs.
     *
     * @param e the event to be processed
     */
    @Override
    public void actionPerformed(ActionEvent e)
    {
        Position configuration = (Position) model.removeRow(table.getSelectedRow());
        positionService.remove(configuration);
    }
}
