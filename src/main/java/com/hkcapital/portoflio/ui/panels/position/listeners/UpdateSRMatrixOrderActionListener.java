package com.hkcapital.portoflio.ui.panels.position.listeners;

import com.hkcapital.portoflio.model.Position;
import com.hkcapital.portoflio.repository.registry.ServiceRegistery;
import com.hkcapital.portoflio.service.orders.OrderManagerService;
import com.hkcapital.portoflio.service.positions.PositionService;
import com.hkcapital.portoflio.service.srmatrix.SRMatrixService;
import com.hkcapital.portoflio.service.registry.Service;
import com.hkcapital.portoflio.ui.panels.position.tablemodels.PositionTableModel;
import com.hkcapital.portoflio.ui.panels.srmatrix.dialogues.SRMatrixDialogue;
import com.hkcapital.portoflio.ui.panels.srmatrix.panels.SRMatrixPanel;
import com.hkcapital.portoflio.ui.panels.strategy.StrategyHeaderPanel;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class UpdateSRMatrixOrderActionListener implements ActionListener
{
    /**
     * Invoked when an action occurs.
     *
     * @param e the event to be processed
     */
    private PositionTableModel<Position> tableModel;

    private final ServiceRegistery<Service> serviceRegistery;

    private final JTable positionTableTable;

    private final StrategyHeaderPanel strategyHeaderPanel;

    public UpdateSRMatrixOrderActionListener(PositionTableModel<Position> tableModel, //
                                             final ServiceRegistery<Service> serviceRegistery,
                                             JTable positionTableTable,
                                             final StrategyHeaderPanel strategyHeaderPanel)
    {
        this.tableModel = tableModel;
        PositionService positionService = (PositionService) serviceRegistery.getService(Service.PositionService);
        OrderManagerService orderManagerService = (OrderManagerService) serviceRegistery.getService(Service.OrderManagerService);
        this.serviceRegistery = serviceRegistery;
        this.positionTableTable = positionTableTable;
        SRMatrixService srMatrixService = (SRMatrixService) serviceRegistery.getService(Service.SRMatrixService);
        this.strategyHeaderPanel = strategyHeaderPanel;
    }

    @Override
    public void actionPerformed(ActionEvent e)
    {
        Integer id = (Integer) tableModel.getValueAt(positionTableTable.getSelectedRow(), 0);
        final SRMatrixDialogue srMatrixDialogue = //
                new SRMatrixDialogue(null, new SRMatrixPanel(serviceRegistery, null,this.tableModel,  id, strategyHeaderPanel.getStrategy().getId()));
        srMatrixDialogue.setVisible(true);
    }
}
