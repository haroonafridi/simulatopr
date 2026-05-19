package com.hkcapital.portflio.ui.panels.strategy;

import com.hkcapital.portflio.broker.etoro.config.TradingConfiguration;
import com.hkcapital.portflio.broker.etoro.dto.order.EtoroMarketOrderDto;
import com.hkcapital.portflio.broker.etoro.master.Instruments;
import com.hkcapital.portflio.market.structure.MarketStructureManagerCache;
import com.hkcapital.portflio.model.Position;
import com.hkcapital.portflio.model.Strategy;
import com.hkcapital.portflio.repository.registry.ServiceRegistery;
import com.hkcapital.portflio.service.orders.OrderManagerService;
import com.hkcapital.portflio.service.positions.PositionService;
import com.hkcapital.portflio.service.registry.Service;
import com.hkcapital.portflio.service.strategy.StrategyService;
import com.hkcapital.portflio.ui.UIBag;
import com.hkcapital.portflio.ui.fields.NumberTextField;
import com.hkcapital.portflio.ui.panels.position.panels.PositionActionsPanel;
import com.hkcapital.portflio.ui.panels.strategy.listners.RemoveStrategyButtonListener;
import com.hkcapital.portflio.ui.panels.strategy.listners.SaveStrategyButtonListener;
import com.hkcapital.portflio.ui.panels.strategy.listners.StrategyEditDialogueMouseHandler;
import com.hkcapital.portflio.values.order.OrderTypes;
import com.hkcapital.portflio.values.timeframe.TimeFrame;
import org.springframework.transaction.annotation.Transactional;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDateTime;
import java.util.List;

public class StrategyHeaderPanel extends UIBag
{
    private final StrategyService strategyService;
    private final JLabel strategyNameLabel = new JLabel("Strategy Name:");
    private final JTextField strategyName = new JTextField(20);
    private final JLabel strategyDescriptionLabel = new JLabel("Strategy Description:");

    private final JTextField strategyDescription = new JTextField(40);
    private final JLabel capitalAllocatedLabel = new JLabel("Capital Allocated");
    private final JTextField capitalAllocated = new NumberTextField(40);

    private final JCheckBox active = new JCheckBox();

    private final JButton saveStrategy = new JButton("Save");

    private final JButton cancelButton = new JButton("Cancel");
    private final JButton removeButton = new JButton("Remove");

    private final JButton manualOrderButton = new JButton("Create Market Order");

    private final JButton automaticTrading = new JButton("Activate Auto Trading");

    private final JButton closeMarket = new JButton("Close Market");

    private final JButton openMarket = new JButton("Open Market");

    private final JTable strategyTable;
    private final StrategyTableModel<Strategy> tableModel;

    private PositionActionsPanel positionActionsPanel;

    private final PositionService positionService;

    private final ServiceRegistery<Service> serviceRegistery;

    private final OrderManagerService orderManagerService;

    private final MarketStructureManagerCache marketStructureManagerCache;


    public StrategyHeaderPanel(final ServiceRegistery<Service> serviceRegistery)
    {
        super(StrategyHeaderPanel.class);
        this.serviceRegistery = serviceRegistery;
        this.strategyService = (StrategyService) serviceRegistery.getService(Service.StrategyService);
        this.positionService = (PositionService) serviceRegistery.getService(Service.PositionService);
        this.orderManagerService = (OrderManagerService) serviceRegistery.getService(Service.OrderManagerService);
        this.marketStructureManagerCache = (MarketStructureManagerCache) serviceRegistery.getService(Service.MarketStructureManagerCache);;
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createTitledBorder("⚙ Strategy Details"));

        tableModel = new StrategyTableModel<>(new String[]{"Id", "Name", "Capital Deployed",
                "Description:", "Active:"}, strategyService.findAll());

        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));

        topPanel.add(strategyNameLabel);
        topPanel.add(strategyName);
        topPanel.add(capitalAllocatedLabel);
        topPanel.add(capitalAllocated);
        topPanel.add(strategyDescriptionLabel);
        topPanel.add(strategyDescription);
        topPanel.add(active);
        topPanel.add(saveStrategy);
        topPanel.add(removeButton);
        //topPanel.add(manualOrderButton);
        topPanel.add(automaticTrading);
        topPanel.add(closeMarket);
        topPanel.add(openMarket);
        add(topPanel, BorderLayout.NORTH);
        strategyTable = new JTable(tableModel);
        int rowCountToShow = 50;
        int rowHeight = strategyTable.getRowHeight();          // default row height
        int tableHeaderHeight = strategyTable.getTableHeader().getPreferredSize().height;
        int preferredHeight = rowHeight * rowCountToShow + tableHeaderHeight;

        strategyTable.setFillsViewportHeight(true);
        strategyTable.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        strategyTable.setPreferredScrollableViewportSize(new Dimension(500, preferredHeight));
        JScrollPane scrollPane = new JScrollPane(strategyTable);
        add(scrollPane, BorderLayout.CENTER);
        saveStrategy.addActionListener(new SaveStrategyButtonListener(strategyService, tableModel, this));
        strategyTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        strategyTable.getSelectionModel().addListSelectionListener(e ->
        {
            if (!e.getValueIsAdjusting())
            {
                int selectedRow = strategyTable.getSelectedRow();
                if (selectedRow >= 0)
                {
                    Strategy strategy = (Strategy) tableModel.getElements().get(selectedRow);
                    setHeaderFieldsFromRow(strategy);
                    List<Position> positionList = positionService.findByStrategyId(strategy.getId());
                    positionActionsPanel.getPositionTableModel().updateData(positionList);
                }
            }
        });

        removeButton.addActionListener(new RemoveStrategyButtonListener(strategyTable, tableModel,
                strategyService, this));
        cancelButton.addActionListener(e -> clear());
        manualOrderButton.addActionListener(m -> createMarketOrder());
        automaticTrading.addActionListener(a ->
        {
            if (TradingConfiguration.ACTIVATE_AUTOMATIC_TRADING)
            {
                TradingConfiguration.ACTIVATE_AUTOMATIC_TRADING = Boolean.FALSE;
                automaticTrading.setText("Activate Auto Trading");
            } else
            {
                TradingConfiguration.ACTIVATE_AUTOMATIC_TRADING = Boolean.TRUE;
                automaticTrading.setText("Deactivate Auto Trading");
            }
        });

        closeMarket.addActionListener(a ->
        {
            marketStructureManagerCache.closeMarket();
        });

        openMarket.addActionListener(a ->
        {
            marketStructureManagerCache.openMarket();
        });

        strategyTable.addMouseListener(new StrategyEditDialogueMouseHandler(tableModel, strategyTable, strategyService));
    }

    @Transactional
    public void createMarketOrder()
    {

        if (true)
        {
            throw new RuntimeException("Not Yet implemented");
        }
        StrategyService strategyService = (StrategyService) serviceRegistery.getService("StrategyService");
        Strategy strategy = strategyService.findById(12);
        List<Position> positionList = positionService.findByStrategyId(strategy.getId());
        TimeFrame timeFrame = new TimeFrame(1, "hour");
        EtoroMarketOrderDto etoroMarketOrderDto = new EtoroMarketOrderDto(Instruments.BTC.getInstrumentId(),
                true, //
                1, //
                positionList.stream().findFirst().get().getAllowedFirePower(), //
                null, //
                null, //
                null, //
                null, //
                null,
                OrderTypes.MANUAL.getOrderType(),
                null,
                null,
                null,
                null,
                "Manual Order from ui",
                timeFrame);
        orderManagerService.createAndSaveMarketOrder(etoroMarketOrderDto);
    }

    private void setHeaderFieldsFromRow(Strategy strategy)
    {
        active.setSelected(strategy.getActive());
        strategyName.setText(strategy.getName());
        strategyDescription.setText(strategy.getName());
    }

    public Strategy getStrategy()
    {
        return (Strategy) tableModel.getElements().get(strategyTable.getSelectedRow());
    }

    public void clear()
    {
        strategyName.setText(null);
        strategyDescription.setText(null);
        positionActionsPanel.getPositionTableModel().updateData(null);
    }


    public void setPositionActionsPanel(PositionActionsPanel positionActionsPanel)
    {
        this.positionActionsPanel = positionActionsPanel;
    }


    public Strategy createStrategy()
    {
        Strategy strategy = Strategy.builder()
                .name(strategyName.getText())
                .description(strategyDescription.getText())
                .creationDate(LocalDateTime.now())
                .active(active.isSelected()).build();
        strategyService.addStrategy(strategy);
        strategyName.setText(null);
        strategyDescription.setText(null);
        return strategy;
    }
}
