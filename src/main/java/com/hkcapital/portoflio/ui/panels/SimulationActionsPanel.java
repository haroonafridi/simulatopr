package com.hkcapital.portoflio.ui.panels;

import javax.swing.*;
import java.awt.*;

public class SimulationActionsPanel extends JPanel {

    private final JButton simulate =  new JButton("Simulate");
    private final JButton strategy =  new JButton("Print Strategy");
    public SimulationActionsPanel() {
        setLayout(new FlowLayout(FlowLayout.LEFT, 20, 15));
        setBorder(BorderFactory.createTitledBorder("⚙ Simulation Actions"));
        add(simulate);
        add(strategy);
    }

    public JButton getSimulate()
    {
        return simulate;
    }


    public JButton getStrategye()
    {
        return strategy;
    }
}
