package com.hkcapital.portflio.ui.panels.etoro.configuartion.tablemodels;

import com.hkcapital.portflio.service.orders.OrderManagerService;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class OrderMouseHandler extends MouseAdapter
{
    private EtoroPortfolioOrdersTableModel<EtoroPortfolioOrderDto> tableModel;

    private final OrderManagerService orderManagerService;

    private final JTable portfolioOrderTable;

    public OrderMouseHandler(EtoroPortfolioOrdersTableModel<EtoroPortfolioOrderDto> tableModel,
                             OrderManagerService orderManagerService,
                             JTable portfolioOrderTable)
    {
        this.tableModel = tableModel;
        this.orderManagerService = orderManagerService;
        this.portfolioOrderTable = portfolioOrderTable;
    }

    @Override
    public void mouseClicked(MouseEvent e)
    {
        if (e.getClickCount() == 2)
        {
            Long orderId = (Long) tableModel.getValueAt(portfolioOrderTable.getSelectedRow(), 0);
            System.out.println("Order id is " + orderId);
        }
    }
}