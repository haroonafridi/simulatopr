package com.hkcapital.portoflio;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

public class PnLSimulationApp
{

    private static final  String GOLD = "GOLD";
    public static void main(String args[])
    {
        List<Instrument> instrumentList = Arrays.asList(new Instrument(1, "GOLD"),
                new Instrument(2, "NASDAQ"));

        Configuraion configuration = new Configuraion(1, 15, //
                2, 3, //
                7.5,
                20);

        ConfigurationTableModel model =
                new ConfigurationTableModel(new PnLSimulationApp().simulate(configuration));
        JTable table = new JTable(model);
        JFrame mainFrame = new JFrame("PnL Simulator App");
        JPanel contents = new JPanel();
        contents.setLayout(new BoxLayout(contents, BoxLayout.Y_AXIS));
        contents.setBorder(BorderFactory.createEmptyBorder(20, 200, 20, 200)); // margins

        ConfigurationPanel  configurationPanel = new ConfigurationPanel();
        contents.add(configurationPanel);
        contents.add(new JScrollPane(table));
        SimulationActionsPanel simulation = new SimulationActionsPanel();
        contents.add(simulation);
        mainFrame.setContentPane(contents);
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        mainFrame.setVisible(true);
        simulation.getSimulate().addActionListener(e -> {
            model.updateData(new PnLSimulationApp().simulate2(configurationPanel.getConfiguration()));
        });
    }


    private  List<PositionPnL> simulate( Configuraion configuration) {

        List<Instrument> instrumentList = Arrays.asList(new Instrument(1, GOLD),
                new Instrument(2, "NASDAQ"));
        List<MarketConditions> marketConditions =
                Arrays.asList(new MarketConditions(1, new Instrument(1, GOLD), -1));

        OpeningCapital openingCapital = new OpeningCapital(1, LocalDate.now(), 5165);

        Position p1Gold = new Position(1, new Instrument(1, GOLD), 3.8722);

        List<Position> positionList = Arrays.asList(p1Gold, p1Gold, p1Gold);

        PortfolioPnLSimulator portfolioPnLSimulator =
                new PortfolioPnLSimulator(instrumentList, configuration, marketConditions, positionList , openingCapital);

        return  portfolioPnLSimulator.simulate();

    }


    private  List<PositionPnL> simulate2( Configuraion configuration) {

        List<Instrument> instrumentList = Arrays.asList(new Instrument(1, GOLD),
                new Instrument(2, "NASDAQ"));
        List<MarketConditions> marketConditions =
                Arrays.asList(new MarketConditions(1, new Instrument(1, GOLD), -1));

        OpeningCapital openingCapital = new OpeningCapital(1, LocalDate.now(), 5165);

        Position p1Gold = new Position(1, new Instrument(1, GOLD), 3.8722);

        List<Position> positionList = Arrays.asList(p1Gold, p1Gold, p1Gold,
                p1Gold, p1Gold, p1Gold,
                p1Gold, p1Gold, p1Gold,
                p1Gold, p1Gold, p1Gold,
                p1Gold, p1Gold, p1Gold,
                p1Gold, p1Gold, p1Gold);

        PortfolioPnLSimulator portfolioPnLSimulator =
                new PortfolioPnLSimulator(instrumentList, configuration, marketConditions, positionList , openingCapital);

        return  portfolioPnLSimulator.simulate();

    }
}
