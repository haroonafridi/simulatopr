package com.hkcapital.portflio.ui.panels.csvdata.panels;

import com.hkcapital.portflio.market.structure.DateTimeUtil;
import com.hkcapital.portflio.repository.registry.ServiceRegistery;
import com.hkcapital.portflio.service.candle.etoro.EtoroCandleService;
import com.hkcapital.portflio.service.csv.impl.CandleCSVGenerator;
import com.hkcapital.portflio.service.csv.impl.TickCSVGenerator;
import com.hkcapital.portflio.service.marketfeed.LiveInstrumentFeedService;
import com.hkcapital.portflio.service.registry.Service;
import com.hkcapital.portflio.ui.UIBag;
import com.hkcapital.portflio.ui.panels.csvdata.labels.Labels;

import javax.swing.*;
import java.awt.*;
import java.time.Instant;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

public class CSVDataPanel extends UIBag
{

    private static final DateTimeFormatter FILE_FORMAT =
            DateTimeFormatter.ofPattern("dd_MM_yyyy");
    private final ServiceRegistery<Service> serviceRegistery;

    private final EtoroCandleService etoroCandleService;

    private final LiveInstrumentFeedService liveInstrumentFeedService;

    private final JLabel dateFromLabel = new JLabel("(yyyy-mm-dd) Date From:");
    private final JTextField dateFrom = new JTextField(30);

    JCheckBox candleDataCheckBox = new JCheckBox("Candle");
    JCheckBox tickDataCheckBox = new JCheckBox("Tick");

    private final JButton generateButton = new JButton(Labels.Generate.getLabel());

    public CSVDataPanel(final ServiceRegistery serviceRegistery)
    {
        super(CSVDataPanel.class);
        this.serviceRegistery = serviceRegistery;
        this.etoroCandleService = (EtoroCandleService) this.serviceRegistery.getService(Service.EtoroCandleService);
        this.liveInstrumentFeedService = (LiveInstrumentFeedService) this.serviceRegistery.getService(Service.LiveInstrumentFeedService);
        setLayout(new GridBagLayout());
        setBorder(BorderFactory.createTitledBorder(Labels.CSVData.getLabel()));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);

        // Row 0: Instrument label + text field
        JPanel dateInputPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));

        dateInputPanel.add(dateFromLabel);
        dateInputPanel.add(dateFrom);

        dateInputPanel.add(candleDataCheckBox);
        dateInputPanel.add(tickDataCheckBox);

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.weightx = 1.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        add(dateInputPanel, gbc);

        JPanel checkBoxPanel = new JPanel(new FlowLayout(FlowLayout.LEADING, 10, 0));
        checkBoxPanel.add(candleDataCheckBox);
        checkBoxPanel.add(tickDataCheckBox);
        add(checkBoxPanel);
        // Row 1: Buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        buttonPanel.add(generateButton);
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        gbc.weightx = 0;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.EAST;
        add(buttonPanel, gbc);
        // Row 2: Table inside scroll pane

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0; // allows table to expand vertically
        gbc.fill = GridBagConstraints.BOTH;
        generateButton.addActionListener(e -> generateData());

    }


    public void generateData()
    {
        final Instant start = DateTimeUtil.asDayStart(dateFrom.getText(), ZoneOffset.UTC);
        final Instant end = DateTimeUtil.asDayEnd(dateFrom.getText(), ZoneOffset.UTC);

        if (candleDataCheckBox.isSelected())
        {
            new CandleCSVGenerator(etoroCandleService).generate(start, end);
        }

        if (tickDataCheckBox.isSelected())
        {
            new TickCSVGenerator(liveInstrumentFeedService).generate(start, end);
        }
        if (candleDataCheckBox.isSelected())
        {
            JOptionPane.showMessageDialog(this, "Candle Data successfully generated");
        }

        if (tickDataCheckBox.isSelected())
        {
            JOptionPane.showMessageDialog(this, "Tick Data successfully generated");
        }

    }

    @Override
    protected void paintComponent(Graphics g)
    {
        super.paintComponent(g); // always call super
    }
}
