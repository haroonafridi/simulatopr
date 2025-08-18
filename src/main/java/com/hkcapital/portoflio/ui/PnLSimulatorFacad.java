package com.hkcapital.portoflio.ui;

import com.hkcapital.portoflio.PnLSimulationApp;
import com.hkcapital.portoflio.model.*;
import com.hkcapital.portoflio.service.PortfolioPnLService;
import com.hkcapital.portoflio.simulation.SimulationData;
import com.hkcapital.portoflio.ui.panels.PositionActionsPanel;
import com.hkcapital.portoflio.ui.panels.SimulationActionsPanel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
        List<PositionPnL> positionPnLList = new ArrayList<>(); ///portfolioPnLService.simulate();

        ConfigurationTableModel model = new ConfigurationTableModel(positionPnLList);

        JTable table = new JTable(model);
        table.setRowHeight(25);
        table.setFont(new Font("SansSerif", Font.PLAIN, 14));
        JFrame mainFrame = new JFrame("PnL Simulator App");
        JPanel contents = new JPanel();
        contents.setLayout(new BoxLayout(contents, BoxLayout.Y_AXIS));
        contents.setBorder(BorderFactory.createEmptyBorder(20, 200, 20, 200)); // margins

        ConfigurationPanel configurationPanel = new ConfigurationPanel();
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
                model.updateData(positionPnLList);
            } catch (Exception ex)
            {
                JOptionPane.showMessageDialog(mainFrame, "Please check configuration!",
                        "Configuration Error!",
                        JOptionPane.ERROR_MESSAGE);
            }

        });

        positionActionsPanel.getAddPosition().addActionListener(e ->
        {
            Position position = positionActionsPanel.getPosition();
            Configuraion configuraion = configurationPanel.getConfiguration();
            int index = portfolioPnLService.getPositionPnLList().size();
            index++;
            portfolioPnLService.addPositionPnL(new PositionPnL(index, position, configuraion,
                    SimulationData.MARKET_CONDITION, 1, 1, 1, 1));
            List<PositionPnL> pnl = portfolioPnLService.getPositionPnLList();
            model.updateData(pnl);
        });

        positionActionsPanel.getRemovePosition().addActionListener((e) ->
        {
            try
            {
                positionPnLList.remove(table.getSelectedRow());
                model.updateData(positionPnLList);
            } catch (Exception ex)
            {
                logger.error("Error while removing a row => {} ", ex.getMessage());
                JOptionPane.showMessageDialog(mainFrame, "Please choose a position to delete!", "Delete Row!", JOptionPane.ERROR_MESSAGE);
            }
        });
    }
}
