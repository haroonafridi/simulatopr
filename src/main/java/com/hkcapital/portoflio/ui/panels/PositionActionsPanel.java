package com.hkcapital.portoflio.ui.panels;

import javax.swing.*;
import java.awt.*;

public class PositionActionsPanel extends JPanel {

    private final JButton addPosition =  new JButton("Add Position");
    private final JButton removePosition =  new JButton("Remove Position");
    public PositionActionsPanel() {
        setLayout(new FlowLayout(FlowLayout.LEFT, 20, 15));
        setBorder(BorderFactory.createTitledBorder("Positions Panel"));
        add(addPosition);
        add(removePosition);
    }

    public JButton getAddPosition()
    {
        return addPosition;
    }


    public JButton getRemovePosition()
    {
        return removePosition;
    }
}
