package com.hkcapital.portoflio.ui.panels.position.listeners;

import com.hkcapital.portoflio.etoro.dto.order.EtoroMarketOrderDto;
import com.hkcapital.portoflio.model.Position;
import com.hkcapital.portoflio.order.OderTypes;
import com.hkcapital.portoflio.repository.ServiceRegistery;
import com.hkcapital.portoflio.service.OrderManagerService;
import com.hkcapital.portoflio.service.PositionService;
import com.hkcapital.portoflio.service.Service;
import com.hkcapital.portoflio.ui.panels.position.tablemodels.PositionTableModel;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ImmediateBuyOrderActionListener implements ActionListener
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

    public ImmediateBuyOrderActionListener(PositionTableModel<Position> tableModel, //
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
                "Are you sure you want to place buy order!",
                "Confirm",
                JOptionPane.YES_NO_OPTION
        );

        if (result == JOptionPane.YES_OPTION) {
            createMarketOrder(id);
        }
        else
        {
            System.out.println("Placing buy order cancelled");
        }
    }


    public void createMarketOrder(Integer positionId)
    {
        Position position = positionService.findById(positionId);
        EtoroMarketOrderDto etoroMarketOrderDto = new EtoroMarketOrderDto(position.getInstrument().getEtoroInstrumentId(),
                true, //
                position.getConfiguration().getLev(), //
                position.getCurrentPositionEquity(), //
                null, //
                null, //
                null, //
                null, //
                null,
                OderTypes.MANUAL.getOrderType(),
                null,
                null,
                null,
                null);
        orderManagerService.createAndSaveMarketOrder(etoroMarketOrderDto);
    }
}
