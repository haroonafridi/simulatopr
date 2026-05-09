package com.hkcapital.portflio.ui.panels.position.listeners;

import com.hkcapital.portflio.broker.etoro.dto.order.EtoroMarketOrderDto;
import com.hkcapital.portflio.model.Position;
import com.hkcapital.portflio.values.order.OrderTypes;
import com.hkcapital.portflio.repository.registry.ServiceRegistery;
import com.hkcapital.portflio.service.orders.OrderManagerService;
import com.hkcapital.portflio.service.positions.PositionService;
import com.hkcapital.portflio.service.registry.Service;
import com.hkcapital.portflio.ui.panels.position.tablemodels.PositionTableModel;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ImmediateSellOrderActionListener implements ActionListener
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

    private final JTable positionTableTable;

    public ImmediateSellOrderActionListener(PositionTableModel<Position> tableModel, //
                                            final ServiceRegistery<Service> serviceRegistery,
                                            JTable positionTableTable)
    {
        this.tableModel = tableModel;
        this.positionService = (PositionService)serviceRegistery.getService(Service.PositionService);
        this.orderManagerService = (OrderManagerService)serviceRegistery.getService(Service.OrderManagerService);
        this.serviceRegistery = serviceRegistery;
        this.positionTableTable = positionTableTable;
    }

    @Override
    public void actionPerformed(ActionEvent e)
    {
        Integer id = (Integer) tableModel.getValueAt(positionTableTable.getSelectedRow(), 0);
        int result = JOptionPane.showConfirmDialog(
                null,
                "Are you sure you want to place sell order!",
                "Confirm sell order",
                JOptionPane.YES_NO_OPTION
        );
        if (result == JOptionPane.YES_OPTION)
        {
            createMarketOrder(id);
        } else
        {
            System.out.println("Placing sell order cancelled");
        }
    }
    public void createMarketOrder(Integer positionId)
    {
        Position position = positionService.findById(positionId);
        EtoroMarketOrderDto etoroMarketOrderDto = new EtoroMarketOrderDto(position.getInstrument().getEtoroInstrumentId(),
                false, //
                position.getConfiguration().getLev(), //
                position.getCurrentPositionEquity(), //
                null, //
                null, //
                null, //
                null, //
                null,
                OrderTypes.MANUAL.getOrderType(),
                null,
                null,
                null,
                null, "Order place manually from UI");
        orderManagerService.createAndSaveMarketOrder(etoroMarketOrderDto);
    }
}
