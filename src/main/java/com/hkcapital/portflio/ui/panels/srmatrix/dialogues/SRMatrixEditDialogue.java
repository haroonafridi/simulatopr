package com.hkcapital.portflio.ui.panels.srmatrix.dialogues;

import com.hkcapital.portflio.market.indicators.TimeFramesUnit;
import com.hkcapital.portflio.model.SRMatrix;
import com.hkcapital.portflio.service.srmatrix.SRMatrixService;
import com.hkcapital.portflio.ui.fields.NumberTextField;

import javax.swing.*;
import java.awt.*;
import java.time.format.DateTimeFormatter;

public class SRMatrixEditDialogue extends JDialog {

    private final SRMatrixService srMatrixService;
    private final Integer smMatrixId;
    private final SRMatrix srMatrix;

    private final JLabel instrumentNameLabel = new JLabel("Name:");
    private final JTextField instrumentName = new JTextField(20);

    private final JLabel creationDateLabel = new JLabel("Creation Date:");
    private final JTextField creationDate = new JTextField(30);

    private final JLabel supportLabel = new JLabel("Support:");
    private final JTextField support = new NumberTextField(30);

    private final JLabel lSupportToleranceLabel = new JLabel("L.Support Tolerance.");
    private final NumberTextField lSupportTolerance = new NumberTextField(40, 0);
    private final JLabel rSupportToleranceLabel = new JLabel("R.Support Tolerance.");
    private final NumberTextField rSupportTolerance = new NumberTextField(40, 0);

    private final JLabel resistanceLabel = new JLabel("Resistance:");
    private final JTextField resistance = new NumberTextField(30);

    private final JLabel lResistanceToleranceLabel = new JLabel("L.Resistance Tolerance.");
    private final NumberTextField lResistanceTolerance = new NumberTextField(40, 0);
    private final JLabel rResistanceToleranceLabel = new JLabel("R.Resistance Tolerance.");
    private final NumberTextField rResistanceTolerance = new NumberTextField(40, 0);
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

    public SRMatrixEditDialogue(SRMatrixService srMatrixService,
                                Integer smMatrixId) {

        //super(owner, "", true); // modal dialog

        this.srMatrixService = srMatrixService;
        this.smMatrixId = smMatrixId;

        this.srMatrix = srMatrixService.findById(smMatrixId);

        initializeFields();
        buildUI();

        setTitle("SR-Matrix for [" +
                srMatrix.getInstrument().getName() +
                "] TimeFrame = [" +
                srMatrix.getTimeFrame() + "-" +
                srMatrix.getTimeFrameUnit() + "]");

        pack();
        setLocationRelativeTo(null);
        setResizable(false);
        setVisible(true);
    }

    private void initializeFields() {

        instrumentName.setText(srMatrix.getInstrument().getName());
        instrumentName.setEnabled(false);

        creationDate.setText(
                srMatrix.getCreationDate()
                        .format(DateTimeFormatter.ofPattern("d MMM uuuu"))
        );
        creationDate.setEnabled(false);

        support.setText(String.valueOf(srMatrix.getSupport()));
        resistance.setText(String.valueOf(srMatrix.getResistance()));
        timeFrame.setText(String.valueOf(srMatrix.getTimeFrame()));

        timeFrameUnit.setSelectedItem(srMatrix.getTimeFrameUnit());
        active.setSelected(srMatrix.getActive());
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
        mainPanel.add(supportLabel);
        mainPanel.add(support);
        mainPanel.add(rSupportToleranceLabel);
        mainPanel.add(rSupportTolerance);
        mainPanel.add(Box.createVerticalStrut(8));
        mainPanel.add(lResistanceToleranceLabel);
        mainPanel.add(lResistanceTolerance);
        mainPanel.add(resistanceLabel);
        mainPanel.add(resistance);
        mainPanel.add(rResistanceToleranceLabel);
        mainPanel.add(rResistanceTolerance);
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
            srMatrix.setActive(active.isSelected());
            srMatrix.setResistance(Double.parseDouble(resistance.getText()));
            srMatrix.setL_r_tolerance(Double.parseDouble(lResistanceTolerance.getText()));
            srMatrix.setR_r_tolerance(Double.parseDouble(rResistanceTolerance.getText()));
            srMatrix.setSupport(Double.parseDouble(support.getText()));
            srMatrix.setL_s_tolerance(Double.parseDouble(lSupportTolerance.getText()));
            srMatrix.setR_s_tolerance(Double.parseDouble(rSupportTolerance.getText()));
            srMatrix.setTimeFrame(Integer.parseInt(timeFrame.getText()));
            srMatrix.setTimeFrameUnit(timeFrameUnit.getSelectedItem().toString());
            srMatrixService.updateSRMatrix(srMatrix);
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