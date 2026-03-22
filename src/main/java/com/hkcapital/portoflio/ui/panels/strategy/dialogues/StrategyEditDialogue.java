package com.hkcapital.portoflio.ui.panels.strategy.dialogues;

import com.hkcapital.portoflio.model.Strategy;
import com.hkcapital.portoflio.service.StrategyService;
import com.hkcapital.portoflio.ui.fields.NumberTextField;

import javax.swing.*;
import java.awt.*;

public class StrategyEditDialogue extends JDialog
{

    private final StrategyService strategyService;
    private final Integer strategyId;
    private final Strategy strategy;

    private final JLabel nameLabel = new JLabel("Name:");
    private final JTextField name = new JTextField(20);
    private final JTextField strategyDescription = new JTextField(40);
    private final JLabel capitalAllocatedLabel = new JLabel("Capital Allocated");
    private final JTextField capitalAllocated = new NumberTextField(40);
    private final JLabel descriptionLabel = new JLabel("Description:");
    private final JTextArea description = new JTextArea(10, 80);
    private final JCheckBox active = new JCheckBox("Active");
    private final JButton save = new JButton("Save");
    private final JButton cancel = new JButton("Cancel");

    public StrategyEditDialogue(StrategyService strategyService,
                                Integer strategyId)
    {

        this.strategyService = strategyService;
        this.strategyId = strategyId;
        this.strategy = strategyService.findById(strategyId);
        initializeFields();
        buildUI();
        setTitle("Strategy for [" +
                strategy.getName() + "]");
        pack();
        setResizable(true);
        setVisible(true);
    }

    private void initializeFields()
    {
        name.setText(strategy.getName());
        description.setText(String.valueOf(strategy.getDescription()));
        active.setSelected(strategy.getActive());
    }

    private void buildUI()
    {

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        mainPanel.add(nameLabel);
        mainPanel.add(name);
        mainPanel.add(capitalAllocatedLabel);
        mainPanel.add(capitalAllocated);
        mainPanel.add(Box.createVerticalStrut(8));
        mainPanel.add(Box.createVerticalStrut(8));
        mainPanel.add(Box.createVerticalStrut(8));
        mainPanel.add(descriptionLabel);
        mainPanel.add(description);
        mainPanel.add(Box.createVerticalStrut(8));
        mainPanel.add(active);
        mainPanel.add(Box.createVerticalStrut(12));
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.add(save);
        buttonPanel.add(cancel);
        mainPanel.add(buttonPanel);
        save.addActionListener(e -> save());
        cancel.addActionListener(e -> dispose());
        setLayout(new BorderLayout());
        add(mainPanel, BorderLayout.CENTER);
    }

    private void save()
    {
        try
        {
            strategy.setActive(active.isSelected());
            strategy.setName(name.getText());
            strategy.setDescription(description.getText());
            strategyService.updateStrategy(strategy);
            dispose();

        } catch (NumberFormatException ex)
        {
            JOptionPane.showMessageDialog(
                    this,
                    "Please enter valid numeric values.",
                    "Validation Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }
}