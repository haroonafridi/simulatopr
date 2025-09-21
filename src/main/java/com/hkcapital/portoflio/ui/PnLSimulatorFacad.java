package com.hkcapital.portoflio.ui;

import com.hkcapital.portoflio.DataObject;
import com.hkcapital.portoflio.model.*;
import com.hkcapital.portoflio.repository.ServiceRegistery;
import com.hkcapital.portoflio.service.*;
import com.hkcapital.portoflio.ui.panels.capital.CapitalPanel;
import com.hkcapital.portoflio.ui.panels.configuartion.dialogues.ConfigurationDialogue;
import com.hkcapital.portoflio.ui.panels.configuartion.panels.ConfigurationPanel;
import com.hkcapital.portoflio.ui.panels.instrument.dialogues.InstrumentDialogue;
import com.hkcapital.portoflio.ui.panels.instrument.panels.InstrumentPanel;
import com.hkcapital.portoflio.ui.panels.marketconditions.dialogues.MarketConditionsDialogue;
import com.hkcapital.portoflio.ui.panels.marketconditions.panels.MarketConditionsPanel;
import com.hkcapital.portoflio.ui.panels.position.panels.PositionActionsPanel;
import com.hkcapital.portoflio.ui.panels.position.tablemodels.PositionTableModel;
import com.hkcapital.portoflio.ui.panels.strategy.StrategyHeaderPanel;
import com.hkcapital.portoflio.ui.panels.tradingsessions.TradingSessionDialogue;
import com.hkcapital.portoflio.ui.panels.tradingsessions.TradingSessionPanel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;


import javax.swing.*;
import javax.swing.plaf.metal.MetalLookAndFeel;
import javax.swing.tree.DefaultMutableTreeNode;
import java.awt.*;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class PnLSimulatorFacad
{

    private static Logger logger = LoggerFactory.getLogger(PnLSimulatorFacad.class);
    private final ServiceRegistery<Service> serviceRegistery;
    private final ConfigurationService configurationService;
    private final StrategyService strategyService;
    private final MarketConditionsService marketConditionsService;
    private final InstrumentService instrumentService;

    private final PositionService positionPnLService;

    private final TradingSessionsService<TradingSessions> tradingSessionsService;

    private DataObject<String, String> dataObject = new DataObject<>();


    public PnLSimulatorFacad(ConfigurationService configurationService,
                             StrategyService strategyService,
                             MarketConditionsService marketConditionsService,
                             InstrumentService instrumentService,
                             PositionService positionPnLService,
                             TradingSessionsService<TradingSessions> tradingSessionsService,
                             final ServiceRegistery<Service> serviceRegistery)
    {
        this.configurationService = configurationService;
        this.strategyService = strategyService;
        this.marketConditionsService = marketConditionsService;
        this.instrumentService = instrumentService;
        this.positionPnLService = positionPnLService;
        this.tradingSessionsService = tradingSessionsService;
        this.serviceRegistery = serviceRegistery;

        serviceRegistery.putService("ConfigurationService", this.configurationService);
        serviceRegistery.putService("StrategyService",  this.strategyService);
        serviceRegistery.putService("MarketConditionsService", this.marketConditionsService);
        serviceRegistery.putService("InstrumentService", this.instrumentService);
        serviceRegistery.putService("PositionService", this.positionPnLService);
        serviceRegistery.putService("TradingSessionsService", this.tradingSessionsService);
    }

    public void createApplication() throws UnsupportedLookAndFeelException
    {
        UIManager.setLookAndFeel(new MetalLookAndFeel());
        //FlatDarkLaf.setup();
       // IntelliJTheme.install(PnLSimulatorFacad.class.getResourceAsStream("D:/portfolio-pnl-simulator/src/main/resources/dark-theme.properties"));
        UIManager.put("defaultFont", new Font("Roboto Mono", Font.PLAIN, 12));
        JFrame mainFrame = new JFrame("Strategy Simulator");

        // === Root layout ===
        JPanel rootPanel = new JPanel(new GridBagLayout());
        GridBagConstraints rootGbc = new GridBagConstraints();
        rootGbc.fill = GridBagConstraints.BOTH;
        rootGbc.gridy = 0; // single row in root

        // === LEFT: Navigation Tree (20% width, full height) ===
        rootGbc.gridx = 0;
        rootGbc.weightx = 0.2; // 20% of total width
        rootGbc.weighty = 1.0; // full height


        DefaultMutableTreeNode root = new DefaultMutableTreeNode("Strategy Simulator");

        DefaultMutableTreeNode instruments = new DefaultMutableTreeNode("Instruments");
        instruments.add(new DefaultMutableTreeNode("Instruments"));

        DefaultMutableTreeNode brokers = new DefaultMutableTreeNode("Brokers");
        brokers.add(new DefaultMutableTreeNode("Brokers"));
        brokers.add(new DefaultMutableTreeNode("Brokers API"));
        brokers.add(new DefaultMutableTreeNode("Broker Fee Structure"));

        DefaultMutableTreeNode configuration = new DefaultMutableTreeNode("Configuration:");

        configuration.add(new DefaultMutableTreeNode("Positions and Leverage"));
        configuration.add(new DefaultMutableTreeNode("Time Frames"));
        configuration.add(new DefaultMutableTreeNode("Sessions"));


        DefaultMutableTreeNode marketConditions = new DefaultMutableTreeNode("Market Conditions");
        marketConditions.add(new DefaultMutableTreeNode("Market Conditions"));
        marketConditions.add(new DefaultMutableTreeNode("Economic Calendar"));

        root.add(instruments);
        root.add(brokers);
        root.add(configuration);
        root.add(marketConditions);
        JTree navigationTree = new JTree(root);
        JScrollPane treeScrollPane = new JScrollPane(navigationTree);
        treeScrollPane.setBorder(BorderFactory.createTitledBorder("Navigation"));
        rootPanel.add(treeScrollPane, rootGbc);

        // === RIGHT: Vertical split with Header + Actions ===
        rootGbc.gridx = 1;
        rootGbc.weightx = 0.8; // 80% of total width

        JPanel rightPanel = new JPanel(new GridBagLayout());
        GridBagConstraints rightGbc = new GridBagConstraints();
        rightGbc.fill = GridBagConstraints.BOTH;
        rightGbc.gridx = 0;
        rightGbc.weightx = 1.0;

        // --- Top: StrategyHeaderPanel (30% height) ---
        rightGbc.gridy = 0;
        rightGbc.weighty = 0.3;
        StrategyHeaderPanel strategyHeaderPanel = new StrategyHeaderPanel(serviceRegistery);
        rightPanel.add(strategyHeaderPanel, rightGbc);

        // --- Bottom: PositionActionsPanel (70% height) ---
        rightGbc.gridy = 1;
        rightGbc.weighty = 0.7;
        PositionActionsPanel positionActionsPanel = //
                new PositionActionsPanel(
                mainFrame,
                serviceRegistery,
                        dataObject,
                strategyHeaderPanel
        );
        strategyHeaderPanel.setPositionActionsPanel(positionActionsPanel);
        rightPanel.add(positionActionsPanel, rightGbc);

        // Add right panel to root
        rootPanel.add(rightPanel, rootGbc);

        // === Frame settings ===
        mainFrame.setContentPane(rootPanel);
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.setExtendedState(JFrame.MAXIMIZED_BOTH); // fullscreen
        mainFrame.setVisible(true);

        navigationTree.addTreeSelectionListener(e ->
        {
            DefaultMutableTreeNode selectedNode =
                    (DefaultMutableTreeNode) navigationTree.getLastSelectedPathComponent();

            if (selectedNode == null) return; // nothing selected

            Object nodeObject = selectedNode.getUserObject();

            // Check if it’s a leaf node or your domain object
            if (nodeObject.toString().equals("Positions and Leverage"))
            {
                ConfigurationDialogue configurationDialogue = //
                        new ConfigurationDialogue(mainFrame, new ConfigurationPanel(serviceRegistery, null));
                configurationDialogue.setVisible(true);

            }
            if (nodeObject.toString().equals("Market Conditions"))
            {
                // Optional: handle folders or intermediate nodes
                MarketConditionsDialogue marketConditionsDialogue = new MarketConditionsDialogue(mainFrame, //
                        new MarketConditionsPanel(serviceRegistery, //
                                null));
                marketConditionsDialogue.setVisible(true);
            }

            if (nodeObject.toString().equals("Instruments"))
            {
                // Optional: handle folders or intermediate nodes
                InstrumentDialogue instrumentDialogue = new InstrumentDialogue(mainFrame, //
                        new InstrumentPanel(serviceRegistery));
                instrumentDialogue.setVisible(true);
            }

            if (nodeObject.toString().equals("Sessions"))
            {
                // Optional: handle folders or intermediate nodes
                TradingSessionDialogue instrumentDialogue = new TradingSessionDialogue(mainFrame, //
                        new TradingSessionPanel(serviceRegistery));
                instrumentDialogue.setVisible(true);
            }

        });
    }


    void saveOrUpdate(List<Position> pnl,
                      Configuration configuration,
                      MarketConditions marketCondition,
                      Instrument instrument,
                      Strategy str)
    {
        Configuration conf = configurationService.addConfiguration(configuration);
        MarketConditions marketCon = marketConditionsService.addMarketCondition(marketCondition);
        Instrument inst = instrumentService.addInstrument(instrument);
        pnl.forEach(pos ->
        {
            // Position position = positionService.addPosition(pos.getPosition());
            //position.setInstrument(null);
            //pos.setPosition(position);
            //pos.setStrategy(str);
            pos.setConfigurtaion(conf);
            pos.setMarketConditions(marketCon);

        });
        strategyService.addStrategy(str);
    }

    private static void simulate(List<Position> positionPnLList, //
                                 PositionTableModel model, //
                                 JFrame mainFrame,//
                                 PositionActionsPanel positionActionsPanel,
                                 StrategyHeaderPanel strategyHeaderPanel)
    {
        try
        {
            positionPnLList.forEach(positionPnL ->
            {
                //double percentMove = positionActionsPanel.getMarketConditions().getPercentMove();
                // double pnl = positionPnL.getPnl() + positionPnL.getCurrentPositionEquity() * (percentMove * 20) / 100;
                //  double percentPnl = (percentMove + positionPnL.getPercentPnL()) * positionPnL.getConfigurtaion().getLev();
                //positionPnL.setStrategy(strategyHeaderPanel.getStrategy());
                // positionPnL.setPnl(pnl);
                //positionPnL.setPercentPnL(percentPnl);
            });
            model.updateData(positionPnLList);
        } catch (Exception ex)
        {
            JOptionPane.showMessageDialog(mainFrame, ex.getMessage(),
                    "Configuration Error!",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void removeAllPositions(List<Position> positionPnLList, PositionTableModel model, JFrame mainFrame)
    {
        try
        {
            positionPnLList.clear();
            model.updateData(positionPnLList);
            // portfolioPnLService.updatePositionPnL(positionPnLList);
        } catch (Exception ex)
        {
            logger.error("Error while removing a row => {} ", ex.getMessage());
            JOptionPane.showMessageDialog(mainFrame, "Please choose a position to delete!", "Delete Row!", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void removePosition(List<Position> positionPnLList, PositionTableModel model, JTable table, JFrame mainFrame)
    {
        try
        {
            positionPnLList.remove(table.getSelectedRow());
            model.updateData(positionPnLList);
            //portfolioPnLService.updatePositionPnL(positionPnLList);
        } catch (Exception ex)
        {
            logger.error("Error while removing a row => {} ", ex.getMessage());
            JOptionPane.showMessageDialog(mainFrame, "Please choose a position to delete!", "Delete Row!", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void addPosition(PositionTableModel model, CapitalPanel capitalPanel, PositionActionsPanel positionActionsPanel)
    {
        OpeningCapital openingCapital = capitalPanel.getOpeningCapital();

        //MarketConditions marketConditions = positionActionsPanel.getMarketConditions();

        // portfolioPnLService.addPositionPnL(position, configuraion, openingCapital, marketConditions);

        // model.updateData(portfolioPnLService.getPositionPnLList());

        //  double totalPnl = portfolioPnLService.calculateTotalPnl();

        // positionActionsPanel.getRunningCapitalPanel().getRunningCapital().setText(Double.toString(totalPnl));
    }

    public void simulate(MarketConditions marketConditions, List<Position> positionPnLList)
    {
        List<Position> instrumentPositions = //
                positionPnLList.stream().filter(positionPnL -> //
                                positionPnL.getInstrument().getName().equals(marketConditions.getInstrument().getName())) //
                        .collect(Collectors.toList());

        for (Position instrumentPosition : instrumentPositions)
        {
            double capitalDeployed = instrumentPosition.getPercentCapitalDeployed();
            double pnl = instrumentPosition.getPnl();
            double pnlPercent = instrumentPosition.getPercentPnL();
            double lev = instrumentPosition.getConfigurtaion().getLev();
        }

    }
}
