package com.hkcapital.portoflio.ui;

import com.hkcapital.portoflio.PnLSimulationApp;
import com.hkcapital.portoflio.model.*;
import com.hkcapital.portoflio.service.PortfolioPnLService;
import com.hkcapital.portoflio.simulation.SimulationData;
import com.hkcapital.portoflio.ui.panels.CapitalPanel;
import com.hkcapital.portoflio.ui.panels.ConfigurationPanel;
import com.hkcapital.portoflio.ui.panels.PositionActionsPanel;
import com.hkcapital.portoflio.ui.panels.SimulationActionsPanel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class PnLSimulatorFacad
{
    private static Logger logger = LoggerFactory.getLogger(PnLSimulationApp.class);
    PortfolioPnLService portfolioPnLService = new PortfolioPnLService(SimulationData.INSTRUMENTS,
            SimulationData.CONFIGURAION,
            SimulationData.MARKET_CONDITIONS,
            SimulationData.POSITIONS,
            SimulationData.OPENING_CAPITAL);

    public void createApplication()
    {
        List<PositionPnL> positionPnLList = new ArrayList<>();
        ConfigurationTableModel model = new ConfigurationTableModel(positionPnLList);
        JTable table = new JTable(model);
        table.setRowHeight(25);
        table.setFont(new Font("SansSerif", Font.PLAIN, 14));
        JFrame mainFrame = new JFrame("PnL Simulator App");
        JPanel contents = new JPanel();
        contents.setLayout(new BoxLayout(contents, BoxLayout.Y_AXIS));
        contents.setBorder(BorderFactory.createEmptyBorder(20, 200, 20, 200)); // margins
        ConfigurationPanel configurationPanel = new ConfigurationPanel();
        CapitalPanel capitalPanel =  new CapitalPanel(new OpeningCapital(1, LocalDate.now(), 5000));
        configurationPanel.add(capitalPanel);
        contents.add(configurationPanel);
        PositionActionsPanel positionActionsPanel = new PositionActionsPanel();
        contents.add(configurationPanel);
        contents.add(positionActionsPanel);
        contents.add(new JScrollPane(table));
        SimulationActionsPanel simulation = new SimulationActionsPanel();
        contents.add(simulation);
        mainFrame.setContentPane(contents);
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        mainFrame.setVisible(true);
        simulation.getSimulate().addActionListener(e ->
        {
            try
            {
                positionPnLList.forEach(positionPnL ->
                {
                   double percentMove =  positionActionsPanel.getMarketConditions().getPercentMove();
                   double  pnl = positionPnL.getPnl() + positionPnL.getCurrentPositionEquity() * (percentMove * 20) / 100;
                   double  percentPnl = (percentMove + positionPnL.getPercentPnL()) * positionPnL.getConfiguraion().getLev();
                   positionPnL.setPnl(pnl);
                    positionPnL.setPercentPnL(percentPnl);
                });
                model.updateData(positionPnLList);
            } catch (Exception ex)
            {
                JOptionPane.showMessageDialog(mainFrame, ex.getMessage(),
                        "Configuration Error!",
                        JOptionPane.ERROR_MESSAGE);
            }

        });


        positionActionsPanel.getAddPosition().addActionListener(e ->
        {
            Position position = positionActionsPanel.getPosition();

            Configuraion configuraion = configurationPanel.getConfiguration();

            OpeningCapital openingCapital = capitalPanel.getOpeningCapital();

            double capital = Math.round( openingCapital.getCapital()* (position.getPercentCapitalDeployed() / 100) );

            double allowedFirepower = openingCapital.getCapital() * (configuraion.getMaxPercentAllowedPerInstrument() / 100);

            double capitalRemainingFirePower  = (openingCapital.getCapital() * configuraion.getPercentAllocationAllowed()/100) - capital ;

            double remainingFirepower = 0;

            double capitalInvestedPerInsrument = 0;

            double totalRemainingFirePower = 0;

            if(portfolioPnLService.getPositionPnLList().size() == 0)
            {
                remainingFirepower = allowedFirepower - capital;

            }
            else
            {
                capitalInvestedPerInsrument =  portfolioPnLService.getPositionPnLList().stream()
                        .filter(pnl -> pnl.getPosition().getInstrument().getName().equals(position.getInstrument().getName()))
                        .mapToDouble(pnl->pnl.getCurrentPositionEquity()).sum();

                remainingFirepower = allowedFirepower-capitalInvestedPerInsrument-capital;

                totalRemainingFirePower = portfolioPnLService.getPositionPnLList().stream()
                        .mapToDouble(pnl->pnl.getCurrentPositionEquity()).sum();

                capitalRemainingFirePower = capitalRemainingFirePower - totalRemainingFirePower;

            }

            int index = portfolioPnLService.getPositionPnLList().size();

            index++;

            MarketConditions marketConditions = positionActionsPanel.getMarketConditions();
            double percentPnl = Math.round(marketConditions.getPercentMove() * configuraion.getLev());
            double pnl =  capital * (percentPnl / 100);
            portfolioPnLService.addPositionPnL(new PositionPnL(index, position, configuraion,
                    marketConditions, percentPnl, pnl, capital, allowedFirepower ,
                    remainingFirepower,
                    capitalRemainingFirePower, 1d));

            List<PositionPnL> pnlList = portfolioPnLService.getPositionPnLList();

            model.updateData(pnlList);

            double totalPnl =  pnlList.stream().mapToDouble(p->p.getPnl()).sum() + openingCapital.getCapital();

            positionActionsPanel.getRunningCapitalPanel().getRunningCapital().setText(Double.toString(totalPnl));

        });

        positionActionsPanel.getRemovePosition().addActionListener((e) ->
        {
            try
            {
                positionPnLList.remove(table.getSelectedRow());
                model.updateData(positionPnLList);
                portfolioPnLService.updatePositionPnL(positionPnLList);
            } catch (Exception ex)
            {
                logger.error("Error while removing a row => {} ", ex.getMessage());
                JOptionPane.showMessageDialog(mainFrame, "Please choose a position to delete!", "Delete Row!", JOptionPane.ERROR_MESSAGE);
            }
        });

        positionActionsPanel.getRemoveAllPositions().addActionListener(e-> {
            try
            {
                positionPnLList.clear();
                model.updateData(positionPnLList);
                portfolioPnLService.updatePositionPnL(positionPnLList);
            } catch (Exception ex)
            {
                logger.error("Error while removing a row => {} ", ex.getMessage());
                JOptionPane.showMessageDialog(mainFrame, "Please choose a position to delete!", "Delete Row!", JOptionPane.ERROR_MESSAGE);
            }
        });
    }

    public void simulate(MarketConditions marketConditions,  List<PositionPnL> positionPnLList)
    {
        List<PositionPnL> instrumentPositions = //
                positionPnLList.stream().filter(positionPnL -> //
                positionPnL.getPosition().getInstrument().getName().equals(marketConditions.getInstrument().getName())) //
                .collect(Collectors.toList());

        for (PositionPnL instrumentPosition : instrumentPositions)
        {
            Position position =  instrumentPosition.getPosition();
            double capitalDeployed = position.getPercentCapitalDeployed();
            double pnl = instrumentPosition.getPnl();
            double pnlPercent = instrumentPosition.getPercentPnL();
            double lev = instrumentPosition.getConfiguraion().getLev();
        }

    }
}
