package com.hkcapital.portoflio.ui.panels.strategy;

import com.hkcapital.portoflio.etoro.Configuration;
import com.hkcapital.portoflio.etoro.dto.order.EtoroMarketOrderDto;
import com.hkcapital.portoflio.etoro.master.Instruments;
import com.hkcapital.portoflio.model.Position;
import com.hkcapital.portoflio.model.Strategy;
import com.hkcapital.portoflio.order.OderTypes;
import com.hkcapital.portoflio.repository.ServiceRegistery;
import com.hkcapital.portoflio.service.OrderManagerService;
import com.hkcapital.portoflio.service.PositionService;
import com.hkcapital.portoflio.service.Service;
import com.hkcapital.portoflio.service.StrategyService;
import com.hkcapital.portoflio.ui.UIBag;
import com.hkcapital.portoflio.ui.panels.position.panels.PositionActionsPanel;
import com.hkcapital.portoflio.ui.panels.strategy.listners.RemoveStrategyButtonListener;
import com.hkcapital.portoflio.ui.panels.strategy.listners.SaveStrategyButtonListener;
import com.hkcapital.portoflio.ui.panels.strategy.listners.StrategyEditDialogueMouseHandler;
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

    private final JCheckBox active = new JCheckBox();

    private final JButton saveStrategy = new JButton();

    private final JButton cancelButton = new JButton("Cancel");
    private final JButton closeButton = new JButton("Close");
    private final JButton removeButton = new JButton("Remove");

    private final JButton manualOrderButton = new JButton("Create Market Order");

    private final JButton automaticTrading = new JButton("Activate Auto Trading");

    private final JTable strategyTable;
    private final StrategyTableModel<Strategy> tableModel;

    private PositionActionsPanel positionActionsPanel;

    private final PositionService positionService;

    private final ServiceRegistery<Service> serviceRegistery;

    private final OrderManagerService orderManagerService;


    public StrategyHeaderPanel(final ServiceRegistery<Service> serviceRegistery)
    {
        super(StrategyHeaderPanel.class);
        this.serviceRegistery = serviceRegistery;
        this.strategyService = (StrategyService) serviceRegistery.getService(Service.StrategyService);
        this.positionService = (PositionService) serviceRegistery.getService(Service.PositionService);
        this.orderManagerService = (OrderManagerService) serviceRegistery.getService(Service.OrderManagerService);
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createTitledBorder("⚙ Strategy Details"));

        tableModel = new StrategyTableModel<>(new String[]{"Id", "Name",
                "Description:","Active:"}, strategyService.findAll());

        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));

        topPanel.add(strategyNameLabel);
        topPanel.add(strategyName);
        topPanel.add(strategyDescriptionLabel);
        topPanel.add(active);
        topPanel.add(strategyDescription);
        topPanel.add(saveStrategy);
        topPanel.add(removeButton);
        topPanel.add(manualOrderButton);
        topPanel.add(automaticTrading);
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
            if (Configuration.ACTIVATE_AUTOMATIC_TRADING)
            {
                Configuration.ACTIVATE_AUTOMATIC_TRADING = Boolean.FALSE;
                automaticTrading.setText("Activate Auto Trading");
            } else
            {
                Configuration.ACTIVATE_AUTOMATIC_TRADING = Boolean.TRUE;
                automaticTrading.setText("Deactivate Auto Trading");
            }
        });

        strategyTable.addMouseListener(new StrategyEditDialogueMouseHandler(tableModel, strategyTable, strategyService));
    }

    @Transactional
    public void createMarketOrder()
    {
        StrategyService strategyService = (StrategyService) serviceRegistery.getService("StrategyService");
        Strategy strategy = strategyService.findById(12);
        List<Position> positionList = positionService.findByStrategyId(strategy.getId());
        EtoroMarketOrderDto etoroMarketOrderDto = new EtoroMarketOrderDto(Instruments.BTC.getInstrumentId(),
                true, //
                1, //
                positionList.stream().findFirst().get().getAllowedFirePower(), //
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
        Strategy strategy = new Strategy(strategyName.getText(),
                strategyDescription.getText(),
                LocalDateTime.now());
        strategy.setActive(active.isSelected());
        strategyService.addStrategy(strategy);
        strategyName.setText(null);
        strategyDescription.setText(null);

        return strategy;
    }
}
