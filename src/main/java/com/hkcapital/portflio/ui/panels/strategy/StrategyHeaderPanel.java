package com.hkcapital.portflio.ui.panels.strategy;

import com.hkcapital.portflio.broker.etoro.config.TradingConfiguration;
import com.hkcapital.portflio.broker.etoro.dto.order.EtoroMarketOrderDto;
import com.hkcapital.portflio.broker.etoro.master.Instruments;
import com.hkcapital.portflio.market.structure.MarketStructureCache;
import com.hkcapital.portflio.model.Position;
import com.hkcapital.portflio.model.Strategy;
import com.hkcapital.portflio.repository.registry.ServiceRegistery;
import com.hkcapital.portflio.service.orders.OrderManagerService;
import com.hkcapital.portflio.service.positions.PositionService;
import com.hkcapital.portflio.service.registry.Service;
import com.hkcapital.portflio.service.strategy.StrategyImportExportManager;
import com.hkcapital.portflio.service.strategy.StrategyImportExportManagerImpl;
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

    StrategyImportExportManager strategyImporterExporter;

    private final JLabel strategyNameLabel =
            new JLabel("Strategy Name:");

    private final JCheckBox activePositions = new JCheckBox("Active Positions");
    private final JCheckBox allPositions = new JCheckBox("All Positions");
    private final JTextField strategyName =
            new JTextField(20);

    private final JLabel strategyDescriptionLabel =
            new JLabel("Strategy Description:");

    private final JTextField strategyDescription =
            new JTextField(30);

    private final JLabel capitalAllocatedLabel =
            new JLabel("Capital Allocated:");

    private final JTextField capitalAllocated =
            new NumberTextField(20);

    private final JCheckBox active =
            new JCheckBox("Active");

    // Buttons
    private final JButton refreshStrategy =
            new JButton("Refresh");

    private final JButton saveStrategy =
            new JButton("Save");

    private final JButton cancelButton =
            new JButton("Cancel");

    private final JButton removeButton =
            new JButton("Remove");

    private final JButton manualOrderButton =
            new JButton("Create Market Order");

    private final JButton automaticTrading =
            new JButton("Activate Auto Trading");

    private final JButton closeMarket =
            new JButton("Close Market");

    private final JButton openMarket =
            new JButton("Open Market");
    private final JButton showLiveMarket =
            new JButton("Show Live Market");
    private final JButton exportStrategyButton =
            new JButton("Export Strategy");
    private final JButton importStrategyButton =
            new JButton("Import Strategy");
    private final JTable strategyTable;
    private final StrategyTableModel<Strategy> tableModel;
    private PositionActionsPanel positionActionsPanel;
    private final PositionService positionService;
    private final ServiceRegistery<Service> serviceRegistery;
    private final OrderManagerService orderManagerService;
    private final MarketStructureCache marketStructureManagerCache;

    public StrategyHeaderPanel(
            final ServiceRegistery<Service> serviceRegistery)
    {
        super(StrategyHeaderPanel.class);

        this.serviceRegistery = serviceRegistery;

        this.strategyService =
                (StrategyService) serviceRegistery
                        .getService(Service.StrategyService);

        this.positionService =
                (PositionService) serviceRegistery
                        .getService(Service.PositionService);

        this.orderManagerService =
                (OrderManagerService) serviceRegistery
                        .getService(Service.OrderManagerService);

        this.marketStructureManagerCache =
                (MarketStructureCache) serviceRegistery
                        .getService(Service.MarketStructureManagerCache);

        strategyImporterExporter = new StrategyImportExportManagerImpl(serviceRegistery);

        activePositions.setSelected(Boolean.TRUE);

        // ============================================================
        // MAIN PANEL
        // ============================================================

        setLayout(new BorderLayout(5, 5));

        setBorder(
                BorderFactory.createTitledBorder(
                        "Strategy Details"
                )
        );


        // ============================================================
        // TABLE MODEL
        // ============================================================

        tableModel = new StrategyTableModel<>(
                new String[]{
                        "Id",
                        "Name",
                        "Capital Deployed",
                        "Description",
                        "Active"
                },
                strategyService.findAll()
        );


        // ============================================================
        // HEADER PANEL
        //
        // Row 1 = fields
        // Row 2 = buttons
        // ============================================================

        JPanel headerPanel = new JPanel();

        headerPanel.setLayout(
                new BoxLayout(
                        headerPanel,
                        BoxLayout.Y_AXIS
                )
        );

        // ============================================================
        // ROW 1 - FIELDS
        // ============================================================

        JPanel fieldsPanel =
                new JPanel(new GridBagLayout());

        GridBagConstraints fieldGbc =
                new GridBagConstraints();

        fieldGbc.insets =
                new Insets(5, 5, 5, 5);

        fieldGbc.anchor =
                GridBagConstraints.WEST;

        fieldGbc.gridy = 0;

        // Strategy name label
        fieldGbc.gridx = 0;
        fieldGbc.weightx = 0;
        fieldGbc.fill = GridBagConstraints.NONE;

        fieldsPanel.add(
                strategyNameLabel,
                fieldGbc
        );

        // Strategy name
        fieldGbc.gridx = 1;

        fieldsPanel.add(
                strategyName,
                fieldGbc
        );

        // Capital label
        fieldGbc.gridx = 2;

        fieldsPanel.add(
                capitalAllocatedLabel,
                fieldGbc
        );

        // Capital
        fieldGbc.gridx = 3;

        fieldsPanel.add(
                capitalAllocated,
                fieldGbc
        );

        // Description label
        fieldGbc.gridx = 4;

        fieldsPanel.add(
                strategyDescriptionLabel,
                fieldGbc
        );

        // Description field
        fieldGbc.gridx = 5;
        fieldGbc.weightx = 1.0;
        fieldGbc.fill =
                GridBagConstraints.HORIZONTAL;

        fieldsPanel.add(
                strategyDescription,
                fieldGbc
        );

        // Active checkbox
        fieldGbc.gridx = 6;
        fieldGbc.weightx = 0;
        fieldGbc.fill =
                GridBagConstraints.NONE;

        fieldsPanel.add(
                active,
                fieldGbc
        );

        // ============================================================
        // ROW 2 - BUTTONS
        // ============================================================

        JPanel buttonsPanel =
                new JPanel(
                        new FlowLayout(
                                FlowLayout.LEFT,
                                5,
                                5
                        )
                );

        buttonsPanel.add(allPositions);
        buttonsPanel.add(activePositions);
        buttonsPanel.add(refreshStrategy);
        buttonsPanel.add(saveStrategy);
        buttonsPanel.add(cancelButton);
        buttonsPanel.add(removeButton);
        buttonsPanel.add(manualOrderButton);
        buttonsPanel.add(automaticTrading);
        buttonsPanel.add(showLiveMarket);
        buttonsPanel.add(openMarket);
        buttonsPanel.add(exportStrategyButton);
        buttonsPanel.add(importStrategyButton);

        // ============================================================
        // ADD HEADER ROWS
        // ============================================================
        headerPanel.add(buttonsPanel);
        headerPanel.add(fieldsPanel);

        add(
                headerPanel,
                BorderLayout.NORTH
        );

        // ============================================================
        // STRATEGY TABLE
        // ============================================================

        strategyTable =
                new JTable(tableModel);

        int rowCountToShow = 50;

        int rowHeight =
                strategyTable.getRowHeight();

        int tableHeaderHeight =
                strategyTable
                        .getTableHeader()
                        .getPreferredSize()
                        .height;

        int preferredHeight =
                rowHeight * rowCountToShow
                        + tableHeaderHeight;


        strategyTable.setFillsViewportHeight(true);

        strategyTable.setAutoResizeMode(
                JTable.AUTO_RESIZE_ALL_COLUMNS
        );

        strategyTable.setPreferredScrollableViewportSize(
                new Dimension(
                        500,
                        preferredHeight
                )
        );

        strategyTable.setSelectionMode(
                ListSelectionModel.SINGLE_SELECTION
        );


        JScrollPane scrollPane =
                new JScrollPane(strategyTable);

        add(
                scrollPane,
                BorderLayout.CENTER
        );


        // ============================================================
        // SAVE
        // ============================================================

        saveStrategy.addActionListener(
                new SaveStrategyButtonListener(
                        strategyService,
                        tableModel,
                        this
                )
        );


        // ============================================================
        // REFRESH
        // ============================================================

        refreshStrategy.addActionListener(e ->
        {
            refreshSelectedStrategy();
        });


        // ============================================================
        // TABLE SELECTION
        // ============================================================

        strategyTable
                .getSelectionModel()
                .addListSelectionListener(e ->
                {
                    if (!e.getValueIsAdjusting())
                    {
                        refreshSelectedStrategy();
                    }
                });


        // ============================================================
        // REMOVE
        // ============================================================

        removeButton.addActionListener(
                new RemoveStrategyButtonListener(
                        strategyTable,
                        tableModel,
                        strategyService,
                        this
                )
        );


        // ============================================================
        // CANCEL
        // ============================================================

        cancelButton.addActionListener(
                e -> clear()
        );


        // ============================================================
        // MANUAL ORDER
        // ============================================================

        manualOrderButton.addActionListener(
                e -> createMarketOrder()
        );


        // ============================================================
        // AUTOMATIC TRADING
        // ============================================================

        automaticTrading.addActionListener(e ->
        {
            if (TradingConfiguration.ACTIVATE_AUTOMATIC_TRADING)
            {
                TradingConfiguration.ACTIVATE_AUTOMATIC_TRADING =
                        Boolean.FALSE;

                automaticTrading.setText(
                        "Activate Auto Trading"
                );
            } else
            {
                TradingConfiguration.ACTIVATE_AUTOMATIC_TRADING =
                        Boolean.TRUE;

                automaticTrading.setText(
                        "Deactivate Auto Trading"
                );
            }
        });


        // ============================================================
        // CLOSE MARKET
        // ============================================================

        closeMarket.addActionListener(e ->
        {
            marketStructureManagerCache.closeMarket();
        });


        // ============================================================
        // OPEN MARKET
        // ============================================================

        openMarket.addActionListener(e ->
        {
            marketStructureManagerCache.openMarket();
        });


        // ============================================================
        // SHOW LIVE MARKET
        // ============================================================

        showLiveMarket.addActionListener(e ->
        {
            if (TradingConfiguration.SHOW_TRADING)
            {
                TradingConfiguration.SHOW_TRADING =
                        Boolean.FALSE;
            } else
            {
                TradingConfiguration.SHOW_TRADING =
                        Boolean.TRUE;
            }
        });

        // ============================================================
        // EXPORT STRATEGY HANDLER
        // ============================================================
        exportStrategyButton.addActionListener(e ->
        {
            int selectedRow =
                    strategyTable.getSelectedRow();

            if (selectedRow < 0)
            {
                return;
            }

            Strategy strategy =
                    (Strategy) tableModel
                            .getElements()
                            .get(selectedRow);

            strategyImporterExporter.exportStrategy(strategy.getId());

        });

        importStrategyButton.addActionListener(e ->
        {
            strategyImporterExporter.importStrategy();
        });

        // ============================================================
        // TABLE DOUBLE CLICK / MOUSE HANDLER
        // ============================================================

        strategyTable.addMouseListener(
                new StrategyEditDialogueMouseHandler(
                        tableModel,
                        strategyTable,
                        strategyService
                )
        );
    }


    // ================================================================
    // REFRESH SELECTED STRATEGY
    // ================================================================

    private void refreshSelectedStrategy()
    {
        int selectedRow =
                strategyTable.getSelectedRow();

        if (selectedRow < 0)
        {
            return;
        }

        Strategy strategy =
                (Strategy) tableModel
                        .getElements()
                        .get(selectedRow);

        setHeaderFieldsFromRow(strategy);
        List<Position> positionList;
        if (positionActionsPanel != null)
        {
            if (allPositions.isSelected())
            {
                positionList = positionService.findByStrategyIdOrderByActive(strategy.getId(), false);
            }
            else
            {
                if (activePositions.isSelected())
                {
                    positionList = positionService.findByStrategyIdAndActivePositionsOrderByActive(strategy.getId(), true);
                } else
                {
                    positionList = positionService.findByStrategyIdAndActivePositionsOrderByActive(strategy.getId(), false);
                }
            }

            positionActionsPanel.getPositionTableModel().updateData(positionList);
        }
    }


    // ================================================================
    // CREATE MARKET ORDER
    // ================================================================

    @Transactional
    public void createMarketOrder()
    {
        if (true)
        {
            throw new RuntimeException(
                    "Not Yet implemented"
            );
        }

        StrategyService strategyService =
                (StrategyService)
                        serviceRegistery
                                .getService(
                                        "StrategyService"
                                );

        Strategy strategy =
                strategyService.findById(12);

        List<Position> positionList =
                positionService.findByStrategyId(
                        strategy.getId()
                );

        TimeFrame timeFrame =
                new TimeFrame(
                        1,
                        "hour"
                );

        EtoroMarketOrderDto etoroMarketOrderDto =
                new EtoroMarketOrderDto(
                        Instruments.BTC.getInstrumentId(),
                        true,
                        1,
                        positionList
                                .stream()
                                .findFirst()
                                .get()
                                .getAllowedFirePower(),
                        null,
                        null,
                        null,
                        null,
                        null,
                        OrderTypes.MANUAL.getOrderType(),
                        null,
                        null,
                        null,
                        null,
                        "Manual Order from ui",
                        timeFrame
                );

        orderManagerService
                .createAndSaveMarketOrder(
                        etoroMarketOrderDto
                );
    }


    // ================================================================
    // SET HEADER FIELDS
    // ================================================================

    private void setHeaderFieldsFromRow(
            Strategy strategy)
    {
        active.setSelected(
                strategy.getActive()
        );

        strategyName.setText(
                strategy.getName()
        );

        strategyDescription.setText(
                strategy.getDescription()
        );

        // If Strategy has a capital field, set it here.
        // Example:
        //
        // capitalAllocated.setText(
        //         String.valueOf(strategy.getCapitalAllocated())
        // );
    }


    // ================================================================
    // GET SELECTED STRATEGY
    // ================================================================

    public Strategy getStrategy()
    {
        int selectedRow =
                strategyTable.getSelectedRow();

        if (selectedRow < 0)
        {
            return null;
        }

        return (Strategy) tableModel
                .getElements()
                .get(selectedRow);
    }


    // ================================================================
    // CLEAR
    // ================================================================

    public void clear()
    {
        strategyName.setText(null);

        strategyDescription.setText(null);

        capitalAllocated.setText(null);

        active.setSelected(false);

        if (positionActionsPanel != null)
        {
            positionActionsPanel
                    .getPositionTableModel()
                    .updateData(null);
        }
    }


    // ================================================================
    // SET POSITION ACTIONS PANEL
    // ================================================================

    public void setPositionActionsPanel(
            PositionActionsPanel positionActionsPanel)
    {
        this.positionActionsPanel =
                positionActionsPanel;
    }


    // ================================================================
    // CREATE STRATEGY
    // ================================================================

    public Strategy createStrategy()
    {
        Strategy strategy =
                Strategy.builder()
                        .name(
                                strategyName.getText()
                        )
                        .description(
                                strategyDescription.getText()
                        )
                        .creationDate(
                                LocalDateTime.now()
                        )
                        .active(
                                active.isSelected()
                        )
                        .build();

        strategyService.addStrategy(strategy);

        strategyName.setText(null);

        strategyDescription.setText(null);

        capitalAllocated.setText(null);

        active.setSelected(false);

        return strategy;
    }
}