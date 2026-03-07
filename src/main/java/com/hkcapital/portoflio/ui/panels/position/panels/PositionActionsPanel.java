package com.hkcapital.portoflio.ui.panels.position.panels;

import com.hkcapital.portoflio.DataObject;
import com.hkcapital.portoflio.model.Configuration;
import com.hkcapital.portoflio.model.Position;
import com.hkcapital.portoflio.model.RunningCapital;
import com.hkcapital.portoflio.repository.ServiceRegistery;
import com.hkcapital.portoflio.service.*;
import com.hkcapital.portoflio.ui.UIBag;
import com.hkcapital.portoflio.ui.fields.NumberTextField;
import com.hkcapital.portoflio.ui.panels.capital.CapitalPanel;
import com.hkcapital.portoflio.ui.panels.capital.RunningCapitalPanel;
import com.hkcapital.portoflio.ui.panels.configuartion.panels.ConfigurationSourcePanel;
import com.hkcapital.portoflio.ui.panels.marketconditions.panels.MarketConditionsSourcePanel;
import com.hkcapital.portoflio.ui.panels.position.listeners.*;
import com.hkcapital.portoflio.ui.panels.position.popupmenu.BuySellMenu;
import com.hkcapital.portoflio.ui.panels.position.tablemodels.PositionTableModel;
import com.hkcapital.portoflio.ui.panels.srmatrix.panels.SRMatrixSourcePanel;
import com.hkcapital.portoflio.ui.panels.strategy.StrategyHeaderPanel;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class PositionActionsPanel extends UIBag
{

    private final ServiceRegistery<Service> serviceRegistery;
    private MarketConditionsService marketConditionsService;
    private ConfigurationService configurationService;
    private SRMatrixService srMatrixService;
    final private PositionService positionService;
    List<Position> positionPnLList = new ArrayList<>();
    PositionTableModel positionTableModel = new PositionTableModel(positionPnLList);
    JTable positionTable = new JTable(positionTableModel);

    private final JButton addPosition = new JButton("Add Position");
    private final JButton removePosition = new JButton("Remove Position");
    private final JButton removePositionAll = new JButton("Remove All Positions");

    private final JButton marketConditionsButton = new JButton("Add Market conditions");
    private final JButton updateMarketConditions = new JButton("Update Market Conditions");

    private final JButton srMatrixButton = new JButton("Add SR Matrix");
    private final JButton updateSrMatrixButton = new JButton("Update SR Matrix");
    private final JButton configurationButton = new JButton("Add Configuration");

    private final JLabel positionSizeLabel = new JLabel("Position size in %:");
    private final NumberTextField positionSizeInPercent = new NumberTextField(30);

    private RunningCapitalPanel runningCapitalPanel = new RunningCapitalPanel(new RunningCapital(1, 5000));
    final private StrategyHeaderPanel strategyHeaderPanel;

    final ConfigurationSourcePanel configurationSourcePanel;

    final CapitalPanel capitalPanel;


    private final Frame frame;

    public PositionActionsPanel(Frame owner, final ServiceRegistery<Service> serviceRegistery,
                                DataObject dataObject,
                                final StrategyHeaderPanel strategyHeaderPanel)
    {

        super(PositionActionsPanel.class);
        this.serviceRegistery = serviceRegistery;
        marketConditionsService = (MarketConditionsService) this.serviceRegistery.getService(Service.MarketConditionsService);
        configurationService = (ConfigurationService) this.serviceRegistery.getService(Service.ConfigurationService);
        srMatrixService = (SRMatrixService) this.serviceRegistery.getService(Service.SRMatrixService);
        positionService = (PositionService) this.serviceRegistery.getService(Service.PositionService);
        JPanel buttonPanel = new JPanel();
        JPanel positionPanelParametersPanel = new JPanel();

        //Position Size panel
        JPanel positionSizePanel = new JPanel();
        //positionSizePanel.setBorder(BorderFactory.createTitledBorder("Position Size:"));
        positionSizePanel.add(positionSizeLabel);
        positionSizePanel.add(positionSizeInPercent);
        positionSizePanel.setLayout(new FlowLayout(FlowLayout.LEFT));

        //Opening capital panel


        JPanel configurationAndSourcePanel = new JPanel(new GridLayout(3, 0));

        MarketConditionsSourcePanel marketConditionsSourcePanel = new MarketConditionsSourcePanel();
        marketConditionsSourcePanel.setLayout(new FlowLayout(FlowLayout.LEFT));

        configurationSourcePanel = new ConfigurationSourcePanel(configurationService);
        configurationSourcePanel.setLayout(new FlowLayout(FlowLayout.LEFT));

        SRMatrixSourcePanel srMatrixAndSourcePanel = new SRMatrixSourcePanel();
        srMatrixAndSourcePanel.setLayout(new FlowLayout(FlowLayout.LEFT));


        capitalPanel = new CapitalPanel(serviceRegistery);
        this.frame = owner;
        positionTable.setRowHeight(25);
        positionTable.setFont(new Font("SansSerif", Font.PLAIN, 14));
        this.strategyHeaderPanel = strategyHeaderPanel;
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBorder(BorderFactory.createTitledBorder("Positions "));
        //buttonPanel.setBorder(BorderFactory.createTitledBorder("Actions"));
        buttonPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 20, 15));

        buttonPanel.add(addPosition);
        buttonPanel.add(removePosition);
        buttonPanel.add(removePositionAll);

        add(positionPanelParametersPanel);
        add(capitalPanel);
        add(buttonPanel);
        marketConditionsSourcePanel.add(marketConditionsButton);
        marketConditionsSourcePanel.add(updateMarketConditions);
        srMatrixAndSourcePanel.add(srMatrixButton);
        srMatrixAndSourcePanel.add(updateSrMatrixButton);
        configurationSourcePanel.add(configurationButton);
        configurationAndSourcePanel.add(marketConditionsSourcePanel);
        configurationAndSourcePanel.add(srMatrixAndSourcePanel);
        configurationAndSourcePanel.add(configurationSourcePanel);
        positionPanelParametersPanel.add(configurationAndSourcePanel);
        positionPanelParametersPanel.add(positionSizePanel);
        marketConditionsButton.addActionListener(new OpenMarketConditionsDialogueListener(marketConditionsSourcePanel, serviceRegistery, frame));
        configurationButton.addActionListener(new OpenConfigurationDialogueListener(configurationSourcePanel, serviceRegistery, frame));
        srMatrixButton.addActionListener(new OpenSRMatrixDialogueListener(srMatrixAndSourcePanel, serviceRegistery, frame));
        add(new JScrollPane(positionTable));
        addPosition.addActionListener(new AddPositionsButtonListener(marketConditionsService, marketConditionsSourcePanel, configurationService,
                configurationSourcePanel, strategyHeaderPanel, srMatrixAndSourcePanel, srMatrixService,
                positionPnLList, positionService, positionTableModel, this));
        removePosition.addActionListener(new RemovePositionButtonListener(positionService, positionTableModel, positionTable));
        removePositionAll.addActionListener(new RemoveAllPositionsButtonListener(positionService, positionTableModel, strategyHeaderPanel));
        positionTable.addMouseListener(new PositionEditDialogueMouseHandler(positionTableModel, positionTable, positionService));
        JMenuItem buy = new JMenuItem("Buy");
        JMenuItem sell = new JMenuItem("Sell");

        JMenuItem placeBuyOrder = new JMenuItem("Place buy order immediately");
        JMenuItem placeSellOrder = new JMenuItem("Place sell order immediately");

        positionTable.addMouseListener(new OpenBuySellContextMenuListener(positionTable, new BuySellMenu("Buy/Sell",
                new JMenuItem[]{buy,placeBuyOrder, sell, placeSellOrder})));
        buy.addActionListener(new BuyActionListener(positionTableModel, serviceRegistery, positionTable));
        sell.addActionListener(new SellActionListener(positionTableModel, serviceRegistery, positionTable));

        placeBuyOrder.addActionListener(new ImmediateBuyOrderActionListener(positionTableModel, serviceRegistery, positionTable));
        placeSellOrder.addActionListener(new ImmediateSellOrderActionListener(positionTableModel, serviceRegistery, positionTable));

    }


    public JButton getAddPosition()
    {
        return addPosition;
    }


    public JButton getRemovePosition()
    {
        return removePosition;
    }

    public JButton getRemoveAllPositions()
    {
        return removePositionAll;
    }


    public RunningCapitalPanel getRunningCapitalPanel()
    {
        return runningCapitalPanel;
    }

    public void setRunningCapitalPanel(RunningCapitalPanel runningCapitalPanel)
    {
        this.runningCapitalPanel = runningCapitalPanel;
    }


    public PositionTableModel getPositionTableModel()
    {
        return positionTableModel;
    }


    public void setPositionTable(JTable positionTable)
    {
        this.positionTable = positionTable;
    }


    public Optional<Configuration> getConfiguration()
    {
        if (configurationSourcePanel.getId() != null)
        {
            return Optional.of(configurationService.findById(configurationSourcePanel.getId().intValue()));
        }
        return Optional.empty();
    }

    public Double getPositionSizeInPercent()
    {
        if (positionSizeInPercent.getText() != null)
        {
            return positionSizeInPercent.getDoubleValue();
        }
        return null;
    }


    public CapitalPanel getCapitalPanel()
    {
        return capitalPanel;
    }
}
