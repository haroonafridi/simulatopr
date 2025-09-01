package com.hkcapital.portoflio.ui;

import com.hkcapital.portoflio.model.*;
import com.hkcapital.portoflio.service.*;
import com.hkcapital.portoflio.ui.panels.*;
import com.hkcapital.portoflio.ui.panels.configuartion.ConfigurationDialogue;
import com.hkcapital.portoflio.ui.panels.instrument.InstrumentDialogue;
import com.hkcapital.portoflio.ui.panels.instrument.InstrumentPanel;
import com.hkcapital.portoflio.ui.panels.marketconditions.MarketConditionsDialogue;
import com.hkcapital.portoflio.ui.panels.marketconditions.MarketConditionsPanel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
@Component
public class PnLSimulatorFacad
{

    private static Logger logger = LoggerFactory.getLogger(PnLSimulatorFacad.class);
    private final ConfigurationService configurationService;
    private final StrategyService strategyService;

    private final MarketConditionsService marketConditionsService;

    private final InstrumentService instrumentService;


    private final PositionService positionPnLService;



//    PortfolioPnLServiceImpl portfolioPnLService = new PortfolioPnLServiceImpl(SimulationData.INSTRUMENTS,
//            SimulationData.CONFIGURAION,
//            SimulationData.MARKET_CONDITIONS,
//            SimulationData.POSITIONS,
//            SimulationData.OPENING_CAPITAL,
//            null);


    public PnLSimulatorFacad(ConfigurationService configurationService,
                             StrategyService strategyService,
                             MarketConditionsService marketConditionsService,
                             InstrumentService instrumentService,
                             PositionService positionPnLService)
    {
        this.configurationService = configurationService;
        this.strategyService = strategyService;
        this.marketConditionsService = marketConditionsService;
        this.instrumentService = instrumentService;
        this.positionPnLService = positionPnLService;
    }

    public void createApplication()
    {
        List<Position> positionPnLList = new ArrayList<>();
        ConfigurationTableModel model = new ConfigurationTableModel(positionPnLList);
        JTable table = new JTable(model);
        table.setRowHeight(25);
        table.setFont(new Font("SansSerif", Font.PLAIN, 14));
        JFrame mainFrame = new JFrame("PnL Simulator App");
        JPanel contents = new JPanel();
        JPanel buttonsPanel = new JPanel();
        JButton instrumentButton = new JButton("Show Instruments.");
        JButton configurationButton = new JButton("Show Configuration.");
        JButton marketConditionsButton = new JButton("Show Market Conditions.");
        buttonsPanel.add(instrumentButton);
        buttonsPanel.add(configurationButton);
        buttonsPanel.add(marketConditionsButton);
        contents.add(buttonsPanel);
        contents.setLayout(new BoxLayout(contents, BoxLayout.Y_AXIS));
        contents.setBorder(BorderFactory.createEmptyBorder(20, 200, 20, 200)); // margins
        StrategyHeaderPanel strategyHeaderPanel = new StrategyHeaderPanel();
        CapitalPanel capitalPanel = new CapitalPanel(new OpeningCapital(1, LocalDate.now(), 5000));
        PositionActionsPanel positionActionsPanel = new PositionActionsPanel();
        contents.add(strategyHeaderPanel);

        contents.add(positionActionsPanel);
        contents.add(new JScrollPane(table));
        SimulationActionsPanel simulation = new SimulationActionsPanel();
        contents.add(simulation);
        mainFrame.setContentPane(contents);
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        mainFrame.setVisible(true);

        simulation.getSimulateStrategy().addActionListener(e -> simulate(positionPnLList, model, mainFrame, positionActionsPanel, strategyHeaderPanel));
        positionActionsPanel.getRemovePosition().addActionListener(e -> removePosition(positionPnLList, model, table, mainFrame));
        positionActionsPanel.getRemoveAllPositions().addActionListener(e -> removeAllPositions(positionPnLList, model, mainFrame));
        Strategy strategy = strategyHeaderPanel.getStrategy();
       // MarketConditions marketConditions = positionActionsPanel.getMarketConditions();
        //Instrument instrument = positionActionsPanel.getPosition();

        instrumentButton.addActionListener(a -> {
            new InstrumentDialogue(mainFrame,new InstrumentPanel(instrumentService)).setVisible(true);
        });


        configurationButton.addActionListener(a -> {
            new ConfigurationDialogue(mainFrame,
                    new com.hkcapital.portoflio.ui.panels.configuartion.ConfigurationPanel(configurationService)).setVisible(true);
        });

        marketConditionsButton.addActionListener(a -> {
            new MarketConditionsDialogue(mainFrame,
                    new MarketConditionsPanel(marketConditionsService, instrumentService)).setVisible(true);
        });



    }

    void saveOrUpdate(List<Position> pnl,
                      Configuration configuration,
                      MarketConditions marketCondition,
                      Instrument instrument,
                      Strategy str) {
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
                                 ConfigurationTableModel model, //
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

    private void removeAllPositions(List<Position> positionPnLList, ConfigurationTableModel model, JFrame mainFrame)
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

    private void removePosition(List<Position> positionPnLList, ConfigurationTableModel model, JTable table, JFrame mainFrame)
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

    private void addPosition(ConfigurationTableModel model, CapitalPanel capitalPanel, PositionActionsPanel positionActionsPanel)
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
