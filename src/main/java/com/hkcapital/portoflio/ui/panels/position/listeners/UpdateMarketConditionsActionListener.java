package com.hkcapital.portoflio.ui.panels.position.listeners;

import com.hkcapital.portoflio.model.Position;
import com.hkcapital.portoflio.repository.registry.ServiceRegistery;
import com.hkcapital.portoflio.service.marketconditions.MarketConditionsService;
import com.hkcapital.portoflio.service.orders.OrderManagerService;
import com.hkcapital.portoflio.service.positions.PositionService;
import com.hkcapital.portoflio.service.registry.Service;
import com.hkcapital.portoflio.ui.panels.marketconditions.dialogues.MarketConditionsDialogue;
import com.hkcapital.portoflio.ui.panels.marketconditions.panels.MarketConditionsPanel;
import com.hkcapital.portoflio.ui.panels.position.tablemodels.PositionTableModel;
import com.hkcapital.portoflio.ui.panels.strategy.StrategyHeaderPanel;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class UpdateMarketConditionsActionListener implements ActionListener
{
    /**
     * Invoked when an action occurs.
     *
     * @param e the event to be processed
     */
    private PositionTableModel<Position> tableModel;

    private final PositionService positionService;

    private final OrderManagerService orderManagerService;

    private final ServiceRegistery<Service> serviceRegistery;

    private final MarketConditionsService marketConditionsService;

    private final JTable positionTableTable;

    private final StrategyHeaderPanel strategyHeaderPanel;

    public UpdateMarketConditionsActionListener(PositionTableModel<Position> tableModel, //
                                                final ServiceRegistery<Service> serviceRegistery,
                                                JTable positionTableTable,
                                                final StrategyHeaderPanel strategyHeaderPanel)
    {
        this.tableModel = tableModel;
        this.positionService = (PositionService) serviceRegistery.getService(Service.PositionService);
        this.orderManagerService = (OrderManagerService) serviceRegistery.getService(Service.OrderManagerService);
        this.serviceRegistery = serviceRegistery;
        this.positionTableTable = positionTableTable;
        this.marketConditionsService = (MarketConditionsService) serviceRegistery.getService(Service.MarketConditionsService);
        this.strategyHeaderPanel = strategyHeaderPanel;
    }

    @Override
    public void actionPerformed(ActionEvent e)
    {
        Integer id = (Integer) tableModel.getValueAt(positionTableTable.getSelectedRow(), 0);
        final MarketConditionsDialogue marketConditionsDialogue = //
                new MarketConditionsDialogue(null, new MarketConditionsPanel(serviceRegistery, null, this.tableModel,
                        id,
                        strategyHeaderPanel.getStrategy().getId()));
        marketConditionsDialogue.setVisible(true);
    }
}
