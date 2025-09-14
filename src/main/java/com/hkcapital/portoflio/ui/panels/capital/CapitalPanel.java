package com.hkcapital.portoflio.ui.panels.capital;

import com.hkcapital.portoflio.model.OpeningCapital;
import com.hkcapital.portoflio.repository.ServiceRegistery;
import com.hkcapital.portoflio.service.ConfigurationService;
import com.hkcapital.portoflio.service.InstrumentService;
import com.hkcapital.portoflio.service.Service;
import com.hkcapital.portoflio.ui.fields.NumberTextField;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class CapitalPanel extends JPanel
{
    private final ServiceRegistery serviceRegistery;
    private final JLabel openingDateLabel = new JLabel(Labels.Date.getLabel());
    private final JLabel openingDate = new JLabel(LocalDate.now().format(DateTimeFormatter.ISO_DATE));
    private final JLabel openingCapitalLabel = new JLabel(Labels.OpeningCapital.getLabel());
    private final NumberTextField openingCapital = new NumberTextField(200);

    private final JLabel allocatedCapitalLabel = new JLabel(Labels.AllocatedCapital.getLabel());
    private final NumberTextField allocatedCapital = new NumberTextField(200);


    public CapitalPanel(final ServiceRegistery serviceRegistery,
            final OpeningCapital capital) {
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
        InstrumentService instrumentService =  (InstrumentService)this.serviceRegistery.getService(Service.InstrumentService);

        instrumentService.findAll().forEach(s-> {
            System.out.println(s.getName());
        });
    }

    public OpeningCapital getOpeningCapital()
    {
        return new OpeningCapital(1, LocalDate.now(),
                openingCapital.getDoubleValue());
    }
}
