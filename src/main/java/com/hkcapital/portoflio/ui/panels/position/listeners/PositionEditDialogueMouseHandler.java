package com.hkcapital.portoflio.ui.panels.position.listeners;

import com.hkcapital.portoflio.model.Position;
import com.hkcapital.portoflio.service.positions.PositionService;
import com.hkcapital.portoflio.ui.panels.position.dialogue.PositionEditDialogue;
import com.hkcapital.portoflio.ui.panels.position.tablemodels.PositionTableModel;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class PositionEditDialogueMouseHandler extends MouseAdapter
{
   private PositionTableModel<Position> tableModel;

   private final PositionService positionService;

   private final JTable positionTableTable;

    public PositionEditDialogueMouseHandler(PositionTableModel<Position> positionTableModel, JTable positionTable ,
                                            PositionService positionService)
    {
        this.tableModel = positionTableModel;
        this.positionService = positionService;
        this.positionTableTable = positionTable;
    }

    @Override
    public void mouseClicked(MouseEvent e)
    {
      if(e.getClickCount() == 2)
      {
         Integer id =  (Integer)tableModel.getValueAt(positionTableTable.getSelectedRow(), 0);
          new PositionEditDialogue(positionService, id);
      }
    }
}