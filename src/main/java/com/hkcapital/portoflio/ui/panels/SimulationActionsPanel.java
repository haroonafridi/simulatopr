package com.hkcapital.portoflio.ui.panels;

import com.hkcapital.portoflio.model.MarketConditions;

import javax.swing.*;
import java.awt.*;

public class SimulationActionsPanel extends JPanel {

    private final JButton simulate =  new JButton("Simulate");
    private final JButton strategy =  new JButton("Print Strategy");

    public SimulationActionsPanel() //
    {
        setLayout(new FlowLayout(FlowLayout.LEFT, 20, 15));
        setBorder(BorderFactory.createTitledBorder("⚙ Simulation Actions"));
        add(simulate);
        add(strategy);
    }


    public void simulate(MarketConditions mc) {
        simulate.addActionListener(e -> {
            System.out.println("Market conditions => "+mc);
        });
    }

    public JButton getSimulate()
    {
        return simulate;
    }


    public JButton getStrategy()
    {
        return strategy;
    }
}
