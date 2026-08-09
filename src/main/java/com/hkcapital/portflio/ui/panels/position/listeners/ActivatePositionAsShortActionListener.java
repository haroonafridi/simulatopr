package com.hkcapital.portflio.ui.panels.position.listeners;

import com.hkcapital.portflio.model.Position;
import com.hkcapital.portflio.repository.registry.ServiceRegistery;
import com.hkcapital.portflio.service.positions.PositionService;
import com.hkcapital.portflio.service.positions.PositionType;
import com.hkcapital.portflio.service.registry.Service;
import com.hkcapital.portflio.ui.panels.position.tablemodels.PositionTableModel;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ActivatePositionAsShortActionListener implements ActionListener
{
    /**
     * Invoked when an action occurs.
     *
     * @param e the event to be processed
     */
    private PositionTableModel<Position> tableModel;

    private final ServiceRegistery<Service> serviceRegistery;

    private final JTable positionTableTable;

    public ActivatePositionAsShortActionListener(PositionTableModel<Position> tableModel, //
                                                 ServiceRegistery<Service> serviceRegistery, //
                                                 JTable positionTableTable)
    {
        this.tableModel = tableModel;
        this.serviceRegistery = serviceRegistery;
        this.positionTableTable = positionTableTable;
    }


    @Override
    public void actionPerformed(ActionEvent e)
    {
        Integer id = (Integer) tableModel.getValueAt(positionTableTable.getSelectedRow(), 0);
        PositionService positionService  = (PositionService)serviceRegistery.getService(Service.PositionService);
        Position position = positionService.findById(id);
        position.setPositionType(PositionType.SELL.getValue());
        positionService.updatePosition(position);
        tableModel.updateData(positionService.findByStrategyId(position.getStrategy().getId()));
    }
}
