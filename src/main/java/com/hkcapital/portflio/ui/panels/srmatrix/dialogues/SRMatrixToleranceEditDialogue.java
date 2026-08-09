package com.hkcapital.portflio.ui.panels.srmatrix.dialogues;

import com.hkcapital.portflio.market.indicators.TimeFramesUnit;
import com.hkcapital.portflio.model.SRMatrix;
import com.hkcapital.portflio.model.SRMatrixTolerance;
import com.hkcapital.portflio.service.srmatrix.SRMatrixService;
import com.hkcapital.portflio.service.srmatrix.SRMatrixToleranceService;
import com.hkcapital.portflio.ui.fields.NumberTextField;

import javax.swing.*;
import java.awt.*;
import java.time.format.DateTimeFormatter;

public class SRMatrixToleranceEditDialogue extends JDialog {
    private final SRMatrixToleranceService sRMatrixToleranceService;
    private final Integer srMatrixToleranceId;
    private final SRMatrixTolerance sRMatrixTolerance;
    private final JLabel instrumentNameLabel = new JLabel("Name:");
    private final JTextField instrumentName = new JTextField(20);
    private final JLabel creationDateLabel = new JLabel("Creation Date:");
    private final JTextField creationDate = new JTextField(30);
    private final JLabel lSupportToleranceLabel = new JLabel("L.Support Tolerance.");
    private final NumberTextField lSupportTolerance = new NumberTextField(40, 0);
    private final JLabel rSupportToleranceLabel = new JLabel("R.Support Tolerance.");
    private final NumberTextField rSupportTolerance = new NumberTextField(40, 0);

    private final JLabel resistanceLabel = new JLabel("Resistance:");
    private final JLabel lResistanceToleranceLabel = new JLabel("L.Resistance Tolerance.");
    private final NumberTextField lResistanceTolerance = new NumberTextField(40, 0);
    private final JLabel rResistanceToleranceLabel = new JLabel("R.Resistance Tolerance.");
    private final NumberTextField rResistanceTolerance = new NumberTextField(40, 0);

    private final JLabel takeProfitPercentLabel = new JLabel("Take Profit %");
    private final NumberTextField takeProfitPercent = new NumberTextField(40, 0);
    private final JLabel stopLossPercentLabel = new JLabel("Take Profit %");
    private final NumberTextField stopLossPercent = new NumberTextField(40, 0);
    private final JLabel timeFrameLabel = new JLabel("TimeFrame:");
    private final JTextField timeFrame = new NumberTextField(30);


    private final JComboBox<String> timeFrameUnit =
            new JComboBox<>(new String[]{TimeFramesUnit.MINUTE.getUnit(),
                    TimeFramesUnit.HOUR.getUnit(),
                    TimeFramesUnit.DAY.getUnit(),
                    TimeFramesUnit.WEEK.getUnit()});

    private final JCheckBox active = new JCheckBox("Active");

    private final JButton save = new JButton("Save");
    private final JButton cancel = new JButton("Cancel");

    public SRMatrixToleranceEditDialogue(SRMatrixToleranceService sRMatrixToleranceService,
                                         Integer srMatrixToleranceId) {

        this.sRMatrixToleranceService = sRMatrixToleranceService;
        this.srMatrixToleranceId = srMatrixToleranceId;
        this.sRMatrixTolerance = sRMatrixToleranceService.findById(srMatrixToleranceId);
        initializeFields();
        buildUI();
        setTitle("SR-Matrix Tolerance for [" +
                sRMatrixTolerance.getInstrument().getName() +
                "] TimeFrame = [" +
                sRMatrixTolerance.getTimeFrame() + "-" +
                sRMatrixTolerance.getTimeFrameUnit() + "]");

        pack();
        setLocationRelativeTo(null);
        setResizable(false);
        setVisible(true);
    }

    private void initializeFields() {

        instrumentName.setText(sRMatrixTolerance.getInstrument().getName());
        instrumentName.setEnabled(false);

        creationDate.setText(
                sRMatrixTolerance.getCreationDate()
                        .format(DateTimeFormatter.ofPattern("d MMM uuuu"))
        );
        creationDate.setEnabled(false);

        timeFrame.setText(String.valueOf(sRMatrixTolerance.getTimeFrame()));
        lResistanceTolerance.setText(String.valueOf(sRMatrixTolerance.getL_r_tolerance_percent()));
        rResistanceTolerance.setText(String.valueOf(sRMatrixTolerance.getR_r_tolerance_percent()));
        lSupportTolerance.setText(String.valueOf(sRMatrixTolerance.getL_s_tolerance_percent()));
        rSupportTolerance.setText(String.valueOf(sRMatrixTolerance.getL_s_tolerance_percent()));
        takeProfitPercent.setText(String.valueOf(sRMatrixTolerance.getTakeProfitPercent()));
        stopLossPercent.setText(String.valueOf(sRMatrixTolerance.getStopLossPercent()));
        timeFrameUnit.setSelectedItem(sRMatrixTolerance.getTimeFrameUnit());
        active.setSelected(sRMatrixTolerance.getActive());
    }

    private void buildUI() {

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        mainPanel.add(instrumentNameLabel);
        mainPanel.add(instrumentName);
        mainPanel.add(Box.createVerticalStrut(8));
        mainPanel.add(creationDateLabel);
        mainPanel.add(creationDate);
        mainPanel.add(Box.createVerticalStrut(8));
        mainPanel.add(lSupportToleranceLabel);
        mainPanel.add(lSupportTolerance);
        mainPanel.add(rSupportToleranceLabel);
        mainPanel.add(rSupportTolerance);
        mainPanel.add(Box.createVerticalStrut(8));
        mainPanel.add(lResistanceToleranceLabel);
        mainPanel.add(lResistanceTolerance);
        mainPanel.add(rResistanceToleranceLabel);
        mainPanel.add(rResistanceTolerance);
        mainPanel.add(takeProfitPercentLabel);
        mainPanel.add(takeProfitPercent);
        mainPanel.add(stopLossPercentLabel);
        mainPanel.add(stopLossPercent);
        mainPanel.add(Box.createVerticalStrut(8));
        mainPanel.add(timeFrameLabel);
        mainPanel.add(timeFrame);
        mainPanel.add(timeFrameUnit);
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

    private void save() {
        try {
            sRMatrixTolerance.setActive(active.isSelected());
            sRMatrixTolerance.setL_r_tolerance_percent(Double.parseDouble(lResistanceTolerance.getText()));
            sRMatrixTolerance.setR_r_tolerance_percent(Double.parseDouble(rResistanceTolerance.getText()));
            sRMatrixTolerance.setL_s_tolerance_percent(Double.parseDouble(lSupportTolerance.getText()));
            sRMatrixTolerance.setR_s_tolerance_percent(Double.parseDouble(rSupportTolerance.getText()));
            sRMatrixTolerance.setTakeProfitPercent(Double.parseDouble(takeProfitPercent.getText()));
            sRMatrixTolerance.setStopLossPercent(Double.parseDouble(stopLossPercent.getText()));
            sRMatrixTolerance.setTimeFrame(Integer.parseInt(timeFrame.getText()));
            sRMatrixTolerance.setTimeFrameUnit(timeFrameUnit.getSelectedItem().toString());
            sRMatrixToleranceService.updateSRMatrixTolerance(sRMatrixTolerance);
            dispose();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(
                    this,
                    "Please enter valid numeric values.",
                    "Validation Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }
}