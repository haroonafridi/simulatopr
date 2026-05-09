package com.hkcapital.portflio.ui.panels;

import com.hkcapital.portflio.model.MarketConditions;
import com.hkcapital.portflio.ui.UIBag;

import javax.swing.*;
import java.awt.*;

public class SimulationActionsPanel extends UIBag
{

    private final JButton simulateStrategy =  new JButton("Simulate Strategy");
    private final JButton saveStrategy =  new JButton("Save Strategy");
    private final JButton printStrategy =  new JButton("Print Strategy");

    public SimulationActionsPanel() //
    {
        super(SimulationActionsPanel.class);
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

    public JButton getSaveStrategy()
    {
        return saveStrategy;
    }
}
