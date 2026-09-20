package com.hkcapital.portflio.ui.panels.csvdata.panels;

import com.hkcapital.portflio.util.DateTimeUtil;
import com.hkcapital.portflio.model.Instrument;
import com.hkcapital.portflio.repository.registry.ServiceRegistery;
import com.hkcapital.portflio.service.candle.etoro.EtoroCandleService;
import com.hkcapital.portflio.service.importexport.export.csv.candle.CandleCSVGenerator;
import com.hkcapital.portflio.service.importexport.export.csv.feed.TickCSVGenerator;
import com.hkcapital.portflio.service.importexport.export.file.csv.CSVFileGenerator;
import com.hkcapital.portflio.service.instrument.InstrumentService;
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

    private CSVFileGenerator csvFileGenerator;
    private final InstrumentService instrumentService;

    private final LiveInstrumentFeedService liveInstrumentFeedService;

    private final JLabel dateFromLabel = new JLabel("(yyyy-mm-dd) Date From:");
    private final JTextField dateFrom = new JTextField(30);
    private JCheckBox candleDataCheckBox = new JCheckBox("Candle");
    private JCheckBox tickDataCheckBox = new JCheckBox("Tick");

    private JLabel instrumentLabel = new JLabel("Choose Instrument");
    private JComboBox<Instrument> instrumentJComboBox = new JComboBox<>();
    private final JButton generateButton = new JButton(Labels.Generate.getLabel());

    public CSVDataPanel(final ServiceRegistery serviceRegistery)
    {
        super(CSVDataPanel.class);
        this.serviceRegistery = serviceRegistery;
        this.etoroCandleService = (EtoroCandleService) this.serviceRegistery.getService(Service.EtoroCandleService);
        this.liveInstrumentFeedService = (LiveInstrumentFeedService) this.serviceRegistery.getService(Service.LiveInstrumentFeedService);
        this.instrumentService = (InstrumentService) this.serviceRegistery.getService(Service.InstrumentService);
        this.csvFileGenerator = (CSVFileGenerator) this.serviceRegistery.getService(Service.CSVCandleFileGenerator);
        setLayout(new GridBagLayout());
        setBorder(BorderFactory.createTitledBorder(Labels.CSVData.getLabel()));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        // Row 0: Instrument label + text field
        JPanel checkBoxPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 0));
        checkBoxPanel.add(candleDataCheckBox);
        checkBoxPanel.add(tickDataCheckBox);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.weightx = 1.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        add(checkBoxPanel);

        // Row 1: Instrument label + text field
        JPanel comboPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 0));
        comboPanel.add(instrumentLabel);
        comboPanel.add(instrumentJComboBox);
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        gbc.weightx = 1.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        add(comboPanel, gbc);
        // Row 2: Instrument label + text field
        JPanel dateInputPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 0));
        dateInputPanel.add(dateFromLabel);
        dateInputPanel.add(dateFrom);
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        gbc.weightx = 1.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        add(dateInputPanel, gbc);

        // Row 1: Buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 0));
        buttonPanel.add(generateButton);
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        gbc.weightx = 0;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.EAST;
        add(buttonPanel, gbc);

        candleDataCheckBox.addChangeListener(l ->
        {
            if (tickDataCheckBox.isSelected() && candleDataCheckBox.isSelected())
            {
                instrumentJComboBox.removeAll();
                instrumentService.findByActiveAndWithFeedAndWithCandle(true, true, true).forEach(inst ->
                {
                    instrumentJComboBox.addItem(inst);
                });
            }

            if (candleDataCheckBox.isSelected() && !candleDataCheckBox.isSelected())
            {
                instrumentJComboBox.removeAll();
                instrumentService.findByActiveAndWithCandle(true, true).forEach(inst ->
                {
                    instrumentJComboBox.addItem(inst);
                });
            }

            if (!tickDataCheckBox.isSelected() && candleDataCheckBox.isSelected())
            {
                instrumentJComboBox.removeAll();
                instrumentService.findByActiveAndWithFeed(true, true).forEach(inst ->
                {
                    instrumentJComboBox.addItem(inst);
                });
            }

        });
        generateButton.addActionListener(e -> generateData());
    }


    public void generateData()
    {
        final Instant start = DateTimeUtil.asDayStart(dateFrom.getText(), ZoneOffset.UTC);

        final Instant end = DateTimeUtil.asDayEnd(dateFrom.getText(), ZoneOffset.UTC);

        if (candleDataCheckBox.isSelected() && instrumentJComboBox.getModel().getSize() > 0)
        {
            int records = new CandleCSVGenerator(etoroCandleService, csvFileGenerator)
                    .generate(start, end, (Instrument) instrumentJComboBox.getSelectedItem());
            JOptionPane.showMessageDialog(this, "Candle Data successfully generated, no of candles =" + records);
        }

        if (tickDataCheckBox.isSelected() && instrumentJComboBox.getModel().getSize() > 0)
        {
            int records = new TickCSVGenerator(liveInstrumentFeedService, csvFileGenerator)
                    .generate(start, end, (Instrument) instrumentJComboBox.getSelectedItem());
            JOptionPane.showMessageDialog(this, "Tick Data successfully generated, no of ticks = " + records);
        }

    }

    @Override
    protected void paintComponent(Graphics g)
    {
        super.paintComponent(g); // always call super
    }
}
