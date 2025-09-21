package com.hkcapital.portoflio.ui.panels.position.listeners;

import com.hkcapital.portoflio.model.Position;
import com.hkcapital.portoflio.service.PositionService;
import com.hkcapital.portoflio.ui.panels.position.tablemodels.PositionTableModel;
import com.hkcapital.portoflio.ui.panels.strategy.StrategyHeaderPanel;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class RemoveAllPositionsButtonListener implements ActionListener
{

    private final PositionService positionService;
    private final PositionTableModel model;

    final private StrategyHeaderPanel strategyHeaderPanel;
    public RemoveAllPositionsButtonListener(final PositionService positionService,
                                            final PositionTableModel model,
                                            final StrategyHeaderPanel strategyHeaderPanel)
    {
        this.positionService = positionService;
        this.model = model;
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
        positionService.removeAll(model.getElements());
        List<Position> positionList = positionService.findByStrategyId(strategyHeaderPanel.getStrategy().getId());
        model.updateData(positionList);
    }
}
