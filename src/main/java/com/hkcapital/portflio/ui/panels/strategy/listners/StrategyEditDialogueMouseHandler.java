package com.hkcapital.portflio.ui.panels.strategy.listners;

import com.hkcapital.portflio.model.Strategy;
import com.hkcapital.portflio.service.strategy.StrategyService;
import com.hkcapital.portflio.ui.panels.strategy.StrategyTableModel;
import com.hkcapital.portflio.ui.panels.strategy.dialogues.StrategyEditDialogue;

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