package com.hkcapital.portflio.ui;

import com.formdev.flatlaf.FlatDarkLaf;
import com.hkcapital.portflio.DataObject;
import com.hkcapital.portflio.broker.etoro.config.EtoroApiConfiguration;
import com.hkcapital.portflio.market.structure.MarketStructureManagerCache;
import com.hkcapital.portflio.model.TradingSessions;
import com.hkcapital.portflio.repository.registry.ServiceRegistery;
import com.hkcapital.portflio.service.api.etoro.EtoroApiService;
import com.hkcapital.portflio.service.api.etoro.EtoroWebSocketManagerService;
import com.hkcapital.portflio.service.candle.etoro.EtoroCandleService;
import com.hkcapital.portflio.service.configuration.ConfigurationService;
import com.hkcapital.portflio.service.orders.impl.etoro.EtoroOrderManagerServiceImpl;
import com.hkcapital.portflio.service.instrument.InstrumentService;
import com.hkcapital.portflio.service.marketconditions.MarketConditionsService;
import com.hkcapital.portflio.service.positions.PositionService;
import com.hkcapital.portflio.service.profile.ProfileService;
import com.hkcapital.portflio.service.registry.Service;
import com.hkcapital.portflio.service.srmatrix.SRMatrixService;
import com.hkcapital.portflio.service.strategy.StrategyService;
import com.hkcapital.portflio.service.tradingsessions.TradingSessionsService;
import com.hkcapital.portflio.ui.panels.configuartion.dialogues.ConfigurationDialogue;
import com.hkcapital.portflio.ui.panels.configuartion.panels.ConfigurationPanel;
import com.hkcapital.portflio.ui.panels.etoro.configuartion.dialogues.EtoroOrdersDialogue;
import com.hkcapital.portflio.ui.panels.etoro.configuartion.panels.EtoroOrdersPanel;
import com.hkcapital.portflio.ui.panels.instrument.dialogues.InstrumentDialogue;
import com.hkcapital.portflio.ui.panels.instrument.panels.InstrumentPanel;
import com.hkcapital.portflio.ui.panels.marketconditions.dialogues.MarketConditionsDialogue;
import com.hkcapital.portflio.ui.panels.marketconditions.panels.MarketConditionsPanel;
import com.hkcapital.portflio.ui.panels.position.panels.PositionActionsPanel;
import com.hkcapital.portflio.ui.panels.srmatrix.dialogues.SRMatrixDialogue;
import com.hkcapital.portflio.ui.panels.srmatrix.panels.SRMatrixPanel;
import com.hkcapital.portflio.ui.panels.strategy.StrategyHeaderPanel;
import com.hkcapital.portflio.ui.panels.tradingsessions.TradingSessionDialogue;
import com.hkcapital.portflio.ui.panels.tradingsessions.TradingSessionPanel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.swing.*;
import javax.swing.plaf.metal.MetalLookAndFeel;
import javax.swing.tree.DefaultMutableTreeNode;
import java.awt.*;

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
    private final EtoroOrderManagerServiceImpl etoroOrderManagerService;

    private final TradingSessionsService<TradingSessions> tradingSessionsService;
    private final EtoroCandleService etoroCandleService;
    private final EtoroWebSocketManagerService etoroWebSocketManagerService;
    private final SRMatrixService srMatrixService;
    private final ProfileService profileService;
    private final EtoroApiService etoroApiService;
    private final EtoroApiConfiguration etoroApiInformationService;
    private DataObject<String, String> dataObject = new DataObject<>();
    private final MarketStructureManagerCache marketStructureManagerCache;
    private final EtoroApiConfiguration etoroApiConfiguration;




    public PnLSimulatorFacad(ConfigurationService configurationService,
                             StrategyService strategyService,
                             MarketConditionsService marketConditionsService,
                             InstrumentService instrumentService,
                             PositionService positionPnLService,
                             TradingSessionsService<TradingSessions> tradingSessionsService,
                             EtoroCandleService etoroCandleService,
                             EtoroOrderManagerServiceImpl etoroOrderManager,
                             EtoroApiConfiguration apiInformationService,
                             EtoroWebSocketManagerService etoroWebSocketManagerService,
                             SRMatrixService srMatrixService,
                             ProfileService profileService,
                             EtoroApiConfiguration etoroApiConfiguration,
                             MarketStructureManagerCache marketStructureManagerCache,
                             EtoroApiService etoroApiService,
                             ServiceRegistery<Service> serviceRegistery)
    {
        this.configurationService = configurationService;
        this.strategyService = strategyService;
        this.marketConditionsService = marketConditionsService;
        this.instrumentService = instrumentService;
        this.positionPnLService = positionPnLService;
        this.tradingSessionsService = tradingSessionsService;
        this.etoroOrderManagerService = etoroOrderManager;
        this.serviceRegistery = serviceRegistery;
        this.etoroCandleService = etoroCandleService;
        this.etoroApiInformationService = apiInformationService;
        this.etoroWebSocketManagerService = etoroWebSocketManagerService;
        this.srMatrixService = srMatrixService;
        this.profileService = profileService;
        this.etoroApiConfiguration = etoroApiConfiguration;
        this.etoroApiService = etoroApiService;
        this.marketStructureManagerCache = marketStructureManagerCache;
        serviceRegistery.putService(Service.ConfigurationService, this.configurationService);
        serviceRegistery.putService(Service.StrategyService, this.strategyService);
        serviceRegistery.putService(Service.MarketConditionsService, this.marketConditionsService);
        serviceRegistery.putService(Service.InstrumentService, this.instrumentService);
        serviceRegistery.putService(Service.PositionService, this.positionPnLService);
        serviceRegistery.putService(Service.TradingSessionsService, this.tradingSessionsService);
        serviceRegistery.putService(Service.EtoroCandleService, this.etoroCandleService);
        serviceRegistery.putService(Service.OrderManagerService, this.etoroOrderManagerService);
        serviceRegistery.putService(Service.EtoroAPIConfiguration, this.etoroApiInformationService);
        serviceRegistery.putService(Service.EtoroWebSocketManagerService, this.etoroApiInformationService);
        serviceRegistery.putService(Service.SRMatrixService, this.srMatrixService);
        serviceRegistery.putService(Service.SRMatrixService, this.srMatrixService);
        serviceRegistery.putService(Service.ProfileService, this.profileService);
        serviceRegistery.putService(Service.EtoroApiService, this.etoroApiService);
        serviceRegistery.putService(Service.MarketStructureManagerCache, this.marketStructureManagerCache);

    }

    public void createApplication() throws UnsupportedLookAndFeelException
    {
        String activeProfile = profileService.getActiveProfile();
        LookAndFeel looAndFeel = new FlatDarkLaf();
        Font font = new Font("Roboto Mono", Font.PLAIN, 12);
        if ("dev".equals(activeProfile))
        {
            looAndFeel = new MetalLookAndFeel();
            font =  new Font("Roboto Mono", Font.PLAIN, 12);
        }
        UIManager.setLookAndFeel(looAndFeel);
        UIManager.put("defaultFont", font);
        JFrame mainFrame = new JFrame("HK-Capital [" + activeProfile + "]");
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

        DefaultMutableTreeNode settings = new DefaultMutableTreeNode("Settings");
        settings.add(new DefaultMutableTreeNode("Profile"));

        DefaultMutableTreeNode brokers = new DefaultMutableTreeNode("Brokers");
        DefaultMutableTreeNode etoro = new DefaultMutableTreeNode("Etoro");
        brokers.add(etoro);
        etoro.add(new DefaultMutableTreeNode("Brokers API"));
        etoro.add(new DefaultMutableTreeNode("Orders"));
        DefaultMutableTreeNode configuration = new DefaultMutableTreeNode("Configuration:");

        configuration.add(new DefaultMutableTreeNode("Positions and Leverage"));
        configuration.add(new DefaultMutableTreeNode("Time Frames"));
        configuration.add(new DefaultMutableTreeNode("Sessions"));


        DefaultMutableTreeNode marketConditions = new DefaultMutableTreeNode("Market Conditions");
        marketConditions.add(new DefaultMutableTreeNode("Market Conditions"));
        marketConditions.add(new DefaultMutableTreeNode("Economic Calendar"));
        DefaultMutableTreeNode models = new DefaultMutableTreeNode("Models");
        models.add(new DefaultMutableTreeNode("SR-Matrix"));
        models.add(new DefaultMutableTreeNode("BRP-Model"));

        root.add(settings);
        root.add(instruments);
        root.add(brokers);
        root.add(configuration);
        root.add(marketConditions);
        root.add(models);
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


            if("Orders".equals(nodeObject.toString()))
            {
                EtoroOrdersDialogue configurationDialogue = //
                        new EtoroOrdersDialogue(mainFrame, new EtoroOrdersPanel(serviceRegistery, null));
                configurationDialogue.setVisible(true);
            }

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

            if (nodeObject.toString().equals("SR-Matrix"))
            {
                // Optional: handle folders or intermediate nodes
                SRMatrixDialogue srMatrixPanel = new SRMatrixDialogue(mainFrame, //
                        new SRMatrixPanel(serviceRegistery, null));
                srMatrixPanel.setVisible(true);
            }
        });
        this.marketStructureManagerCache.openMarket();
        etoroWebSocketManagerService.subscribeAndSchedule();
    }




}
