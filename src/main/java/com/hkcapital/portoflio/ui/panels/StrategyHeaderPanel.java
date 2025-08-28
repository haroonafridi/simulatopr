package com.hkcapital.portoflio.ui.panels;

import com.hkcapital.portoflio.model.OpeningCapital;
import com.hkcapital.portoflio.model.Strategy;
import com.hkcapital.portoflio.ui.fields.NumberTextField;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class StrategyHeaderPanel extends JPanel {

    private final JLabel strategyNameLabel = new JLabel("Strategy Name:");
    private  final JTextField strategyName = new JTextField(20);
    private final JLabel strategyDescriptionLabel = new JLabel("Strategy Description:");

    private  final JTextField strategyDescription = new JTextField(100);


    public StrategyHeaderPanel()
    {

        setLayout(new FlowLayout(FlowLayout.LEFT, 10,10));
        setBorder(BorderFactory.createTitledBorder("⚙ Strategy Details"));
        add(strategyNameLabel);
        add(strategyName);
        add(strategyDescriptionLabel);
        add(strategyDescription);
    }

    public Strategy getStrategy()
    {
        return new Strategy(strategyName.getText(), strategyDescription.getText(), LocalDateTime.now());
    }
}
