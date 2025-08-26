package com.hkcapital.portoflio.ui.panels;

import com.hkcapital.portoflio.model.MarketConditions;

import javax.swing.*;
import java.awt.*;

public class SimulationActionsPanel extends JPanel {

    private final JButton simulateStrategy =  new JButton("Simulate Strategy");
    private final JButton saveStrategy =  new JButton("Save Strategy");
    private final JButton printStrategy =  new JButton("Print Strategy");

    public SimulationActionsPanel() //
    {
        setLayout(new FlowLayout(FlowLayout.LEFT, 20, 15));
        setBorder(BorderFactory.createTitledBorder("⚙ Simulation Actions"));
        add(simulateStrategy);
        add(saveStrategy);
        add(printStrategy);
    }


    public void simulate(MarketConditions mc) {
        simulateStrategy.addActionListener(e -> {
            System.out.println("Market conditions => "+mc);
        });
    }

    public JButton getSimulateStrategy()
    {
        return simulateStrategy;
    }


    public JButton getPrintStrategy()
    {
        return printStrategy;
    }
}
