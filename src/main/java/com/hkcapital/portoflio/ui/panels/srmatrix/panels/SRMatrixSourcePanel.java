package com.hkcapital.portoflio.ui.panels.srmatrix.panels;

import com.hkcapital.portoflio.ui.UIBag;
import com.hkcapital.portoflio.ui.fields.NumberTextField;
import com.hkcapital.portoflio.ui.panels.srmatrix.labels.Labels;

import javax.swing.*;
import java.awt.*;

public class SRMatrixSourcePanel extends UIBag
{
    private final JLabel idLabel = new JLabel("Id:");
    private final NumberTextField id = new NumberTextField(30);
    private final JLabel supportLabel = new JLabel(Labels.Support.getLabel());
    private final NumberTextField support = new NumberTextField(80);
    private final JLabel resistenceLabel = new JLabel(Labels.Resistance.getLabel());
    private final NumberTextField resistence = new NumberTextField(80);
    private final JLabel timeFrameLabel = new JLabel(Labels.TimeFrame.getLabel());
    private final NumberTextField timeFrame = new NumberTextField(80);
    private final JLabel timeFrameUnitLabel = new JLabel(Labels.TimeFrameUnit.getLabel());
    private JTextField timeFrameUnit = new JTextField(10);

    private final JLabel activeLable = new JLabel("Active");
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
        add(resistenceLabel);
        add(resistence);
        add(timeFrameLabel);
        add(timeFrame);
        add(timeFrameUnitLabel);
        add(timeFrameUnit);
        add(activeLable);
        add(active);
        id.setEnabled(false);
        support.setEnabled(false);
        resistence.setEnabled(false);
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

    public JLabel getResistenceLabel()
    {
        return resistenceLabel;
    }

    public NumberTextField getResistence()
    {
        return resistence;
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

    public JLabel getActiveLable()
    {
        return activeLable;
    }

    public JCheckBox getActive()
    {
        return active;
    }
}
