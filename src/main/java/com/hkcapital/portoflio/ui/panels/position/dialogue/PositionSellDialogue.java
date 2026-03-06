package com.hkcapital.portoflio.ui.panels.position.dialogue;

import com.hkcapital.portoflio.model.Position;
import com.hkcapital.portoflio.service.PositionService;

import javax.swing.*;
import java.awt.*;

public class PositionSellDialogue extends JDialog
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

    private final JLabel  supportLabel = new JLabel("Support:");
    private final JTextField support = new JTextField(20);
    private final JLabel  resistenceLabel = new JLabel("Resistance:");
    private final JTextField resistence = new JTextField(20);
    private final JLabel  timeFrameLabel = new JLabel("Timeframe:");
    private final JTextField timeFrame = new JTextField(20);
    private final JLabel  timeFrameUnitLabel = new JLabel("Timeframe:");
    private final JTextField timeFrameUnit = new JTextField(20);
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


    private final JButton buy = new JButton("Place Sell Order");
    private final JButton cancel = new JButton("Cancel");

    public PositionSellDialogue(PositionService positionService,
                                Integer positionId)
    {

        this.positionService = positionService;
        this.id = positionId;
        Position position = positionService.findById(positionId);
        initializeFields(position);
        buildUI(position);
        setTitle("Placing  ["+position.getInstrument().getName()+"] sell order!");
        pack();
        setLocationRelativeTo(null);
        setResizable(true);
        setVisible(true);
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
        mainPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JPanel marketConditionsPanel = new JPanel();
        marketConditionsPanel.setBorder(BorderFactory.createTitledBorder("Market Conditions:"));
        marketConditionsPanel.add(nameLabel);
        marketConditionsPanel.add(name);
        name.setEnabled(false);
        marketConditionsPanel.add(dayLowLabel);
        marketConditionsPanel.add(dayLow);
        dayLow.setEnabled(false);
        marketConditionsPanel.add(dayHighLabel);
        marketConditionsPanel.add(dayHigh);
        dayHigh.setEnabled(false);
        marketConditionsPanel.add(percentMoveLabel);
        marketConditionsPanel.add(percentMove);
        percentMove.setEnabled(false);
        JPanel configurationPanel = new JPanel();
        configurationPanel.add(percentAllocatedLabel);
        configurationPanel.add(percentAllocated);
        percentAllocated.setEnabled(false);

        configurationPanel.add(noOfInstrumentLabel);
        configurationPanel.add(noOfInstrument);
        noOfInstrument.setEnabled(false);


        configurationPanel.add(noOfPositionsPerInstrumentLabel);
        configurationPanel.add(noOfPositionsPerInstrument);
        noOfPositionsPerInstrument.setEnabled(false);

        configurationPanel.add(maxPercentAllowedPerInstrumentLabel);
        configurationPanel.add(maxPercentAllowedPerInstrument);
        maxPercentAllowedPerInstrument.setEnabled(false);

        configurationPanel.add(leverageLabel);
        configurationPanel.add(leverage);
        leverage.setEnabled(false);
        JPanel srMatrixPanel = new JPanel();
        srMatrixPanel.setBorder(BorderFactory.createTitledBorder("SR-Matrix:"));
        srMatrixPanel.add(supportLabel);
        srMatrixPanel.add(support);
        support.setEnabled(false);
        srMatrixPanel.add(resistenceLabel);
        srMatrixPanel.add(resistence);
        resistence.setEnabled(false);
        srMatrixPanel.add(timeFrameLabel);
        srMatrixPanel.add(timeFrame);
        timeFrame.setEnabled(false);
        srMatrixPanel.add(timeFrameUnitLabel);
        srMatrixPanel.add(timeFrameUnit);
        timeFrameUnit.setEnabled(false);

        mainPanel.add(marketConditionsPanel);
        mainPanel.add(srMatrixPanel);
        mainPanel.add(configurationPanel);
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.add(buy);
        buttonPanel.add(cancel);
        mainPanel.add(buttonPanel);
        buy.addActionListener(e -> save());
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