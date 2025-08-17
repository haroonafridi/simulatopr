package com.hkcapital.portoflio.model;

import com.hkcapital.portoflio.ui.fields.NumberTextField;

import javax.swing.*;
import java.awt.*;

public class ConfigurationPanel extends JPanel {

    private final NumberTextField percentAllowedAllocation=  new NumberTextField(30);
    private final  NumberTextField noOfInstrumentsAllowed=  new NumberTextField(30);
    private final  NumberTextField noOfPositionsPerInstrument=  new NumberTextField(30);

    private final  NumberTextField maxAllowedPercentPerPosition=  new NumberTextField(30);

    private final  NumberTextField lev=  new NumberTextField(30);
    public ConfigurationPanel() {
        setLayout(new FlowLayout(FlowLayout.LEFT, 20, 15));
        setBorder(BorderFactory.createTitledBorder("⚙ Configuration Settings"));

        add(new JLabel("% Allowed Allocation:"));
        add(percentAllowedAllocation);

        add(new JLabel("No Of Instruments Allowed:"));
        add(noOfInstrumentsAllowed);

        add(new JLabel("No Of Positions Per Instrument:"));
        add(noOfPositionsPerInstrument);

        add(new JLabel("Max Percent Allowed per Instrument:"));
        add(maxAllowedPercentPerPosition);

        add(new JLabel("Leverage:"));
        add(lev);
    }

    public Configuraion getConfiguration()
    {
     return new Configuraion(1, percentAllowedAllocation.getDoubleValue(),
             noOfInstrumentsAllowed.getIntValue(),
             noOfPositionsPerInstrument.getIntValue(),
             maxAllowedPercentPerPosition.getIntValue(),
             lev.getIntValue());
    }

}
