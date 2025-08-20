package com.hkcapital.portoflio.ui.panels;

import com.hkcapital.portoflio.model.Instrument;
import com.hkcapital.portoflio.model.OpeningCapital;
import com.hkcapital.portoflio.model.Position;
import com.hkcapital.portoflio.ui.fields.NumberTextField;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class CapitalPanel extends JPanel {

    private final JLabel openinDateLabel = new JLabel("Date:");
    private final JLabel openingDate = new JLabel(LocalDate.now().format(DateTimeFormatter.ISO_DATE));
    private final JLabel openinCapitalLabel = new JLabel("Opening capital:");
    private final NumberTextField openingCapital = new NumberTextField();

    public CapitalPanel() {
        setLayout(new FlowLayout(FlowLayout.CENTER, 20, 15));
        setBorder(BorderFactory.createTitledBorder("Opening Capital"));
        add(openinDateLabel);
        add(openingDate);
        add(openinCapitalLabel);
        add(openingCapital);
    }

    public OpeningCapital getOpeningCapital()
    {
        return new OpeningCapital(1, LocalDate.now(),
                openingCapital.getDoubleValue());
    }
}
