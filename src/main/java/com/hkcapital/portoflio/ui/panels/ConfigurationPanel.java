package com.hkcapital.portoflio.ui.panels;

import com.hkcapital.portoflio.model.Configuration;
import com.hkcapital.portoflio.service.ConfigurationService;
import com.hkcapital.portoflio.ui.fields.NumberTextField;
import org.springframework.stereotype.Component;

import javax.swing.*;
import java.awt.*;
@Component
public class ConfigurationPanel extends JPanel {

    private final NumberTextField percentAllowedAllocation=  new NumberTextField(30, 15);
    private final  NumberTextField noOfInstrumentsAllowed=  new NumberTextField(30, 2);
    private final  NumberTextField noOfPositionsPerInstrument=  new NumberTextField(30, 3);

    private final  NumberTextField maxAllowedPercentPerPosition=  new NumberTextField(30,7.5);

    private final  NumberTextField lev=  new NumberTextField(30, 20);

    private final  JButton saveOrUpdateConfig=  new JButton("Save Configuration");

    private final ConfigurationService configurationService;
    public ConfigurationPanel(ConfigurationService configurationService) {
        this.configurationService = configurationService;
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
        add(saveOrUpdateConfig);
    }

    public Configuration getConfiguration()
    {
     return new Configuration( percentAllowedAllocation.getDoubleValue(),
             noOfInstrumentsAllowed.getIntValue(),
             noOfPositionsPerInstrument.getIntValue(),
             maxAllowedPercentPerPosition.getDoubleValue(),
             lev.getIntValue());
    }


    public JButton getSaveOrUpdateConfigButton() {
        return saveOrUpdateConfig;
    }

}
