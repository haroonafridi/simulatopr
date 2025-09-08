package com.hkcapital.portoflio.ui.panels.marketconditions;

import com.hkcapital.portoflio.ui.fields.NumberTextField;

import javax.swing.*;
import java.awt.*;

public class MarketConditionsSourcePanel extends JPanel
{
    private final JLabel positionSizIdLabel = new JLabel("Id");
    private  NumberTextField positionId = new NumberTextField(30);
    private final JLabel instrumentLabel = new JLabel("Instrument");
    private  NumberTextField instrumentName = new NumberTextField(80);

    private final JLabel dayLowLabel = new JLabel("Day Low");
    private  NumberTextField dayLow = new NumberTextField(80 );
    private final JLabel dayHighLabel = new JLabel("Day High");
    private  NumberTextField dayHigh = new NumberTextField(80 );
    private final JLabel percentMoveLabel = new JLabel("% Move");
    private  NumberTextField percentMove = new NumberTextField(80 );
    public MarketConditionsSourcePanel() {
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
