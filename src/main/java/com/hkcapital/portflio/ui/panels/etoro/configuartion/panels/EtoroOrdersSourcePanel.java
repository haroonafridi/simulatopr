package com.hkcapital.portflio.ui.panels.etoro.configuartion.panels;

import com.hkcapital.portflio.service.orders.OrderManagerService;
import com.hkcapital.portflio.ui.UIBag;

import javax.swing.*;

import static com.hkcapital.portflio.ui.panels.etoro.configuartion.labels.Labels.OrdersTitle;

public class EtoroOrdersSourcePanel extends UIBag
{
    private final OrderManagerService orderManagerService;

    public EtoroOrdersSourcePanel(OrderManagerService orderManagerService)
    {
        super(EtoroOrdersSourcePanel.class);
        this.orderManagerService = orderManagerService;
        setBorder(BorderFactory.createTitledBorder(OrdersTitle.getLabel()));
    }

}
