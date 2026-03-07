package com.hkcapital.portoflio.ui.panels.position.dialogue;

import com.hkcapital.portoflio.model.Position;
import com.hkcapital.portoflio.service.PositionService;

import javax.swing.*;
import java.awt.*;

public class PositionEditDialogue extends JDialog
{

    private final PositionService positionService;
    private final Integer id;

    private final JLabel nameLabel = new JLabel("Instrument:");
    private final JTextField name = new JTextField(20);

    private final JLabel dayLowLabel = new JLabel("Day low:");
    private final JTextField dayLow = new JTextField(20);

    private final JLabel dayHighLabel = new JLabel("Day High:");
    private final JTextField dayHigh = new JTextField(20);
    private final JLabel percentMoveLabel = new JLabel("% Move:");
    private final JTextField percentMove = new JTextField(20);
    private final JButton chooseMarketConditions = new JButton("Choose:");

    private final JLabel  supportLabel = new JLabel("Support:");
    private final JTextField support = new JTextField(20);
    private final JLabel  resistenceLabel = new JLabel("Resistance:");
    private final JTextField resistence = new JTextField(20);
    private final JLabel  timeFrameLabel = new JLabel("Timeframe:");
    private final JTextField timeFrame = new JTextField(20);
    private final JLabel  timeFrameUnitLabel = new JLabel("Timeframe:");
    private final JTextField timeFrameUnit = new JTextField(20);
    private final JButton chooseSRMatrix = new JButton("Choose");
    private final JLabel  percentAllocatedLabel = new JLabel("% Allocated:");
    private final JTextField percentAllocated = new JTextField(20);
    private final JLabel noOfInstrumentLabel = new JLabel("No of Instruments:");
    private final JTextField  noOfInstrument = new JTextField(20);

    private final JLabel noOfPositionsPerInstrumentLabel = new JLabel("No Of Positions per Instrument:");
    private final JTextField noOfPositionsPerInstrument = new JTextField(20);

    private final JLabel maxPercentAllowedPerInstrumentLabel = new JLabel("Max Percent Allowed Per instrument:");
    private final JTextField maxPercentAllowedPerInstrument = new JTextField(20);

    private final JLabel leverageLabel = new JLabel("Leverage:");
    private final JTextField   leverage = new JTextField(20);

    private final JButton chooseConfiguration = new JButton("Choose");

    private final JButton save = new JButton("Select");
    private final JButton cancel = new JButton("Cancel");

    public PositionEditDialogue(PositionService positionService,
                                Integer positionId)
    {

        this.positionService = positionService;
        this.id = positionId;
        Position position = positionService.findById(positionId);
        initializeFields(position);
        buildUI(position);
        setTitle("Change SR-Matrix and Configuration:");
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
        setResizable(true);

    }

    private void initializeFields(Position position)
    {
        //Market Conditions
        name.setText(position.getInstrument().getName());
        dayLow.setText(""+position.getMarketConditions().getDayLow());
        dayHigh.setText(""+position.getMarketConditions().getDayHigh());
        percentMove.setText(""+position.getMarketConditions().getPercentMove());
        //SR-Matrix
        support.setText(""+position.getSrMatrix().getSupport());
        resistence.setText(""+position.getSrMatrix().getResistance());
        timeFrame.setText(""+position.getSrMatrix().getTimeFrame());
        timeFrameUnit.setText(""+position.getSrMatrix().getTimeFrameUnit());
        //Configuration

        percentAllocated.setText("" + position.getConfigurtaion().getPercentAllocationAllowed());
        noOfInstrument.setText("" + position.getConfigurtaion().getNoOfInsutrments());
        noOfPositionsPerInstrument.setText("" + position.getConfigurtaion().getNoOfPositionsPerInstruments());
        maxPercentAllowedPerInstrument.setText("" + position.getConfigurtaion().getMaxPercentAllowedPerInstrument());
        leverage.setText("" + position.getConfigurtaion().getLev());

    }

    private void buildUI(Position position)
    {

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(BorderFactory.createTitledBorder("Change positions details for instrument ["+position.getInstrument().getName()+"]"));

        JPanel marketConditionsPanel = new JPanel(new GridLayout(4, 2, 5, 5));
        JPanel marketConditionsPanelButtonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT,5,5));
        JPanel marketConditionsInputsPanel = new JPanel(new GridLayout(2,3));
        marketConditionsPanel.setBorder(BorderFactory.createTitledBorder("Market Conditions:"));
        marketConditionsInputsPanel.add(nameLabel);
        marketConditionsInputsPanel.add(name);
        name.setEditable(false);
        marketConditionsInputsPanel.add(dayLowLabel);
        marketConditionsInputsPanel.add(dayLow);
        dayLow.setEditable(false);
        marketConditionsInputsPanel.add(dayHighLabel);
        marketConditionsInputsPanel.add(dayHigh);
        dayHigh.setEditable(false);
        marketConditionsInputsPanel.add(percentMoveLabel);
        marketConditionsInputsPanel.add(percentMove);
        percentMove.setEditable(false);
        if(position.getMarketConditions().getPercentMove() > 0) {
            percentMove.setBackground(Color.green);
        } else
        {
            percentMove.setBackground(Color.red);
        }
        marketConditionsPanelButtonPanel.add(chooseMarketConditions);
        marketConditionsPanel.add(marketConditionsInputsPanel, BorderLayout.CENTER);
        marketConditionsPanel.add(marketConditionsPanelButtonPanel, BorderLayout.SOUTH);

        JPanel configurationPanel = new JPanel(new GridLayout(4, 2, 5, 5));
        configurationPanel.setBorder(BorderFactory.createTitledBorder("Configuration:"));
        configurationPanel.add(percentAllocatedLabel);
        configurationPanel.add(percentAllocated);
        percentAllocated.setEditable(false);

        configurationPanel.add(noOfInstrumentLabel);
        configurationPanel.add(noOfInstrument);
        noOfInstrument.setEditable(false);


        configurationPanel.add(noOfPositionsPerInstrumentLabel);
        configurationPanel.add(noOfPositionsPerInstrument);
        noOfPositionsPerInstrument.setEditable(false);

        configurationPanel.add(maxPercentAllowedPerInstrumentLabel);
        configurationPanel.add(maxPercentAllowedPerInstrument);
        maxPercentAllowedPerInstrument.setEditable(false);

        configurationPanel.add(leverageLabel);
        configurationPanel.add(leverage);
        leverage.setEditable(false);
        configurationPanel.add(chooseConfiguration);

        JPanel srMatrixPanel = new JPanel(new GridLayout(2,2));
        srMatrixPanel.setBorder(BorderFactory.createTitledBorder("SR-Matrix:"));
        srMatrixPanel.add(supportLabel);
        srMatrixPanel.add(support);
        support.setEditable(false);
        support.setBackground(new Color(230, 240, 255));
        srMatrixPanel.add(resistenceLabel);
        srMatrixPanel.add(resistence);
        resistence.setEditable(false);
        resistence.setBackground(new Color(255, 235, 235));
        srMatrixPanel.add(timeFrameLabel);
        srMatrixPanel.add(timeFrame);
        timeFrame.setEditable(false);
        srMatrixPanel.add(timeFrameUnitLabel);
        srMatrixPanel.add(timeFrameUnit);
        timeFrameUnit.setEditable(false);
        srMatrixPanel.add(chooseSRMatrix);

        mainPanel.add(marketConditionsPanel);
        mainPanel.add(srMatrixPanel);
        mainPanel.add(configurationPanel);
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
//            strategy.setActive(active.isSelected());
//            strategy.setName(name.getText());
//            strategy.setDescription(description.getText());
//            serviceRegistry.updateStrategy(strategy);
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