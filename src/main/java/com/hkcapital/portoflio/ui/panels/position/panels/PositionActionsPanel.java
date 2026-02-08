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
import com.hkcapital.portoflio.ui.panels.position.tablemodels.PositionTableModel;
import com.hkcapital.portoflio.ui.panels.position.listeners.*;
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
    final private PositionService positionService;
    List<Position> positionPnLList = new ArrayList<>();
    PositionTableModel model = new PositionTableModel(positionPnLList);
    JTable table = new JTable(model);

    private final JButton addPosition = new JButton("Add Position");
    private final JButton removePosition = new JButton("Remove Position");
    private final JButton removePositionAll = new JButton("Remove All Positions");

    private final JButton marketConditionsButton = new JButton("Add Market conditions");
    private final JButton configurationButton = new JButton("Add Configuration");

    private final JLabel positionSizeLabel = new JLabel("Position size in %:");
    private final NumberTextField positionSizeInPercent = new NumberTextField(30);

    private RunningCapitalPanel runningCapitalPanel = new RunningCapitalPanel(new RunningCapital(1, 5000));
    final private StrategyHeaderPanel strategyHeaderPanel;

    final ConfigurationSourcePanel configurationSourcePanel;

    final  CapitalPanel capitalPanel;


    private final Frame frame;

    public PositionActionsPanel(Frame owner, final ServiceRegistery<Service> serviceRegistery,
                                DataObject dataObject,
                                final StrategyHeaderPanel strategyHeaderPanel)
    {

        super(PositionActionsPanel.class);
        this.serviceRegistery = serviceRegistery;
        marketConditionsService = (MarketConditionsService) this.serviceRegistery.getService(Service.MarketConditionsService);
        configurationService = (ConfigurationService) this.serviceRegistery.getService(Service.ConfigurationService);
        positionService = (PositionService) this.serviceRegistery.getService(Service.PositionService);
        InstrumentService instrumentService = (InstrumentService) this.serviceRegistery.getService(Service.InstrumentService);

        JPanel buttonPanel = new JPanel();

        JPanel positionPanelParametersPanel = new JPanel();

        // positionPanelParametersPanel.setBorder(BorderFactory.createTitledBorder("Position Parameters:"));

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
        //marketConditionsSourcePanel.setBorder(BorderFactory.createTitledBorder("Market Conditions:"));
        configurationSourcePanel = new ConfigurationSourcePanel(configurationService);
        configurationSourcePanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        //configurationSourcePanel.setBorder(BorderFactory.createTitledBorder("Configurations:"));
        capitalPanel = new CapitalPanel(serviceRegistery);
        this.frame = owner;
        table.setRowHeight(25);
        table.setFont(new Font("SansSerif", Font.PLAIN, 14));
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
        configurationSourcePanel.add(configurationButton);
        configurationAndSourcePanel.add(marketConditionsSourcePanel);
        configurationAndSourcePanel.add(configurationSourcePanel);
        positionPanelParametersPanel.add(configurationAndSourcePanel);
        positionPanelParametersPanel.add(positionSizePanel);
        marketConditionsButton.addActionListener(new OpenMarketConditionsDialogueListener(marketConditionsSourcePanel, serviceRegistery, frame));
        configurationButton.addActionListener(new OpenConfigurationDialogueListener(configurationSourcePanel, serviceRegistery, frame));
        add(new JScrollPane(table));
        addPosition.addActionListener(new AddPositionsButtonListener(marketConditionsService, marketConditionsSourcePanel, configurationService,
                configurationSourcePanel, strategyHeaderPanel, //
                positionPnLList, positionService, model, this));
        removePosition.addActionListener(new RemovePositionButtonListener(positionService, model, table));
        removePositionAll.addActionListener(new RemoveAllPositionsButtonListener(positionService, model, strategyHeaderPanel));
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


    public PositionTableModel getModel()
    {
        return model;
    }


    public void setTable(JTable table)
    {
        this.table = table;
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
        if(positionSizeInPercent.getText() != null)
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
