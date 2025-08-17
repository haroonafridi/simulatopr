package com.hkcapital.portoflio;

import javax.swing.*;
import java.awt.*;

public class SimulationActionsPanel extends JPanel {

    private final JButton simulate =  new JButton("Simulate");
    public SimulationActionsPanel() {
        setLayout(new FlowLayout(FlowLayout.LEFT, 20, 15));
        setBorder(BorderFactory.createTitledBorder("⚙ Simulation Actions"));
        add(simulate);
    }

    public JButton getSimulate()
    {
        return simulate;
    }
}
