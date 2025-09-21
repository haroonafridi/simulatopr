package com.hkcapital.portoflio.ui.panels.strategy.listners;

import com.hkcapital.portoflio.model.Strategy;
import com.hkcapital.portoflio.service.StrategyService;
import com.hkcapital.portoflio.ui.panels.strategy.StrategyHeaderPanel;
import com.hkcapital.portoflio.ui.panels.strategy.StrategyTableModel;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class RemoveStrategyButtonListener implements ActionListener
{

    private final JTable strategyTable;
    private final StrategyTableModel<Strategy> tableModel;

    private final StrategyService strategyService;

    private final StrategyHeaderPanel strategyHeaderPanel;


    public RemoveStrategyButtonListener(final JTable strategyTable, final StrategyTableModel<Strategy> tableModel, //
                                        final StrategyService strategyService,
                                        final StrategyHeaderPanel strategyHeaderPanel)
    {
        this.strategyTable = strategyTable;
        this.tableModel = tableModel;
        this.strategyService = strategyService;
        this.strategyHeaderPanel = strategyHeaderPanel;
    }

    /**
     * Invoked when an action occurs.
     *
     * @param e the event to be processed
     */
    @Override
    public void actionPerformed(ActionEvent e)
    {
        int selectedRow = strategyTable.getSelectedRow();
        if (selectedRow >= 0) {
            Strategy strategy = (Strategy) tableModel.removeRow(selectedRow);
            strategyService.removeStrategy(strategy);
            clear();
        } else {
            JOptionPane.showMessageDialog(strategyHeaderPanel, "Please select an instrument to remove.",
                    "No Selection", JOptionPane.WARNING_MESSAGE);
        }
    }
    public void clear() {
        strategyHeaderPanel.clear();
    }
}
