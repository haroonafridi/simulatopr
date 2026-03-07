package com.hkcapital.portoflio.ui.panels.position.dialogue;

import com.hkcapital.portoflio.etoro.dto.order.EtoroMarketOrderDto;
import com.hkcapital.portoflio.etoro.master.Instruments;
import com.hkcapital.portoflio.model.Position;
import com.hkcapital.portoflio.order.OderTypes;
import com.hkcapital.portoflio.repository.ServiceRegistery;
import com.hkcapital.portoflio.service.OrderManagerService;
import com.hkcapital.portoflio.service.PositionService;
import com.hkcapital.portoflio.service.Service;

import javax.swing.*;
import java.awt.*;

public class PositionBuyDialogue extends JDialog
{

    private final PositionService positionService;
    private final Integer positionId;

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
    private final JButton buy = new JButton("Place Buy Order");
    private final JButton cancel = new JButton("Cancel");

    private final ServiceRegistery<Service> serviceRegistery;

    private OrderManagerService orderManagerService;

    public PositionBuyDialogue(final ServiceRegistery<Service> serviceRegistery,
                               Integer positionId)
    {

        this.serviceRegistery = serviceRegistery;
        this.positionService = (PositionService)serviceRegistery.getService(Service.PositionService);
        this.orderManagerService = (OrderManagerService) serviceRegistery.getService(Service.OrderManagerService);
        this.positionId = positionId;
        Position position = positionService.findById(positionId);
        initializeFields(position);
        buildUI(position);
        setTitle("Placing  ["+position.getInstrument().getName()+"] buy order!");
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
        setModal(true);
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
        //mainPanel.setBorder(BorderFactory.createTitledBorder("Manual Sell Order Screen : ["+position.getInstrument().getName()+"]"));
        JPanel marketConditionsPanel = new JPanel(new GridLayout(2,2));
        marketConditionsPanel.setBorder(BorderFactory.createTitledBorder("Market Conditions:"));
        marketConditionsPanel.add(nameLabel);
        marketConditionsPanel.add(name);
        name.setEditable(false);
        marketConditionsPanel.add(dayLowLabel);
        marketConditionsPanel.add(dayLow);
        dayLow.setEditable(false);
        marketConditionsPanel.add(dayHighLabel);
        marketConditionsPanel.add(dayHigh);
        dayHigh.setEditable(false);
        marketConditionsPanel.add(percentMoveLabel);
        marketConditionsPanel.add(percentMove);
        percentMove.setEditable(false);
        if(position.getMarketConditions().getPercentMove() > 0) {
            percentMove.setBackground(Color.green);
        } else
        {
            percentMove.setBackground(Color.red);
        }
        JPanel configurationPanel = new JPanel(new GridLayout(3,2));
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
        JPanel srMatrixPanel = new JPanel(new GridLayout(2,3));
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

        mainPanel.add(marketConditionsPanel);
        mainPanel.add(srMatrixPanel);
        mainPanel.add(configurationPanel);
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.add(buy);
        buttonPanel.add(cancel);
        mainPanel.add(buttonPanel);
        buy.addActionListener(e -> createBuyOrder());
        cancel.addActionListener(e -> dispose());
        buy.setBackground(new Color(200,60,60));
        buy.setForeground(Color.WHITE);
        setLayout(new BorderLayout());
        add(mainPanel, BorderLayout.CENTER);
    }

    private void createBuyOrder()
    {
        createMarketOrder();
        dispose();
    }


    public void createMarketOrder()
    {
        Position position = positionService.findById(positionId);
        EtoroMarketOrderDto etoroMarketOrderDto = new EtoroMarketOrderDto(position.getInstrument().getEtoroInstrumentId(),
                true, //
                position.getConfigurtaion().getLev(), //
                position.getAllowedFirePower(), //
                null, //
                null, //
                null, //
                null, //
                null,
                OderTypes.MANUAL.getOrderType(),
                null,
                null,
                null,
                null);
        orderManagerService.createAndSaveMarketOrder(etoroMarketOrderDto);
    }
}