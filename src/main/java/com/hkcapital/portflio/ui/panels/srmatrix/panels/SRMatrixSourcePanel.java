package com.hkcapital.portflio.ui.panels.srmatrix.panels;

import com.hkcapital.portflio.ui.UIBag;
import com.hkcapital.portflio.ui.fields.NumberTextField;
import com.hkcapital.portflio.ui.panels.srmatrix.labels.Labels;

import javax.swing.*;
import java.awt.*;

public class SRMatrixSourcePanel extends UIBag
{
    private final JLabel idLabel = new JLabel("Id:");
    private final NumberTextField id = new NumberTextField(30);
    private final JLabel supportLabel = new JLabel(Labels.Support.getLabel());
    private final NumberTextField support = new NumberTextField(80);

    private final JLabel lSupportToleranceLabel = new JLabel(Labels.LSupportTolerance.getLabel());
    private final NumberTextField lSupportTolerance = new NumberTextField(80, 0);

    private final JLabel rSupportToleranceLabel = new JLabel(Labels.RSupportTolerance.getLabel());
    private final NumberTextField rSupportTolerance = new NumberTextField(80, 0);
    private final JLabel resistanceLabel = new JLabel(Labels.Resistance.getLabel());
    private final NumberTextField resistance = new NumberTextField(80);

    private final JLabel lResistanceToleranceLabel = new JLabel(Labels.LResistanceTolerance.getLabel());
    private final NumberTextField lResistanceTolerance = new NumberTextField(80, 0);
    private final JLabel rResistanceToleranceLabel = new JLabel(Labels.RResistanceTolerance.getLabel());
    private final NumberTextField rResistanceTolerance = new NumberTextField(80, 0);
    private final JLabel timeFrameLabel = new JLabel(Labels.TimeFrame.getLabel());
    private final NumberTextField timeFrame = new NumberTextField(80);
    private final JLabel timeFrameUnitLabel = new JLabel(Labels.TimeFrameUnit.getLabel());
    private JTextField timeFrameUnit = new JTextField(10);

    private final JLabel activeLabel = new JLabel("Active");
    private final JCheckBox active = new JCheckBox();

    public SRMatrixSourcePanel()
    {
        super(SRMatrixSourcePanel.class);
        // this.srMatrixService = srMatrixService;
        setLayout(new GridBagLayout());
        setBorder(BorderFactory.createTitledBorder(Labels.SRMatrix.getLabel()));
        add(idLabel);
        add(id);
        add(supportLabel);
        add(support);
        add(lSupportToleranceLabel);
        add(lSupportTolerance);
        add(rSupportToleranceLabel);
        add(rSupportTolerance);
        add(resistanceLabel);
        add(resistance);
        add(lResistanceToleranceLabel);
        add(lResistanceTolerance);
        add(rResistanceToleranceLabel);
        add(rResistanceTolerance);
        add(timeFrameLabel);
        add(timeFrame);
        add(timeFrameUnitLabel);
        add(timeFrameUnit);
        add(activeLabel);
        add(active);
        id.setEnabled(false);
        support.setEnabled(false);
        resistance.setEnabled(false);
        timeFrame.setEnabled(false);
        timeFrameUnit.setEnabled(false);
        active.setEnabled(false);
    }

    public JLabel getSupportLabel()
    {
        return supportLabel;
    }

    public NumberTextField getSupport()
    {
        return support;
    }

    public JLabel getResistanceLabel()
    {
        return resistanceLabel;
    }

    public NumberTextField getResistance()
    {
        return resistance;
    }

    public JLabel getTimeFrameLabel()
    {
        return timeFrameLabel;
    }

    public NumberTextField getTimeFrame()
    {
        return timeFrame;
    }

    public JLabel getIdLabel()
    {
        return idLabel;
    }

    public NumberTextField getId()
    {
        return id;
    }

    public JLabel getTimeFrameUnitLabel()
    {
        return timeFrameUnitLabel;
    }

    public JTextField getTimeFrameUnit()
    {
        return timeFrameUnit;
    }

    public void setTimeFrameUnit(JTextField timeFrameUnit)
    {
        this.timeFrameUnit = timeFrameUnit;
    }

    public JLabel getActiveLabel()
    {
        return activeLabel;
    }

    public JCheckBox getActive()
    {
        return active;
    }

    public JLabel getlSupportToleranceLabel()
    {
        return lSupportToleranceLabel;
    }

    public NumberTextField getLSupportTolerance()
    {
        return lSupportTolerance;
    }

    public JLabel getRSupportToleranceLabel()
    {
        return rSupportToleranceLabel;
    }

    public NumberTextField getRSupportTolerance()
    {
        return rSupportTolerance;
    }

    public JLabel getLResistanceToleranceLabel()
    {
        return lResistanceToleranceLabel;
    }

    public NumberTextField getLResistanceTolerance()
    {
        return lResistanceTolerance;
    }

    public JLabel getRResistanceToleranceLabel()
    {
        return rResistanceToleranceLabel;
    }

    public NumberTextField getRResistanceTolerance()
    {
        return rResistanceTolerance;
    }
}
