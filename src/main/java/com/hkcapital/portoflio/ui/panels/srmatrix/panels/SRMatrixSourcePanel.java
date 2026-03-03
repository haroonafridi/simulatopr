package com.hkcapital.portoflio.ui.panels.srmatrix.panels;

import com.hkcapital.portoflio.model.Instrument;
import com.hkcapital.portoflio.model.SRMatrix;
import com.hkcapital.portoflio.repository.ServiceRegistery;
import com.hkcapital.portoflio.service.InstrumentService;
import com.hkcapital.portoflio.service.SRMatrixService;
import com.hkcapital.portoflio.service.Service;
import com.hkcapital.portoflio.ui.UIBag;
import com.hkcapital.portoflio.ui.buttons.ButtonLabels;
import com.hkcapital.portoflio.ui.fields.NumberTextField;
import com.hkcapital.portoflio.ui.panels.srmatrix.dialogues.SRMatrixEditDialogue;
import com.hkcapital.portoflio.ui.panels.srmatrix.labels.Labels;
import com.hkcapital.portoflio.ui.panels.srmatrix.tablemodels.SRMatrixTableModel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.LocalDateTime;
import java.util.List;

public class SRMatrixSourcePanel extends UIBag
{
    private final JLabel idLabel = new JLabel("Id");
    private final NumberTextField id = new NumberTextField(30);
    private final JLabel supportLabel = new JLabel("Support");
    private final NumberTextField support = new NumberTextField(40);
    private final JLabel resistenceLabel = new JLabel("Resistance");
    private final NumberTextField resistence = new NumberTextField(40);
    private final JLabel timeFrameLabel = new JLabel("Timeframe");
    private final NumberTextField timeFrame = new NumberTextField(40);
    private final JLabel timeFrameUnitLabel = new JLabel("Timeframe Unit");
    private JTextField timeFrameUnit = new JTextField(20);

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
        add(timeFrameUnit);
        add(timeFrameUnitLabel);
        add(activeLable);
        add(active);
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
