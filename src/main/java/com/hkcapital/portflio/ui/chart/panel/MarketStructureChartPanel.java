package com.hkcapital.portflio.ui.chart.panel;

import com.hkcapital.portflio.market.structure.MarketPriceBand;
import com.hkcapital.portflio.market.structure.MarketStructureCache;
import com.hkcapital.portflio.market.structure.MarketTypes;
import com.hkcapital.portflio.model.Candle;
import com.hkcapital.portflio.repository.registry.ServiceRegistery;
import com.hkcapital.portflio.service.candle.etoro.EtoroCandleService;
import com.hkcapital.portflio.service.registry.Service;
import com.hkcapital.portflio.ui.UIBag;
import com.hkcapital.portflio.ui.panels.position.listeners.OpenSRMatrixDialogueListener;
import com.hkcapital.portflio.ui.panels.srmatrix.dialogues.SRMatrixDialogue;
import com.hkcapital.portflio.ui.panels.srmatrix.panels.SRMatrixPanel;
import com.hkcapital.portflio.ui.panels.srmatrix.panels.SRMatrixSourcePanel;
import org.jfree.chart.ChartMouseEvent;
import org.jfree.chart.ChartMouseListener;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.panel.CrosshairOverlay;
import org.jfree.chart.plot.Crosshair;
import org.jfree.chart.plot.ValueMarker;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.chart.ui.TextAnchor;
import org.jfree.data.time.Minute;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.springframework.stereotype.Component;

import javax.swing.*;
import java.awt.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.ChronoField;
import java.util.Date;
import java.util.List;
import java.util.NavigableSet;
@Component
public class MarketStructureChartPanel extends UIBag
{

    private final TimeSeries series;
    private final ChartPanel chartPanel;

    private JComboBox<String> timeframeCombo;
    private JComboBox<String> unitCombo;

    private JComboBox<String> bandsCombo;

    private double minClose = Double.MAX_VALUE;
    private double maxClose = Double.MIN_VALUE;

    private File candleFile;
    private File bandFile;

    private Crosshair yCrosshair;

    private final ServiceRegistery<Service> serviceRegistery;

    private MarketStructureCache marketStructureCache;

    private Frame frame;

    public MarketStructureChartPanel(final ServiceRegistery serviceRegistery,
                                     final MarketStructureCache marketStructureCache)
    {
        super(ServiceRegistery.class);
        this.marketStructureCache = marketStructureCache;
        this.serviceRegistery = serviceRegistery;
        // ================= DATA =================
        series = new TimeSeries("Close Price");

        TimeSeriesCollection dataset = new TimeSeriesCollection();

        dataset.addSeries(series);

        // ================= CHART =================
        DateAxis xAxis = new DateAxis("Time");
        NumberAxis yAxis = new NumberAxis("Close Price");

        XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer(true, true);

        renderer.setSeriesShapesVisible(0, true);

        renderer.setSeriesShape(0,
                new java.awt.geom.Ellipse2D.Double(-1, -1, 4, 4));

        renderer.setSeriesPaint(0, Color.MAGENTA);
        renderer.setSeriesFillPaint(0, Color.YELLOW);
        renderer.setSeriesOutlinePaint(0, Color.YELLOW);
        renderer.setSeriesStroke(0, new BasicStroke(1.2f));

        XYPlot plot = new XYPlot(dataset, xAxis, yAxis, renderer);


        JFreeChart chart = new JFreeChart("Market Close Chart", JFreeChart.DEFAULT_TITLE_FONT, plot, true);

        chartPanel = new ChartPanel(chart);
        chartPanel.setMouseWheelEnabled(true);

        // ================= CONTROLS =================
        JButton uploadButton = new JButton("Upload Candles CSV");
        JButton refreshButton = new JButton("Refresh");
        JButton drawBandButton = new JButton("Upload Bands CSV");
        JButton refreshBands = new JButton("Refresh Bands");
        JButton liveBands = new JButton("Live Bands");
        JButton liveCandles = new JButton("Live Candles");

        String[] timeframes = {"1", "5", "15", "30", "4"};
        timeframeCombo = new JComboBox<>(timeframes);
        timeframeCombo.setSelectedItem("5"); // DEFAULT

        String[] units = {"MINUTE", "HOUR", "DAY"};
        unitCombo = new JComboBox<>(units);
        unitCombo.setSelectedItem("MINUTE");

        String[] bands = {"UPPER", "LOWER"};
        bandsCombo = new JComboBox<>(bands);
        bandsCombo.setSelectedItem("UPPER");

        uploadButton.addActionListener(e -> openCandleFileDialog());

        JPanel toolBar = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));

        toolBar.add(uploadButton);
        toolBar.add(new JLabel("Timeframe:"));
        toolBar.add(timeframeCombo);
        toolBar.add(new JLabel("Unit:"));
        toolBar.add(unitCombo);
        toolBar.add(refreshButton);
        toolBar.add(bandsCombo);
        toolBar.add(drawBandButton);
        toolBar.add(refreshBands);
        toolBar.add(liveCandles);
        toolBar.add(liveBands);

        CrosshairOverlay overlay = new CrosshairOverlay();

        yCrosshair = new Crosshair(Double.NaN, Color.RED, new BasicStroke(1.0f));
        yCrosshair.setLabelVisible(true);
        yCrosshair.setLabelBackgroundPaint(new Color(255, 255, 255));

        overlay.addDomainCrosshair(new Crosshair(Double.NaN)); // optional vertical line
        overlay.addRangeCrosshair(yCrosshair);

        chartPanel.addOverlay(overlay);

        // ================= LAYOUT =================
        setLayout(new BorderLayout());
        add(toolBar, BorderLayout.NORTH);
        add(chartPanel, BorderLayout.CENTER);
        chartPanel.addChartMouseListener(new ChartMouseListener()
        {

            @Override
            public void chartMouseMoved(ChartMouseEvent event)
            {

                if (event.getEntity() == null) return;

                XYPlot plot = (XYPlot) chartPanel.getChart().getPlot();

                double y = plot.getRangeAxis().java2DToValue(
                        event.getTrigger().getY(),
                        chartPanel.getScreenDataArea(),
                        plot.getRangeAxisEdge()
                );

                yCrosshair.setValue(y);
            }

            @Override
            public void chartMouseClicked(ChartMouseEvent event)
            {
                double y = plot.getRangeAxis().java2DToValue(
                        event.getTrigger().getY(),
                        chartPanel.getScreenDataArea(),
                        plot.getRangeAxisEdge()
                );
                displaySrMatrix();
            }
        });
        refreshButton.addActionListener(e ->
        {
            processCandleCsv(
                    candleFile,
                    (String) timeframeCombo.getSelectedItem(),
                    (String) unitCombo.getSelectedItem()
            );
        });

        liveCandles.addActionListener(e-> {
            processCandle(null, null);
        });
        liveBands.addActionListener(e ->
        {
            try
            {
                processBand((String) timeframeCombo.getSelectedItem(),
                        (String) unitCombo.getSelectedItem(),
                        null);
            } catch (IOException ex)
            {
                throw new RuntimeException(ex);
            }
        });

        drawBandButton.addActionListener(e -> openBandFileDialog());

        refreshBands.addActionListener(e ->
        {
            try
            {
                processBandCsv(
                        bandFile,
                        (String) timeframeCombo.getSelectedItem(),
                        (String) unitCombo.getSelectedItem(),
                        (String) bandsCombo.getSelectedItem()
                );
            } catch (IOException ex)
            {
                throw new RuntimeException(ex);
            }
        });

        setSize(1100, 700);

    }


    private void displaySrMatrix()
    {
        SRMatrixSourcePanel srMatrixAndSourcePanel = new SRMatrixSourcePanel();
        SRMatrixDialogue configurationDialogue = //
                new SRMatrixDialogue(frame, new SRMatrixPanel(serviceRegistery, srMatrixAndSourcePanel));
        configurationDialogue.setVisible(true);
    }

    // ================= FILE DIALOG =================
    private void openCandleFileDialog()
    {

        JFileChooser fileChooser = new JFileChooser();

        fileChooser.setCurrentDirectory(new File("D:/gold_data"));

        fileChooser.setFileFilter(
                new javax.swing.filechooser.FileNameExtensionFilter(
                        "CSV Files", "csv"
                )
        );

        int result = fileChooser.showOpenDialog(this);

        if (result == JFileChooser.APPROVE_OPTION)
        {

            candleFile = fileChooser.getSelectedFile();

            processCandleCsv(
                    candleFile,
                    (String) timeframeCombo.getSelectedItem(),
                    (String) unitCombo.getSelectedItem()
            );
        }
    }


    private void openBandFileDialog()
    {

        JFileChooser fileChooser = new JFileChooser();

        fileChooser.setCurrentDirectory(new File("D:/hk-prod/market-data"));

        fileChooser.setFileFilter(
                new javax.swing.filechooser.FileNameExtensionFilter(
                        "CSV Files", "csv"
                )
        );

        int result = fileChooser.showOpenDialog(this);

        if (result == JFileChooser.APPROVE_OPTION)
        {

            bandFile = fileChooser.getSelectedFile();

            try
            {
                processBandCsv(
                        bandFile,
                        (String) timeframeCombo.getSelectedItem(),
                        (String) unitCombo.getSelectedItem(),
                        (String) bandsCombo.getSelectedItem()
                );
            } catch (IOException e)
            {
                throw new RuntimeException(e);
            }
        }

    }

    // ================= CSV PARSER =================
    public void processCandleCsv(File file, String timeframe, String unit)
    {

        series.clear();

        DateTimeFormatter formatter =
                new DateTimeFormatterBuilder()
                        .append(DateTimeFormatter.ISO_LOCAL_DATE_TIME)
                        .optionalStart()
                        .appendFraction(ChronoField.NANO_OF_SECOND, 0, 9, true)
                        .optionalEnd()
                        .toFormatter();


        try (BufferedReader br = new BufferedReader(new FileReader(file)))
        {

            LocalDate dateTimeTitle = null;
            String header = br.readLine();
            if (header == null) return;

            String[] columns = header.split(",");

            int timeIndex = -1;
            int closeIndex = -1;
            int timeFrameIndex = -1;
            int timeFrameUnitIndex = -1;

            for (int i = 0; i < columns.length; i++)
            {
                if (columns[i].equals("creation_date_time"))
                {
                    timeIndex = i;
                }
                if (columns[i].equals("close"))
                {
                    closeIndex = i;
                }

                if (columns[i].equals("time_frame"))
                {
                    timeFrameIndex = i;
                }

                if (columns[i].equals("time_frame_unit"))
                {
                    timeFrameUnitIndex = i;
                }

                if (columns[i].equals("open"))
                {
                    timeFrameUnitIndex = i;
                }

                if (columns[i].equals("high"))
                {
                    timeFrameUnitIndex = i;
                }

                if (columns[i].equals("low"))
                {
                    timeFrameUnitIndex = i;
                }

            }

            if (timeIndex == -1 || closeIndex == -1)
            {
                throw new RuntimeException("Required columns not found!");
            }

            String line;

            while ((line = br.readLine()) != null)
            {
                String[] values = line.split(",");
                String timeFrameStr = values[timeFrameIndex].trim();
                String timeframeUnitStr = values[timeFrameUnitIndex].trim();

                if (timeframeUnitStr.equals(unit.toUpperCase()) && timeFrameStr.equals(timeframe))
                {
                    if (values.length <= Math.max(timeIndex, closeIndex)) continue;

                    String timeStr = values[timeIndex].trim();
                    String closeStr = values[closeIndex].trim();

                    if (timeStr.isEmpty() || closeStr.isEmpty()) continue;


                    LocalDateTime dateTime = LocalDateTime.parse(timeStr, formatter);
                    dateTimeTitle = dateTime.toLocalDate();
                    double close = Double.parseDouble(closeStr);

                    if (close < minClose) minClose = close;
                    if (close > maxClose) maxClose = close;

                    series.addOrUpdate(
                            new Minute(
                                    java.util.Date.from(
                                            dateTime.atZone(ZoneId.systemDefault()).toInstant()
                                    )
                            ),
                            close
                    );

                    NumberAxis yAxis = (NumberAxis) ((XYPlot) chartPanel.getChart().getPlot()).getRangeAxis();
                    yAxis.setLabelPaint(Color.RED);
                    yAxis.setTickLabelPaint(Color.RED);

                    double padding = Math.max((maxClose - minClose) * 0.05, 1.0);

                    if (maxClose == minClose)
                    {
                        maxClose += 1.0;
                        minClose -= 1.0;
                    }


                    yAxis.setRange(minClose - padding, maxClose + padding);

                    DateAxis xAxis = (DateAxis) ((XYPlot) chartPanel.getChart().getPlot()).getDomainAxis();

                    xAxis.setTickLabelPaint(Color.GRAY);
                    xAxis.setLabelPaint(Color.GRAY);
                }
            }

            chartPanel.getChart().setTitle("Gold Chart of date " + dateTimeTitle);
        } catch (Exception e)
        {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this,
                    "Error reading CSV: " + e.getMessage());
        }
    }


    public void processCandle(String timeframe, String unit) {

        series.clear();

        minClose = Double.MAX_VALUE;
        maxClose = Double.MIN_VALUE;

        EtoroCandleService etoroCandleService =
                (EtoroCandleService) serviceRegistery.getService(Service.EtoroCandleService);

        LocalDateTime start = LocalDate.now().atStartOfDay();
        LocalDateTime end = start.plusDays(1);

        List<Candle> candles =
                etoroCandleService.findByInstrumentIDAndCreationDateTimeBetween(18, start, end);

        LocalDate dateTimeTitle = null;

        for (Candle candle : candles) {

            // Filter by timeframe and unit
            if (!String.valueOf(candle.getTimeFrame()).equals(timeframe)) {
                continue;
            }

            if (!unit.equalsIgnoreCase(candle.getTimeFrameUnit())) {
                continue;
            }

            LocalDateTime dateTime = LocalDateTime.ofInstant(
                    candle.getFromDate(),
                    ZoneId.systemDefault()
            );

            dateTimeTitle = dateTime.toLocalDate();

            double close = candle.getClose();

            minClose = Math.min(minClose, close);
            maxClose = Math.max(maxClose, close);

            series.addOrUpdate(
                    new Minute(Date.from(dateTime.atZone(ZoneId.systemDefault()).toInstant())),
                    close
            );
        }

        NumberAxis yAxis =
                (NumberAxis) ((XYPlot) chartPanel.getChart().getPlot()).getRangeAxis();

        yAxis.setLabelPaint(Color.RED);
        yAxis.setTickLabelPaint(Color.RED);

        if (maxClose == minClose) {
            maxClose += 1.0;
            minClose -= 1.0;
        }

        double padding = Math.max((maxClose - minClose) * 0.05, 1.0);
        yAxis.setRange(minClose - padding, maxClose + padding);

        DateAxis xAxis =
                (DateAxis) ((XYPlot) chartPanel.getChart().getPlot()).getDomainAxis();

        xAxis.setTickLabelPaint(Color.GRAY);
        xAxis.setLabelPaint(Color.GRAY);

        if (dateTimeTitle != null) {
            chartPanel.getChart().setTitle("Gold Chart of date " + dateTimeTitle);
        }

    }


    // ================= CSV PARSER =================
    public void processBandCsv(File file, String timeframe, String unit, String band) throws IOException
    {
        XYPlot plot = (XYPlot) chartPanel.getChart().getPlot();
        plot.clearRangeMarkers();

        try (BufferedReader br = new BufferedReader(new FileReader(file)))
        {

            String header = br.readLine();
            if (header == null) return;

            String[] columns = header.split(",");

            int lowerBandIndex = -1;
            int upperBandIndex = -1;
            int marketVisitCountIndex = -1;
            int bandTypeIndex = -1;

            int timeFrameIndex = -1;
            int timeFrameUnitIndex = -1;


            for (int i = 0; i < columns.length; i++)
            {
                if (columns[i].equals("lowerBound"))
                {
                    lowerBandIndex = i;
                }
                if (columns[i].equals("upperBound"))
                {
                    upperBandIndex = i;
                }

                if (columns[i].equals("marketVisitCount"))
                {
                    marketVisitCountIndex = i;
                }

                if (columns[i].equals("band_type"))
                {
                    bandTypeIndex = i;
                }

                if (columns[i].equals("timeFrame"))
                {
                    timeFrameIndex = i;
                }

                if (columns[i].equals("timeFrameUnit"))
                {
                    timeFrameUnitIndex = i;
                }


            }
            String line;

            while ((line = br.readLine()) != null)
            {
                String[] values = line.split(",");
                String lowerBandStr = values[lowerBandIndex].trim();
                String upperBandStr = values[upperBandIndex].trim();
                String marketVisitCountStr = values[marketVisitCountIndex].trim();
                String bandTypeStr = values[bandTypeIndex].trim();
                String timeFrameStr = values[timeFrameIndex].trim();
                String timeframeUnitStr = values[timeFrameUnitIndex].trim();

                if (timeframeUnitStr.equals(unit.toUpperCase()) && timeFrameStr.equals(timeframe) && band.equals(bandTypeStr))
                {
                    double value = Double.valueOf(upperBandStr);

                    // Solid stroke setup (replaces the dotted configuration)
                    BasicStroke solidStroke = new BasicStroke(0.5f);
                    Font labelFont = new Font("Arial", Font.PLAIN, 10);

                    if (band.equals("LOWER"))
                    {
                        value = Double.valueOf(lowerBandStr);
                    }

                    ValueMarker lowerStartMarker = new ValueMarker(value);
                    lowerStartMarker.setPaint(Color.RED);
                    lowerStartMarker.setStroke(solidStroke);
                    //lowerStartMarker.setLabel(" Lower Band = " + lowerBandStr);
                    lowerStartMarker.setLabelFont(labelFont);
                    lowerStartMarker.setLabelPaint(Color.BLUE);
                    //lowerStartMarker.setLabelAnchor(RectangleAnchor.BOTTOM_LEFT);
                    lowerStartMarker.setLabelTextAnchor(TextAnchor.BOTTOM_LEFT);
                    plot.addRangeMarker(lowerStartMarker);

                    // Marker 2: Right Anchor (End of Line)
                    ValueMarker lowerEndMarker = new ValueMarker(value);
                    lowerEndMarker.setPaint(Color.RED);
                    lowerEndMarker.setStroke(solidStroke);
                    lowerEndMarker.setLabel("[" + lowerBandStr + " - " + upperBandStr + "]" + "  visits = (" + marketVisitCountStr + ")");
                    lowerEndMarker.setLabelFont(labelFont);
                    lowerEndMarker.setLabelPaint(Color.BLUE);
                    //lowerEndMarker.setLabelAnchor(RectangleAnch.BOTTOM_RIGHT);
                    lowerEndMarker.setLabelTextAnchor(TextAnchor.BOTTOM_LEFT);
                    plot.addRangeMarker(lowerEndMarker);
                }

            }


        } catch (Exception e)
        {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this,
                    "Error reading CSV: " + e.getMessage());
        }

    }


    public void processBand(String timeframe, String unit, String band) throws IOException
    {
        XYPlot plot = (XYPlot) chartPanel.getChart().getPlot();
        plot.clearRangeMarkers();

        NavigableSet<MarketPriceBand> bands = this.marketStructureCache.get(MarketTypes.GOLD_15_MIN).getLowerBands();

        bands.forEach(b ->
        {
            BasicStroke solidStroke = new BasicStroke(0.5f);
            Font labelFont = new Font("Arial", Font.PLAIN, 10);

            ValueMarker lowerStartMarker = new ValueMarker(b.getLowerBound());
            lowerStartMarker.setPaint(Color.RED);
            lowerStartMarker.setStroke(solidStroke);
            //lowerStartMarker.setLabel(" Lower Band = " + lowerBandStr);
            lowerStartMarker.setLabelFont(labelFont);
            lowerStartMarker.setLabelPaint(Color.BLUE);
            //lowerStartMarker.setLabelAnchor(RectangleAnchor.BOTTOM_LEFT);
            lowerStartMarker.setLabelTextAnchor(TextAnchor.BOTTOM_LEFT);
            plot.addRangeMarker(lowerStartMarker);

            // Marker 2: Right Anchor (End of Line)
            ValueMarker lowerEndMarker = new ValueMarker(b.getLowerBound());
            lowerEndMarker.setPaint(Color.RED);
            lowerEndMarker.setStroke(solidStroke);
            lowerEndMarker.setLabel("[" + b.getLowerBound() + " - " + b.getLowerBound() + "]" + "  visits = (" + b.getMarketVisitCount() + ")");
            lowerEndMarker.setLabelFont(labelFont);
            lowerEndMarker.setLabelPaint(Color.BLUE);
            //lowerEndMarker.setLabelAnchor(RectangleAnch.BOTTOM_RIGHT);
            lowerEndMarker.setLabelTextAnchor(TextAnchor.BOTTOM_LEFT);
            plot.addRangeMarker(lowerEndMarker);
        });
    }


    private boolean isWithinTimeWindow(LocalDateTime time,
                                       LocalDateTime now,
                                       int amount,
                                       String unit)
    {

        switch (unit.toLowerCase())
        {

            case "seconds":
                return !time.isBefore(now.minusSeconds(amount));

            case "minutes":
                return !time.isBefore(now.minusMinutes(amount));

            case "hours":
                return !time.isBefore(now.minusHours(amount));

            case "days":
                return !time.isBefore(now.minusDays(amount));

            default:
                return true;
        }
    }


    public void setFrame(Frame frame) {
        this.frame = frame;
    }

    public Frame getFrame()
    {
        return frame;
    }
}