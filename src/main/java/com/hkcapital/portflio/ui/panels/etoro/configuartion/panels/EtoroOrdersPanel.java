package com.hkcapital.portflio.ui.panels.etoro.configuartion.panels;

import com.hkcapital.portflio.broker.etoro.dto.portfolio.EtoroPortfolioResponseDTO;
import com.hkcapital.portflio.model.etoro.EtoroOrder;
import com.hkcapital.portflio.repository.registry.ServiceRegistery;
import com.hkcapital.portflio.service.api.etoro.EtoroApiService;
import com.hkcapital.portflio.service.orders.OrderManagerService;
import com.hkcapital.portflio.service.registry.Service;
import com.hkcapital.portflio.ui.UIBag;
import com.hkcapital.portflio.ui.panels.etoro.configuartion.tablemodels.EtoroOrdersTableModel;
import com.hkcapital.portflio.ui.panels.etoro.configuartion.tablemodels.EtoroPortfolioOrderDto;
import com.hkcapital.portflio.ui.panels.etoro.configuartion.tablemodels.EtoroPortfolioOrdersTableModel;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class EtoroOrdersPanel extends UIBag
{
    private final ServiceRegistery<Service> serviceRegister;
    private final OrderManagerService orderManagerService;
    private final EtoroApiService etoroApiService;
    private final EtoroOrdersSourcePanel etoroOrdersSourcePanel;

    private final JButton etoroOrders = new JButton("Etoro Orders");
    private final JButton closeOrder = new JButton("Close Order");
    private final JButton addOrder = new JButton("Add Order");

    private final JTable portfolioOrderTable;
    private final JTable localOrderTable;

    private final EtoroOrdersTableModel etoroLocalOrdersModel;
    private final EtoroPortfolioOrdersTableModel etoroPortfolioOrdersModel;

    public EtoroOrdersPanel(final ServiceRegistery<Service> serviceRegistery,
                            final EtoroOrdersSourcePanel configurationSourcePanel)
    {
        super(EtoroOrdersPanel.class);

        this.serviceRegister = serviceRegistery;
        this.orderManagerService =
                (OrderManagerService) serviceRegister.getService(Service.OrderManagerService);
        this.etoroApiService =
                (EtoroApiService) serviceRegister.getService(Service.EtoroApiService);
        this.etoroOrdersSourcePanel = configurationSourcePanel;

        etoroPortfolioOrdersModel =
                new EtoroPortfolioOrdersTableModel<>(new ArrayList<>());

        etoroLocalOrdersModel =
                new EtoroOrdersTableModel<>(orderManagerService.findByInstrumentID(18));

        setLayout(new GridBagLayout());
        setBorder(BorderFactory.createTitledBorder("Etoro Orders Panel"));

        // ============================================================
        // Buttons
        // ============================================================

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));

        buttonPanel.add(etoroOrders);
        buttonPanel.add(closeOrder);
        buttonPanel.add(addOrder);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.weighty = 0.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);

        add(buttonPanel, gbc);

        // ============================================================
        // Tables
        // ============================================================

        portfolioOrderTable = new JTable(etoroPortfolioOrdersModel);
        localOrderTable = new JTable(etoroLocalOrdersModel);

        JScrollPane portfolioScrollPane =
                new JScrollPane(portfolioOrderTable);

        JScrollPane localScrollPane =
                new JScrollPane(localOrderTable);

        portfolioScrollPane.setBorder(
                BorderFactory.createTitledBorder("Portfolio Orders"));

        localScrollPane.setBorder(
                BorderFactory.createTitledBorder("Local Orders"));

        JSplitPane splitPane =
                new JSplitPane(
                        JSplitPane.HORIZONTAL_SPLIT,
                        portfolioScrollPane,
                        localScrollPane);

        splitPane.setResizeWeight(0.5);
        splitPane.setOneTouchExpandable(true);

        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(5, 5, 5, 5);

        add(splitPane, gbc);

        // ============================================================
        // Actions
        // ============================================================

        initEtoroOrders();

        etoroOrders.addActionListener(e ->
        {
            initEtoroOrders();
        });


        closeOrder.addActionListener(e ->
        {
            int selectedRow = localOrderTable.getSelectedRow();

            if (selectedRow < 0)
            {
                JOptionPane.showMessageDialog(
                        this,
                        "Please select an order first.");
                return;
            }

            final Long orderId =
                    (Long) localOrderTable.getValueAt(selectedRow, 0);

            final EtoroOrder order =
                    orderManagerService.findByorderID(orderId);

            if (order != null)
            {
                order.setStatus("CLOSED");
                order.setOrderInfo("Order closed manually");
                orderManagerService.closeEtoroOrder(order);
            }
            updateLocalOrders();
            initEtoroOrders();
        });

        addOrder.addActionListener(e ->
        {
            int selectedRow = portfolioOrderTable.getSelectedRow();
            if (selectedRow < 0)
            {
                JOptionPane.showMessageDialog(
                        this,
                        "Please select an order first.");
                return;
            }

            final EtoroPortfolioOrderDto order =
                    (EtoroPortfolioOrderDto) etoroPortfolioOrdersModel.getValueAt(selectedRow, -1);

            final EtoroOrder existingOrder = orderManagerService.findByorderID(order.getOrderId());

            if (existingOrder != null)
            {
                JOptionPane.showMessageDialog(
                        this,
                        "Order already existed in local db order id = "+existingOrder.getOrderID());
                return;
            }
            final EtoroOrder etoroOrder = new EtoroOrder();
            etoroOrder.setOrderID(order.getOrderId());
            etoroOrder.setInstrumentID(order.getInstrumentId());
            etoroOrder.setOrderInfo(
                    "Order added manually from Etoro Orders screen");
            etoroOrder.setStatus("SENT");
            etoroOrder.setOderType("MANUAL");
            etoroOrder.setTimeFrame(0);
            etoroOrder.setTimeFrameUnit("NONE");
            etoroOrder.setAmount(order.getAmount());
            orderManagerService.addEtoroOrder(etoroOrder);
            initEtoroOrders();
            updateLocalOrders();
        });
    }

    private void updateLocalOrders()
    {
        etoroLocalOrdersModel.getElements().clear();
        orderManagerService.findByInstrumentID(18).forEach(o -> etoroLocalOrdersModel.addRow(o));
    }

    @Override
    protected void paintComponent(Graphics g)
    {
        super.paintComponent(g);
    }

    private void initEtoroOrders()
    {
        etoroPortfolioOrdersModel.getElements().clear();
        final EtoroPortfolioResponseDTO portfolioResponse =
                etoroApiService.etoroPortfolio();
        portfolioResponse.getClientPortfolio()
                .getPositions()
                .stream()
                .filter(p -> p.getInstrumentId().compareTo(18) == 0)
                .forEach(p ->
                {
                    final EtoroOrder order =
                            orderManagerService.findByorderID(p.getOrderId());

                    if (order == null)
                    {
                        final EtoroPortfolioOrderDto dto =
                                EtoroPortfolioOrderDto.builder()
                                        .orderId(p.getOrderId())
                                        .amount(p.getAmount())
                                        .instrumentId(p.getInstrumentId())
                                        .isBuy(p.isBuy())
                                        .lev(p.getLeverage())
                                        .status("SENT")
                                        .info("Order is not available in local db. Can be added manually.")
                                        .build();

                        etoroPortfolioOrdersModel.addRow(dto);
                    } else if ("SENT".equals(order.getStatus()))
                    {
                        final EtoroPortfolioOrderDto dto =
                                EtoroPortfolioOrderDto.builder()
                                        .orderId(p.getOrderId())
                                        .amount(p.getAmount())
                                        .instrumentId(p.getInstrumentId())
                                        .isBuy(p.isBuy())
                                        .lev(p.getLeverage())
                                        .status(order.getStatus())
                                        .info("Order is available in local db and can be closed.")
                                        .build();
                        etoroPortfolioOrdersModel.addRow(dto);
                    }
                });
    }
}