package com.hkcapital.portoflio.ui.panels.capital;

import com.hkcapital.portoflio.model.Configuration;
import com.hkcapital.portoflio.model.OpeningCapital;
import com.hkcapital.portoflio.repository.ServiceRegistery;
import com.hkcapital.portoflio.ui.UIBag;
import com.hkcapital.portoflio.ui.fields.NumberTextField;
import com.hkcapital.portoflio.ui.panels.configuartion.panels.ConfigurationSourcePanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class CapitalPanel extends UIBag
{
    private final ServiceRegistery serviceRegistery;
    private final JLabel openingDateLabel = new JLabel(Labels.Date.getLabel());
    private final JLabel openingDate = new JLabel(LocalDate.now().format(DateTimeFormatter.ISO_DATE));
    private final JLabel openingCapitalLabel = new JLabel(Labels.OpeningCapital.getLabel());
    private final NumberTextField openingCapital = new NumberTextField(200);

    private final JLabel allocatedCapitalLabel = new JLabel(Labels.AllocatedCapital.getLabel());
    private final NumberTextField allocatedCapital = new NumberTextField(200);


    public CapitalPanel(final ServiceRegistery serviceRegistery)
    {

        super(CapitalPanel.class);
        this.serviceRegistery = serviceRegistery;
        setLayout(new FlowLayout(FlowLayout.CENTER, 20, 15));
        setBorder(BorderFactory.createTitledBorder(Labels.OpeningCapital.getLabel()));
        add(openingDateLabel);
        openingDate.setText(LocalDateTime.now().format(DateTimeFormatter.ISO_DATE));
        add(openingDate);
        add(openingCapitalLabel);
        openingCapital.setText(null);
        add(openingCapital);
        add(allocatedCapitalLabel);
        add(allocatedCapital);
        openingCapital.addFocusListener(new CalculateAllocatedCapital());
        allocatedCapital.addFocusListener(new CalculateAllocatedCapital());
    }

    public OpeningCapital getOpeningCapital()
    {
        return new OpeningCapital(1, LocalDate.now(),
                openingCapital.getDoubleValue());
    }


    class CalculateAllocatedCapital implements FocusListener
    {

        CalculateAllocatedCapital()
        {
        }
        @Override
        public void focusGained(FocusEvent e)
        {

        }
        @Override
        public void focusLost(FocusEvent e)
        {
            ConfigurationSourcePanel component =  getComponent(ConfigurationSourcePanel.class);

            Configuration configuration = component.getConfiguration();

            double percent =  configuration.getPercentAllocationAllowed();

            allocatedCapital.setText(Double.toString(openingCapital.getDoubleValue() * percent / 100));
        }
    }


}


