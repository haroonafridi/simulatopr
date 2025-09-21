package com.hkcapital.portoflio.ui.panels.strategy.listners;

import com.hkcapital.portoflio.model.Strategy;
import com.hkcapital.portoflio.service.StrategyService;
import com.hkcapital.portoflio.ui.panels.strategy.StrategyHeaderPanel;
import com.hkcapital.portoflio.ui.panels.strategy.StrategyTableModel;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SaveStrategyButtonListener implements ActionListener
{

    private final StrategyService strategyService;

    final StrategyTableModel<Strategy> tableModel;
    final StrategyHeaderPanel strategyHeaderPanel;

    /**
     * Invoked when an action occurs.
     *
     * @param e the event to be processed
     */

    public SaveStrategyButtonListener(
            final StrategyService strategyService,
            final StrategyTableModel<Strategy> tableModel,
            final StrategyHeaderPanel strategyHeaderPanel) //
    {
        this.strategyService = strategyService;
        this.tableModel = tableModel;
        this.strategyHeaderPanel = strategyHeaderPanel;
    }

    @Override
    public void actionPerformed(ActionEvent e)
    {
        tableModel.addRow(strategyHeaderPanel.createStrategy());
        tableModel.fireTableDataChanged();
    }
}
