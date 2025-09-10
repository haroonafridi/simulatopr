package com.hkcapital.portoflio.ui.panels.position;

import com.hkcapital.portoflio.model.Configuration;
import com.hkcapital.portoflio.model.MarketConditions;
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
import com.hkcapital.portoflio.ui.panels.marketconditions.MarketConditionsDialogue;
import com.hkcapital.portoflio.ui.panels.marketconditions.MarketConditionsPanel;
import com.hkcapital.portoflio.ui.panels.marketconditions.MarketConditionsSourcePanel;
import com.hkcapital.portoflio.ui.panels.strategy.StrategyHeaderPanel;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

import static com.hkcapital.portoflio.service.impl.PositionServiceImpl.calculatePosition;

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


    private RunningCapitalPanel runningCapitalPanel = new RunningCapitalPanel(new RunningCapital(1, 5000));

    private final InstrumentService instrumentService;
    private final MarketConditionsService marketConditionsService;

    private final ConfigurationService configurationService;

    private final PositionService positionService;

    private StrategyHeaderPanel strategyHeaderPanel;

    private final Frame frame;

    public PositionActionsPanel(Frame owner, final PositionService positionService, //
                                final InstrumentService instrumentService,
                                final MarketConditionsService marketConditionsService,
                                final ConfigurationService configurationService,
                                StrategyHeaderPanel strategyHeaderPanel)
    {

        JPanel buttonPanel = new JPanel();

        JPanel positionPanelParametersPanel = new JPanel();

       // positionPanelParametersPanel.setBorder(BorderFactory.createTitledBorder("Position Parameters:"));

        //Position Size panel
        JPanel positionSizePanel = new JPanel();
        //positionSizePanel.setBorder(BorderFactory.createTitledBorder("Position Size:"));
        positionSizePanel.add(positionSizeLabel);
        positionSizePanel.add(positionSize);
        positionSizePanel.setLayout(new FlowLayout(FlowLayout.LEFT));

        //Opening capital panel

        JPanel openingCapitalPanel = new JPanel();
        JLabel openingCapitalLabel = new JLabel("Opening Capital");
        JTextField openingCapital = new NumberTextField();
        openingCapitalPanel.add(openingCapitalLabel);
        openingCapitalPanel.add(openingCapital);

        JPanel configurationAndSourcePanel = new JPanel(new GridLayout(3, 0));

        MarketConditionsSourcePanel marketConditionsSourcePanel = new MarketConditionsSourcePanel();
        marketConditionsSourcePanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        //marketConditionsSourcePanel.setBorder(BorderFactory.createTitledBorder("Market Conditions:"));
        ConfigurationSourcePanel configurationSourcePanel = new ConfigurationSourcePanel();
        configurationSourcePanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        //configurationSourcePanel.setBorder(BorderFactory.createTitledBorder("Configurations:"));

        this.frame = owner;
        this.positionService = positionService;
        this.instrumentService = instrumentService;
        this.marketConditionsService = marketConditionsService;
        this.configurationService = configurationService;
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
        add(openingCapitalPanel);
        add(buttonPanel);
        marketConditionsSourcePanel.add(marketConditionsButton);
        configurationSourcePanel.add(configurationButton);
        configurationAndSourcePanel.add(marketConditionsSourcePanel);
        configurationAndSourcePanel.add(configurationSourcePanel);
        positionPanelParametersPanel.add(configurationAndSourcePanel);
        positionPanelParametersPanel.add(positionSizePanel);
        marketConditionsButton.addActionListener(a -> displayMarketConditionDialogue(marketConditionsSourcePanel));
        configurationButton.addActionListener(a -> displayConfigurationDialogue(configurationSourcePanel));

        add(new JScrollPane(table));
        addPosition.addActionListener(a ->
        {
            MarketConditions marketConditions = marketConditionsService.findById(marketConditionsSourcePanel.getPositionId().getIntValue());
            Position position = new Position();
            position.setInstrument(marketConditions.getInstrument());
            position.setConfigurtaion(configurationService.findById(configurationSourcePanel.getId().getIntValue()));
            position.setMarketConditions(marketConditions);
            position.setStrategy(strategyHeaderPanel.getStrategy());
            PositionParameters positionParameter =  calculatePosition(positionPnLList);
            position.setAllowedFirePower(positionParameter.allowedFirePower());
            position.setPercentPnL(positionParameter.percentPnl());
            position.setPnl(positionParameter.pnl());
            position.setCapitalRemainingFirePower(positionParameter.remainingCapital());
            positionService.add(position);
            List<Position> positionList = positionService.findByStrategyId(strategyHeaderPanel.getStrategy().getId());
            model.updateData(positionList);
        });

        removePosition.addActionListener(r ->
        {
            Position configuration = (Position) getModel().removeRow(table.getSelectedRow());
            positionService.remove(configuration);
        });

        removePositionAll.addActionListener(r ->
        {
            positionService.removeAll(model.getElements());
            List<Position> positionList = positionService.findByStrategyId(strategyHeaderPanel.getStrategy().getId());
            model.updateData(positionList);
        });
    }

    private void displayMarketConditionDialogue(MarketConditionsSourcePanel marketConditionsSourcePanel)
    {
        MarketConditionsDialogue marketConditionsDialogue = //
                new MarketConditionsDialogue(frame, new MarketConditionsPanel(marketConditionsService, instrumentService,
                        marketConditionsSourcePanel));
        marketConditionsDialogue.setVisible(true);
    }


    private void displayConfigurationDialogue(ConfigurationSourcePanel configurationSourcePanel)
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
