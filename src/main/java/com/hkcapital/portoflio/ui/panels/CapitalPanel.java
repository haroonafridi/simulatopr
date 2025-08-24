package com.hkcapital.portoflio.ui.panels;

import com.hkcapital.portoflio.model.OpeningCapital;
import com.hkcapital.portoflio.ui.fields.NumberTextField;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class CapitalPanel extends JPanel {

    private final JLabel openinDateLabel = new JLabel("Date:");
    private final JLabel openingDate = new JLabel(LocalDate.now().format(DateTimeFormatter.ISO_DATE));
    private final JLabel openingCapitalLabel = new JLabel("Opening capital:");
    private final NumberTextField openingCapital = new NumberTextField(200);

    public CapitalPanel(OpeningCapital capital) {
        setLayout(new FlowLayout(FlowLayout.CENTER, 20, 15));
        setBorder(BorderFactory.createTitledBorder("Opening Capital"));
        add(openinDateLabel);
        openingDate.setText(capital.getDate().format(DateTimeFormatter.ISO_DATE));
        add(openingDate);
        add(openingCapitalLabel);
        openingCapital.setText(Double.toString(capital.getCapital()));
        add(openingCapital);
    }

    public OpeningCapital getOpeningCapital()
    {
        return new OpeningCapital(1, LocalDate.now(),
                openingCapital.getDoubleValue());
    }
}
