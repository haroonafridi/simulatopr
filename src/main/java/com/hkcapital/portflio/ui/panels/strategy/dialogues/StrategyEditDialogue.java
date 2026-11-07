
package com.hkcapital.portflio.ui.panels.strategy.dialogues;

import com.hkcapital.portflio.model.Strategy;
import com.hkcapital.portflio.service.strategy.StrategyService;
import com.hkcapital.portflio.ui.fields.NumberTextField;

import javax.swing.*;
import java.awt.*;

public class StrategyEditDialogue extends JDialog {

    private final StrategyService strategyService;
    private final Strategy strategy;

    // Header fields
    private final JLabel nameLabel = new JLabel("Strategy Name:");
    private final JTextField name = new JTextField(25);

    private final JLabel strategyDescriptionLabel =
            new JLabel("Strategy Description:");

    private final JTextField strategyDescription =
            new JTextField(25);

    private final JLabel capitalAllocatedLabel =
            new JLabel("Capital Allocated:");

    private final NumberTextField capitalAllocated =
            new NumberTextField(20);

    // Strategy options
    private final JCheckBox active = new JCheckBox("Active");
    private final JCheckBox dynamic = new JCheckBox("Dynamic");

    // Main description
    private final JLabel descriptionLabel =
            new JLabel("Description:");

    private final JTextArea description =
            new JTextArea(10, 60);

    // Action buttons
    private final JButton save = new JButton("Save");
    private final JButton cancel = new JButton("Cancel");


    public StrategyEditDialogue(
            StrategyService strategyService,
            Integer strategyId) {

        this.strategyService = strategyService;
        this.strategy = strategyService.findById(strategyId);

        initializeFields();
        buildUI();

        setTitle("Edit Strategy - " + strategy.getName());
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setModal(true);
        setResizable(true);
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }


    // --------------------------------------------------
    // Initialize Fields
    // --------------------------------------------------

    private void initializeFields() {

        name.setText(strategy.getName());

        strategyDescription.setText(
                strategy.getDescription() != null
                        ? strategy.getDescription()
                        : ""
        );

        description.setText(
                String.valueOf(strategy.getDescription())
        );

        capitalAllocated.setText(
                strategy.getCapitalAllocated() != null
                        ? strategy.getCapitalAllocated().toString()
                        : ""
        );

        active.setSelected(strategy.getActive());
        dynamic.setSelected(strategy.getDynamic());

        description.setLineWrap(true);
        description.setWrapStyleWord(true);
    }


    // --------------------------------------------------
    // Build User Interface
    // --------------------------------------------------

    private void buildUI() {

        JPanel mainPanel = new JPanel(new BorderLayout(10, 15));

        mainPanel.setBorder(
                BorderFactory.createEmptyBorder(20, 20, 20, 20)
        );

        // Header panel
        JPanel headerPanel = new JPanel(new GridBagLayout());

        headerPanel.setBorder(
                BorderFactory.createTitledBorder("Strategy Details")
        );

        GridBagConstraints gbc = new GridBagConstraints();

        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;

        // ----------------------------------------------
        // HEADER ROW 1
        // Strategy Name | Strategy Description
        // ----------------------------------------------

        gbc.gridy = 0;

        gbc.gridx = 0;
        gbc.weightx = 0;
        headerPanel.add(nameLabel, gbc);

        gbc.gridx = 1;
        gbc.weightx = 1;
        headerPanel.add(name, gbc);

        gbc.gridx = 2;
        gbc.weightx = 0;
        headerPanel.add(strategyDescriptionLabel, gbc);

        gbc.gridx = 3;
        gbc.weightx = 1;
        headerPanel.add(strategyDescription, gbc);


        // ----------------------------------------------
        // HEADER ROW 2
        // Capital Allocated | Active | Dynamic
        // ----------------------------------------------

        gbc.gridy = 1;

        gbc.gridx = 0;
        gbc.weightx = 0;
        headerPanel.add(capitalAllocatedLabel, gbc);

        gbc.gridx = 1;
        gbc.weightx = 1;
        headerPanel.add(capitalAllocated, gbc);

        gbc.gridx = 2;
        gbc.weightx = 0;
        headerPanel.add(active, gbc);

        gbc.gridx = 3;
        gbc.weightx = 0;
        headerPanel.add(dynamic, gbc);


        // ----------------------------------------------
        // DESCRIPTION AREA
        // ----------------------------------------------

        JPanel descriptionPanel = new JPanel(new BorderLayout(0, 8));

        descriptionPanel.add(
                descriptionLabel,
                BorderLayout.NORTH
        );

        JScrollPane descriptionScrollPane =
                new JScrollPane(description);

        descriptionScrollPane.setPreferredSize(
                new Dimension(650, 220)
        );

        descriptionPanel.add(
                descriptionScrollPane,
                BorderLayout.CENTER
        );


        // ----------------------------------------------
        // BUTTON PANEL
        // ----------------------------------------------

        JPanel buttonPanel = new JPanel(
                new FlowLayout(FlowLayout.RIGHT, 10, 0)
        );

        save.setPreferredSize(new Dimension(100, 32));
        cancel.setPreferredSize(new Dimension(100, 32));

        buttonPanel.add(cancel);
        buttonPanel.add(save);


        // ----------------------------------------------
        // ASSEMBLE MAIN PANEL
        // ----------------------------------------------

        JPanel centerPanel = new JPanel(new BorderLayout(0, 20));

        centerPanel.add(headerPanel, BorderLayout.NORTH);
        centerPanel.add(descriptionPanel, BorderLayout.CENTER);

        mainPanel.add(centerPanel, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        // Events
        save.addActionListener(e -> save());
        cancel.addActionListener(e -> dispose());

        getRootPane().setDefaultButton(save);

        add(mainPanel, BorderLayout.CENTER);
    }


    // --------------------------------------------------
    // Save Strategy
    // --------------------------------------------------

    private void save() {

        try {

            if (name.getText().trim().isEmpty()) {

                JOptionPane.showMessageDialog(
                        this,
                        "Strategy name cannot be empty.",
                        "Validation Error",
                        JOptionPane.ERROR_MESSAGE
                );

                name.requestFocusInWindow();
                return;
            }

            strategy.setName(name.getText().trim());

            strategy.setDescription(description.getText());

            strategy.setCapitalAllocated(
                    capitalAllocated.isNotNull()
                            ? capitalAllocated.getDoubleValue()
                            : null
            );

            strategy.setActive(active.isSelected());
            strategy.setDynamic(dynamic.isSelected());

            strategyService.updateStrategy(strategy);

            dispose();

        } catch (NumberFormatException ex) {

            JOptionPane.showMessageDialog(
                    this,
                    "Please enter a valid numeric value.",
                    "Validation Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }
}
