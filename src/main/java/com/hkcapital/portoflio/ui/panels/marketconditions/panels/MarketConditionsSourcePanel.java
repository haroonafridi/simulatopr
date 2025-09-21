package com.hkcapital.portoflio.ui.panels.marketconditions.panels;

import com.hkcapital.portoflio.ui.UIBag;
import com.hkcapital.portoflio.ui.fields.NumberTextField;
import com.hkcapital.portoflio.ui.panels.marketconditions.labels.Labels;

import javax.swing.*;
import java.awt.*;

public class MarketConditionsSourcePanel extends UIBag
{
    private final JLabel positionSizIdLabel = new JLabel(Labels.Id.getLabel());
    private NumberTextField positionId = new NumberTextField(30);
    private final JLabel instrumentLabel = new JLabel(Labels.InstrumentName.getLabel());
    private NumberTextField instrumentName = new NumberTextField(80);

    private final JLabel dayLowLabel = new JLabel(Labels.DayLow.getLabel());
    private NumberTextField dayLow = new NumberTextField(80);
    private final JLabel dayHighLabel = new JLabel(Labels.DayHigh.getLabel());
    private NumberTextField dayHigh = new NumberTextField(80);
    private final JLabel percentMoveLabel = new JLabel(Labels.PercentMove.getLabel());
    private NumberTextField percentMove = new NumberTextField(80);

    public MarketConditionsSourcePanel()
    {
        super(MarketConditionsSourcePanel.class);
        add(positionSizIdLabel);
        positionId.setEnabled(false);
        add(positionId);
        add(instrumentLabel);
        instrumentName.setEnabled(false);
        add(instrumentName);
        add(dayLowLabel);
        dayLow.setEnabled(false);
        add(dayLow);
        add(dayHighLabel);
        add(dayHigh);
        dayHigh.setEnabled(false);
        add(percentMoveLabel);
        percentMove.setEnabled(false);
        add(percentMove);
        setLayout(new FlowLayout(FlowLayout.LEFT, 15, 15));
    }


    public NumberTextField getPositionId()
    {
        return positionId;
    }

    public NumberTextField getInstrumentName()
    {
        return instrumentName;
    }

    public NumberTextField getDayLow()
    {
        return dayLow;
    }

    public NumberTextField getDayHigh()
    {
        return dayHigh;
    }

    public NumberTextField getPercentMove()
    {
        return percentMove;
    }

    public void setPositionId(NumberTextField positionId)
    {
        this.positionId = positionId;
    }

    public void setInstrumentName(NumberTextField instrumentName)
    {
        this.instrumentName = instrumentName;
    }

    public void setDayLow(NumberTextField dayLow)
    {
        this.dayLow = dayLow;
    }

    public void setDayHigh(NumberTextField dayHigh)
    {
        this.dayHigh = dayHigh;
    }

    public void setPercentMove(NumberTextField percentMove)
    {
        this.percentMove = percentMove;
    }
}
