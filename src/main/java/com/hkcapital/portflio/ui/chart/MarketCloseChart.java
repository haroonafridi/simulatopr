package com.hkcapital.portflio.ui.chart;

import com.hkcapital.portflio.market.indicators.CandleBuilder;
import com.hkcapital.portflio.market.indicators.CandleDto;
import com.hkcapital.portflio.market.indicators.TimeFramesUnit;
import com.hkcapital.portflio.market.structure.MarketPriceBand;
import com.hkcapital.portflio.market.structure.MarketStructure;
import com.hkcapital.portflio.market.structure.MarketStructureCache;
import com.hkcapital.portflio.market.structure.MarketTypes;
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

public class MarketCloseChart extends JFrame
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

    private MarketStructureCache marketStructureCache;
    private CandleBuilder candleBuilder;

    public MarketCloseChart(MarketStructureCache marketStructureCache, CandleBuilder candleBuilder)
    {

        this.marketStructureCache = marketStructureCache;
        this.candleBuilder = candleBuilder;
        setTitle("AI Trading Terminal - Market Close Viewer");

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


        JFreeChart chart = new JFreeChart(
                "Market Close Chart",
                JFreeChart.DEFAULT_TITLE_FONT,
                plot,
                true
        );

        chartPanel = new ChartPanel(chart);
        chartPanel.setMouseWheelEnabled(true);

        // ================= CONTROLS =================
        JButton uploadButton = new JButton("Upload Cansle CSV");
        JButton refreshButton = new JButton("Refresh");
        JButton drawBandButton = new JButton("Upload Bands CSV");
        JButton refreshBands = new JButton("Refresh Bands");

        String[] timeframes = {"1", "5", "15", "30", "4"};

        timeframeCombo = new JComboBox<>(timeframes);
        timeframeCombo.setSelectedItem("1"); // DEFAULT
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

                JOptionPane.showMessageDialog(chartPanel, "Price is " + y);
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
        setLocationRelativeTo(null);
        processCandle("1", "MINUTE");
        try
        {
            processBand();
        } catch (IOException e)
        {
            throw new RuntimeException(e);
        }
        //setDefaultCloseOperation(EXIT_ON_CLOSE);

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

    // ================= MAIN =================
    public void display()
    {
        this.setVisible(true);
        processCandle(null, null);
        try
        {
            processBand();
        } catch (IOException e)
        {
            throw new RuntimeException(e);
        }

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

    public void processBand() throws IOException
    {
        XYPlot plot = (XYPlot) chartPanel.getChart().getPlot();
        plot.clearRangeMarkers();
        MarketStructure marketStructure4Hr = this.marketStructureCache.get(MarketTypes.GOLD_4_HOUR);
        MarketStructure marketStructure1Hr = null;
        MarketStructure marketStructure30Min = null;
        MarketStructure marketStructure15Min = null;
        MarketStructure marketStructure5Min = null;
        MarketStructure marketStructure1Min = null;

        if (marketStructure4Hr != null)
        {
            marketStructure1Hr = marketStructure4Hr.getChildMarketStructure();
        }

        if (marketStructure1Hr != null)
        {
            marketStructure30Min = marketStructure1Hr.getChildMarketStructure();
        }

        if (marketStructure30Min != null)
        {
            marketStructure15Min = marketStructure30Min.getChildMarketStructure();
        }

        if (marketStructure15Min != null)
        {
            marketStructure5Min = marketStructure15Min.getChildMarketStructure();
        }

        if (marketStructure5Min != null)
        {
            marketStructure1Min = marketStructure5Min.getChildMarketStructure();
        }


        if (unitCombo.getSelectedItem().toString().equals("HOUR"))
        {
            if (timeframeCombo.getSelectedItem().toString().equals("4") && marketStructure4Hr != null)
            {
                final NavigableSet<MarketPriceBand> bands = marketStructure4Hr.getUpperBands();
                bands.forEach(b ->
                {
                    drawBands(plot, b);
                });
            }

            if (timeframeCombo.getSelectedItem().toString().equals("1") && marketStructure1Hr != null)
            {
                final NavigableSet<MarketPriceBand> bands = marketStructure1Hr.getUpperBands();
                bands.forEach(b ->
                {
                    drawBands(plot, b);
                });
            }
        }

        if (unitCombo.getSelectedItem().toString().equals("MINUTE"))
        {
            if (timeframeCombo.getSelectedItem().toString().equals("1") && marketStructure1Min != null)
            {

                NavigableSet<MarketPriceBand> bands = marketStructure1Min.getUpperBands();

                if (this.bandsCombo.getSelectedItem().toString().equals("LOWER"))
                {
                    bands = marketStructure1Min.getLowerBands();
                }

                bands.forEach(b ->
                {
                    drawBands(plot, b);
                });
            }

            if (timeframeCombo.getSelectedItem().toString().equals("5") && marketStructure5Min != null)
            {

                NavigableSet<MarketPriceBand> bands = marketStructure1Min.getUpperBands();

                if (this.bandsCombo.getSelectedItem().toString().equals("LOWER"))
                {
                    bands = marketStructure1Min.getLowerBands();
                }
                bands.forEach(b ->
                {
                    drawBands(plot, b);
                });
            }

            if (timeframeCombo.getSelectedItem().toString().equals("15") && marketStructure15Min != null)
            {
                NavigableSet<MarketPriceBand> bands = marketStructure1Min.getUpperBands();

                if (this.bandsCombo.getSelectedItem().toString().equals("LOWER"))
                {
                    bands = marketStructure1Min.getLowerBands();
                }
                bands.forEach(b ->
                {
                    drawBands(plot, b);
                });
            }
            if (timeframeCombo.getSelectedItem().toString().equals("30") && marketStructure30Min != null)
            {
                NavigableSet<MarketPriceBand> bands = marketStructure1Min.getUpperBands();

                if (this.bandsCombo.getSelectedItem().toString().equals("LOWER"))
                {
                    bands = marketStructure1Min.getLowerBands();
                }
                bands.forEach(b ->
                {
                    drawBands(plot, b);
                });
            }
        }
    }

    private static void drawBands(XYPlot plot, MarketPriceBand b)
    {
        double upperBandValue = b.getUpperBound();
        double lowerBandValue = b.getLowerBound();
        BasicStroke solidStroke = new BasicStroke(0.5f);
        Font labelFont = new Font("Arial", Font.PLAIN, 10);
        ValueMarker upperStartMarker = new ValueMarker(upperBandValue);
        upperStartMarker.setPaint(Color.RED);
        upperStartMarker.setStroke(solidStroke);
        upperStartMarker.setLabelFont(labelFont);
        upperStartMarker.setLabelPaint(Color.BLUE);
        upperStartMarker.setLabelTextAnchor(TextAnchor.BOTTOM_LEFT);
        plot.addRangeMarker(upperStartMarker);
        ValueMarker lowerEndMarker = new ValueMarker(lowerBandValue);
        lowerEndMarker.setPaint(Color.RED);
        lowerEndMarker.setStroke(solidStroke);
        lowerEndMarker.setLabel("[" + lowerBandValue + " - " + upperBandValue + "]" + "  visits = (" + b.getMarketVisitCount() + ")");
        lowerEndMarker.setLabelFont(labelFont);
        lowerEndMarker.setLabelPaint(Color.BLUE);
        //lowerEndMarker.setLabelAnchor(RectangleAnch.BOTTOM_RIGHT);
        lowerEndMarker.setLabelTextAnchor(TextAnchor.BOTTOM_LEFT);
        plot.addRangeMarker(lowerEndMarker);
    }


    public void processCandle(String timeframe, String unit)
    {

        series.clear();
        minClose = Double.MAX_VALUE;
        maxClose = Double.MIN_VALUE;

        if (unitCombo.getSelectedItem().toString().equals("HOUR"))
        {
            if (timeframeCombo.getSelectedItem().toString().equals("4"))
            {
                List<CandleDto> candles4Hour = this.candleBuilder.getCandles().stream()
                        .filter(candleDto ->
                                candleDto.getTimeFramesUnit()
                                        .getUnit()
                                        .equals(TimeFramesUnit.HOUR.getUnit()) && candleDto.getInterval().intValue() == 4).toList();
                for (CandleDto candle : candles4Hour)
                {
                    LocalDateTime dateTime = LocalDateTime.ofInstant(
                            candle.getTime(),
                            ZoneId.systemDefault()
                    );
                    double close = candle.getClose();
                    minClose = Math.min(minClose, close);
                    maxClose = Math.max(maxClose, close);
                    series.addOrUpdate(
                            new Minute(Date.from(dateTime.atZone(ZoneId.systemDefault()).toInstant())),
                            close
                    );
                }
            }

            if (timeframeCombo.getSelectedItem().toString().equals("1"))
            {
                List<CandleDto> candles1Hour = this.candleBuilder.getCandles().stream()
                        .filter(candleDto ->
                                candleDto.getTimeFramesUnit()
                                        .getUnit()
                                        .equals(TimeFramesUnit.HOUR.getUnit())
                                        && candleDto.getInterval().intValue() == 1)//
                        .toList();

                for (CandleDto candle : candles1Hour)
                {
                    LocalDateTime dateTime = LocalDateTime.ofInstant(
                            candle.getTime(),
                            ZoneId.systemDefault()
                    );
                    double close = candle.getClose();
                    minClose = Math.min(minClose, close);
                    maxClose = Math.max(maxClose, close);
                    series.addOrUpdate(
                            new Minute(Date.from(dateTime.atZone(ZoneId.systemDefault()).toInstant())),
                            close
                    );
                }
            }
        }

        if (unitCombo.getSelectedItem().toString().equals("MINUTE"))
        {
            if (timeframeCombo.getSelectedItem().toString().equals("1"))
            {
                List<CandleDto> candles1Minute = this.candleBuilder.getCandles().stream()
                        .filter(candleDto ->
                                candleDto.getTimeFramesUnit()
                                        .getUnit()
                                        .equals(TimeFramesUnit.MINUTE.getUnit()) && //
                                        candleDto.getInterval().intValue() == 1) //
                        .toList();

                for (CandleDto candle : candles1Minute)
                {
                    LocalDateTime dateTime = LocalDateTime.ofInstant(
                            candle.getTime(),
                            ZoneId.systemDefault()
                    );
                    double close = candle.getClose();
                    minClose = Math.min(minClose, close);
                    maxClose = Math.max(maxClose, close);
                    series.addOrUpdate(
                            new Minute(Date.from(dateTime.atZone(ZoneId.systemDefault()).toInstant())),
                            close
                    );
                }
            }
            if (timeframeCombo.getSelectedItem().toString().equals("5"))
            {
                List<CandleDto> candles5Minute = this.candleBuilder.getCandles().stream()
                        .filter(candleDto ->
                                candleDto.getTimeFramesUnit()
                                        .getUnit()
                                        .equals(TimeFramesUnit.MINUTE.getUnit()) && candleDto.getInterval().intValue() == 5).toList();
                for (CandleDto candle : candles5Minute)
                {
                    LocalDateTime dateTime = LocalDateTime.ofInstant(
                            candle.getTime(),
                            ZoneId.systemDefault()
                    );
                    double close = candle.getClose();
                    minClose = Math.min(minClose, close);
                    maxClose = Math.max(maxClose, close);
                    series.addOrUpdate(
                            new Minute(Date.from(dateTime.atZone(ZoneId.systemDefault()).toInstant())),
                            close
                    );
                }
            }

            if (timeframeCombo.getSelectedItem().toString().equals("15"))
            {
                List<CandleDto> candles15Minute = this.candleBuilder.getCandles().stream()
                        .filter(candleDto ->
                                candleDto.getTimeFramesUnit()
                                        .getUnit()
                                        .equals(TimeFramesUnit.MINUTE.getUnit()) && candleDto.getInterval().intValue() == 15).toList();
                for (CandleDto candle : candles15Minute)
                {
                    LocalDateTime dateTime = LocalDateTime.ofInstant(
                            candle.getTime(),
                            ZoneId.systemDefault()
                    );
                    double close = candle.getClose();
                    minClose = Math.min(minClose, close);
                    maxClose = Math.max(maxClose, close);
                    series.addOrUpdate(
                            new Minute(Date.from(dateTime.atZone(ZoneId.systemDefault()).toInstant())),
                            close
                    );
                }
            }
            if (timeframeCombo.getSelectedItem().toString().equals("30"))
            {
                List<CandleDto> candles30Minute = this.candleBuilder.getCandles().stream()
                        .filter(candleDto ->
                                candleDto.getTimeFramesUnit()
                                        .getUnit()
                                        .equals(TimeFramesUnit.MINUTE.getUnit()) && candleDto.getInterval().intValue() == 30).toList();
                for (CandleDto candle : candles30Minute)
                {
                    LocalDateTime dateTime = LocalDateTime.ofInstant(
                            candle.getTime(),
                            ZoneId.systemDefault()
                    );
                    double close = candle.getClose();
                    minClose = Math.min(minClose, close);
                    maxClose = Math.max(maxClose, close);
                    series.addOrUpdate(
                            new Minute(Date.from(dateTime.atZone(ZoneId.systemDefault()).toInstant())),
                            close
                    );
                }
            }

            NumberAxis yAxis =
                    (NumberAxis) ((XYPlot) chartPanel.getChart().getPlot()).getRangeAxis();

            yAxis.setLabelPaint(Color.RED);
            yAxis.setTickLabelPaint(Color.RED);

            DateAxis xAxis =
                    (DateAxis) ((XYPlot) chartPanel.getChart().getPlot()).getDomainAxis();

            xAxis.setTickLabelPaint(Color.GRAY);
            xAxis.setLabelPaint(Color.GRAY);

            autoFitChart();

        }
    }

    public void refresh()
    {
        SwingUtilities.invokeLater(() ->
        {

            processCandle(null, null);

            try
            {
                processBand();
            } catch (IOException e)
            {
                e.printStackTrace();
            }

            chartPanel.repaint();
        });
    }

    public void updatePlot()
    {
        processCandle(null, null);

        try
        {
            processBand();
        } catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    private void autoFitChart()
    {
        XYPlot plot = (XYPlot) chartPanel.getChart().getPlot();

        // Y Axis
        NumberAxis yAxis = (NumberAxis) plot.getRangeAxis();
        yAxis.setAutoRange(true);
        yAxis.setAutoRangeIncludesZero(false);
        yAxis.setLowerMargin(0.05);
        yAxis.setUpperMargin(0.05);

        // X Axis
        DateAxis xAxis = (DateAxis) plot.getDomainAxis();
        xAxis.setAutoRange(true);

        chartPanel.repaint();
    }

}