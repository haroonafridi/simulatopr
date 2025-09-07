package com.hkcapital.portoflio.ui.panels.position;

import com.hkcapital.portoflio.model.Position;
import com.hkcapital.portoflio.model.RunningCapital;
import com.hkcapital.portoflio.service.ConfigurationService;
import com.hkcapital.portoflio.service.InstrumentService;
import com.hkcapital.portoflio.service.MarketConditionsService;
import com.hkcapital.portoflio.service.PositionService;
import com.hkcapital.portoflio.ui.fields.NumberTextField;
import com.hkcapital.portoflio.ui.panels.RunningCapitalPanel;
import com.hkcapital.portoflio.ui.panels.configuartion.ConfigurationDialogue;
import com.hkcapital.portoflio.ui.panels.configuartion.ConfigurationPanel;
import com.hkcapital.portoflio.ui.panels.configuartion.ConfigurationSourcePanel;
import com.hkcapital.portoflio.ui.panels.marketconditions.MarketConditionsSourcePanel;
import com.hkcapital.portoflio.ui.panels.marketconditions.MarketConditionsDialogue;
import com.hkcapital.portoflio.ui.panels.marketconditions.MarketConditionsPanel;
import com.hkcapital.portoflio.ui.panels.strategy.StrategyHeaderPanel;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class PositionActionsPanel extends JPanel
{
    List<Position> positionPnLList = new ArrayList<>();
    PositionTableModel model = new PositionTableModel(positionPnLList);
    JTable table = new JTable(model);

    private final JButton addPosition = new JButton("Add Position");
    private final JButton removePosition = new JButton("Remove Position");
    private final JButton removePositionAll = new JButton("Remove All Positions");

    private final JButton marketConditionsButton = new JButton("Add Market conditions");
    private final JButton configurationButton = new JButton("Add Configuration");

    private final JLabel positionSizeLabel = new JLabel("Position size in %:");
    private final NumberTextField positionSize = new NumberTextField(30);
    private JPanel buttonPanel = new JPanel();
    private JPanel positionPanel = new JPanel();

    private RunningCapitalPanel runningCapitalPanel = new RunningCapitalPanel(new RunningCapital(1, 5000));

    private final InstrumentService instrumentService;
    private final MarketConditionsService marketConditionsService;

    private final ConfigurationService configurationService;

    private final PositionService positionService;

    private final MarketConditionsSourcePanel marketConditionsSourcePanel = new MarketConditionsSourcePanel();
    private final ConfigurationSourcePanel configurationSourcePanel = new ConfigurationSourcePanel();

    private StrategyHeaderPanel strategyHeaderPanel;

    private final Frame frame;

    public PositionActionsPanel(Frame owner, final PositionService positionService, //
                                final InstrumentService instrumentService,
                                final MarketConditionsService marketConditionsService,
                                final ConfigurationService configurationService,
                                StrategyHeaderPanel strategyHeaderPanel)
    {
        this.frame = owner;
        this.positionService = positionService;
        this.instrumentService = instrumentService;
        this.marketConditionsService = marketConditionsService;
        this.configurationService = configurationService;
        table.setRowHeight(25);
        table.setFont(new Font("SansSerif", Font.PLAIN, 14));
        this.strategyHeaderPanel = strategyHeaderPanel;
        setLayout(new GridLayout(3,0));
        setBorder(BorderFactory.createTitledBorder("Positions Panel"));
        buttonPanel.setBorder(BorderFactory.createTitledBorder("Actions"));
        buttonPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 20, 15));
        positionPanel.setBorder(BorderFactory.createTitledBorder("Position Parameters:"));
        buttonPanel.add(addPosition);
        buttonPanel.add(removePosition);
        buttonPanel.add(removePositionAll);
        add(positionPanel);
        add(buttonPanel);
        //add(runningCapitalPanel);
        JPanel positionParams = new JPanel(new GridLayout(1,0));
        positionParams.add(positionSizeLabel);
        positionParams.add(positionSize);
        positionParams.add(marketConditionsButton);
        positionParams.add(configurationButton);
        JPanel configurationAndSourcePanel = new JPanel(new GridLayout(3,2));
        configurationAndSourcePanel.add(marketConditionsSourcePanel);
        configurationAndSourcePanel.add(configurationSourcePanel);
        configurationAndSourcePanel.add(positionParams);
        positionPanel.add(configurationAndSourcePanel);
        marketConditionsButton.addActionListener(a -> displayMarketConditionDialogue());
        configurationButton.addActionListener(a -> displayConfigurationDialogue());
        add(new JScrollPane(table));
        addPosition.addActionListener(a->
        {
            Position position = new Position();
            position.setConfigurtaion(configurationService.findById(configurationSourcePanel.getId().getIntValue()));
            position.setMarketConditions(marketConditionsService.findById(marketConditionsSourcePanel.getPositionId().getIntValue()));
            position.setStrategy(strategyHeaderPanel.getStrategy());
            positionService.add(position);
        });
    }


    private void displayMarketConditionDialogue()
    {
        MarketConditionsDialogue marketConditionsDialogue = //
                new MarketConditionsDialogue(frame, new MarketConditionsPanel(marketConditionsService, instrumentService,
                        marketConditionsSourcePanel));
        marketConditionsDialogue.setVisible(true);
    }


    private void displayConfigurationDialogue()
    {
        ConfigurationDialogue configurationDialogue = //
                new ConfigurationDialogue(frame, new ConfigurationPanel(configurationService, configurationSourcePanel));
        configurationDialogue.setVisible(true);
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
}
