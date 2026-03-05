package com.hkcapital.portoflio.ui.panels.strategy.listners;

import com.hkcapital.portoflio.model.Strategy;
import com.hkcapital.portoflio.service.StrategyService;
import com.hkcapital.portoflio.ui.panels.strategy.StrategyTableModel;
import com.hkcapital.portoflio.ui.panels.strategy.dialogues.StrategyEditDialogue;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class StrategyEditDialogueMouseHandler extends MouseAdapter
{
   private StrategyTableModel<Strategy> tableModel;

   private final StrategyService strategyService;

   private final JTable strategyTable;

    public StrategyEditDialogueMouseHandler(StrategyTableModel<Strategy> tableModel, JTable strategyTable ,
                                            StrategyService strategyService)
    {
        this.tableModel = tableModel;
        this.strategyService = strategyService;
        this.strategyTable = strategyTable;
    }

    @Override
    public void mouseClicked(MouseEvent e)
    {
      if(e.getClickCount() == 2)
      {
         Integer id =  (Integer)tableModel.getValueAt(strategyTable.getSelectedRow(), 0);
          new StrategyEditDialogue(strategyService, id);
      }
    }
}