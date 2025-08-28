package com.hkcapital.portoflio.ui.panels;

import com.hkcapital.portoflio.model.Instrument;
import com.hkcapital.portoflio.model.MarketConditions;
import com.hkcapital.portoflio.model.Position;
import com.hkcapital.portoflio.ui.fields.NumberTextField;

import javax.swing.*;
import java.awt.*;

public class MarketConditionsPanel extends JPanel
{
    private final JLabel dayLowLabel = new JLabel("Day Low:");
    private  NumberTextField dayLowField = new NumberTextField(60, 3321.15);
    private final JLabel dayHighLabel = new JLabel("Day High:");
    private  NumberTextField dayHighField = new NumberTextField(60, 3378.40);

    private final JLabel percentMoveLabel = new JLabel("Percent Move:");

    private  NumberTextField percentMoveField = new NumberTextField(30, 1.06);

    private Instrument instrument;

    public MarketConditionsPanel(Instrument instrument,
                                 double dayLow, double dayHigh, double percentMove)
    {

        this.instrument = instrument;
        setBorder(BorderFactory.createTitledBorder("Market Conditions"));
        add(dayLowLabel);
        dayLowField.setText(Double.toString(dayLow));
        add(dayLowField);
        add(dayHighLabel);
        dayHighField.setText(Double.toString(dayHigh));
        add(dayHighField);
        add(percentMoveLabel);
        percentMoveField.setText(Double.toString(percentMove));
        add(percentMoveField);
    }


    public MarketConditions getMarketConditions()
    {
        return new MarketConditions( this.instrument, dayLowField.getDoubleValue(), dayHighField.getDoubleValue(),
                percentMoveField.getDoubleValue());
    }
}
