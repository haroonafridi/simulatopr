package com.hkcapital.portflio.ui.chart;

import com.hkcapital.portflio.market.indicators.CandleDto;
import com.hkcapital.portflio.market.indicators.TimeFramesUnit;
import com.hkcapital.portflio.market.structure.MarketPriceBand;
import com.hkcapital.portflio.market.structure.MarketStructure;
import com.hkcapital.portflio.market.structure.MarketStructureCache;
import com.hkcapital.portflio.market.structure.MarketTypes;
import com.hkcapital.portflio.service.candle.etoro.EtoroCandleService;
import com.hkcapital.portflio.service.candle.etoro.impl.SignalBuilder;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.*;
import org.jfree.chart.plot.Crosshair;
import org.jfree.chart.plot.ValueMarker;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.CandlestickRenderer;
import org.jfree.chart.ui.TextAnchor;
import org.jfree.data.time.DateRange;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.xy.DefaultOHLCDataset;
import org.jfree.data.xy.OHLCDataItem;

import javax.swing.*;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.NavigableSet;

public class LiveMarketChart extends JFrame
{

    private final TimeSeries series;
    private final ChartPanel chartPanel;
    private JComboBox<String> timeframeCombo;
    private JComboBox<String> unitCombo;
    private JComboBox<String> bandsCombo;

    private double minClose = 4200d;
    private double maxClose = 4500d;
    private Crosshair yCrosshair;
    XYPlot plot;
    private MarketStructureCache marketStructureCache;
    private SignalBuilder signalBuilder;
    private final EtoroCandleService etoroCandleService;

    DateAxis xAxis = new DateAxis("Time");
    NumberAxis yAxis = new NumberAxis("Close Price");

    public LiveMarketChart(MarketStructureCache marketStructureCache,
                           SignalBuilder signalBuilder,
                           EtoroCandleService etoroCandleService
    )
    {

        this.marketStructureCache = marketStructureCache;
        this.signalBuilder = signalBuilder;
        this.etoroCandleService = etoroCandleService;

        setTitle("Live - Market Information of GOLD");
        // ================= DATA =================
        series = new TimeSeries("Close Price");
        TimeSeriesCollection dataset = new TimeSeriesCollection();
        dataset.addSeries(series);

        // ================= CHART =================

        yAxis.setRange(minClose, maxClose);
        yAxis.setTickUnit(new NumberTickUnit(10));
        LocalDate today = LocalDate.now();
        Date startOfDay = Date.from(today.atStartOfDay(ZoneId.systemDefault()).plusHours(10).toInstant());
        Date endOfDay = Date.from(today.atTime(23, 59, 59).atZone(ZoneId.systemDefault()).toInstant());
        xAxis.setRange(new DateRange(startOfDay, endOfDay));
        xAxis.setTickUnit(new DateTickUnit(DateTickUnitType.MINUTE, 30));
        xAxis.setDateFormatOverride(new SimpleDateFormat("HH:mm"));

        CandlestickRenderer renderer = new CandlestickRenderer();
        renderer.setAutoWidthMethod(CandlestickRenderer.WIDTHMETHOD_SMALLEST);

        ArrayList<OHLCDataItem> ohlc = new ArrayList<>();

        this.signalBuilder.getCandleBuilder1Min().getCandles().forEach(c ->
        {
            Date candleDate = new Date(c.getTime().toEpochMilli());
            ohlc.add(new OHLCDataItem(
                    candleDate,   // Dynamic date
                    c.getOpen(),  // Dynamic open
                    c.getHigh(),  // Dynamic high
                    c.getLow(),   // Dynamic low
                    c.getClose(), // Dynamic close
                    0
            ));
        });
        OHLCDataItem[] dataArray = ohlc.toArray(new OHLCDataItem[0]);
        // 4. Create the final dataset using the array
        DefaultOHLCDataset ds = new DefaultOHLCDataset("Data", dataArray);

        plot = new XYPlot(ds, xAxis, yAxis, renderer);

        JFreeChart chart = new JFreeChart(
                "Gold Live Chart",
                JFreeChart.DEFAULT_TITLE_FONT,
                plot,
                true
        );

        chartPanel = new ChartPanel(chart);

        String[] timeframes = {"1", "5", "15", "30", "4"};

        timeframeCombo = new JComboBox<>(timeframes);
        timeframeCombo.setSelectedItem("1"); // DEFAULT
        String[] units = {"MINUTE", "HOUR", "DAY"};
        unitCombo = new JComboBox<>(units);
        unitCombo.setSelectedItem("MINUTE");

        String[] bands = {"UPPER", "LOWER"};
        bandsCombo = new JComboBox<>(bands);
        bandsCombo.setSelectedItem("UPPER");
        JPanel toolBar = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        toolBar.add(new JLabel("Timeframe:"));
        toolBar.add(timeframeCombo);
        toolBar.add(new JLabel("Unit:"));
        toolBar.add(unitCombo);
        toolBar.add(bandsCombo);
        // ================= LAYOUT =================
        setLayout(new BorderLayout());
        add(toolBar, BorderLayout.NORTH);
        add(chartPanel, BorderLayout.CENTER);
        setSize(1100, 700);
        setLocationRelativeTo(null);
        autoFitChart();
    }

    public void display()
    {
        this.setVisible(true);

    }


    public void handleMarketTick()
    {
        // Always modify Swing components on the Event Dispatch Thread (EDT)
        SwingUtilities.invokeLater(() ->
        {
            org.jfree.data.xy.OHLCDataItem[] dataArray = null;
            java.util.List<CandleDto> candles = null;

            if (unitCombo.getSelectedItem().equals(TimeFramesUnit.MINUTE.getUnit()))
            {
                if (timeframeCombo.getSelectedItem().equals("1"))
                {
                    candles =
                            signalBuilder.getCandleBuilder1Min().candles();
                    dataArray = candles.stream()
                            .map(c -> new org.jfree.data.xy.OHLCDataItem(
                                    new java.util.Date(c.getTime().toEpochMilli()),
                                    c.getOpen(),
                                    c.getHigh(),
                                    c.getLow(),
                                    c.getClose(),
                                    0
                            ))
                            .toArray(org.jfree.data.xy.OHLCDataItem[]::new);
                    processBand();
                    drawPlot(dataArray, 2, 30);
                }

                if (timeframeCombo.getSelectedItem().equals("5"))
                {
                    candles =
                            signalBuilder.getCandleBuilder5Min().candles();
                    dataArray = candles.stream()
                            .map(c -> new org.jfree.data.xy.OHLCDataItem(
                                    new java.util.Date(c.getTime().toEpochMilli()),
                                    c.getOpen(),
                                    c.getHigh(),
                                    c.getLow(),
                                    c.getClose(),
                                    0
                            ))
                            .toArray(org.jfree.data.xy.OHLCDataItem[]::new);
                    processBand();
                    drawPlot(dataArray, 4, 30);
                }


                if (timeframeCombo.getSelectedItem().equals("15"))
                {
                    candles =
                            signalBuilder.getCandleBuilder15Min().candles();
                    dataArray = candles.stream()
                            .map(c -> new org.jfree.data.xy.OHLCDataItem(
                                    new java.util.Date(c.getTime().toEpochMilli()),
                                    c.getOpen(),
                                    c.getHigh(),
                                    c.getLow(),
                                    c.getClose(),
                                    0
                            ))
                            .toArray(org.jfree.data.xy.OHLCDataItem[]::new);
                    processBand();
                    drawPlot(dataArray, 8, 30);
                }


                if (timeframeCombo.getSelectedItem().equals("30"))
                {
                    candles =
                            signalBuilder.getCandleBuilder30Min().candles();
                    dataArray = candles.stream()
                            .map(c -> new org.jfree.data.xy.OHLCDataItem(
                                    new java.util.Date(c.getTime().toEpochMilli()),
                                    c.getOpen(),
                                    c.getHigh(),
                                    c.getLow(),
                                    c.getClose(),
                                    0
                            ))
                            .toArray(org.jfree.data.xy.OHLCDataItem[]::new);
                    processBand();
                    drawPlot(dataArray, 15, 30);
                }
            }

            if (unitCombo.getSelectedItem().equals(TimeFramesUnit.HOUR.getUnit()))
            {
                if (timeframeCombo.getSelectedItem().equals("1"))
                {
                    candles =
                            signalBuilder.getCandleBuilder1Hour().candles();
                    dataArray = candles.stream()
                            .map(c -> new org.jfree.data.xy.OHLCDataItem(
                                    new java.util.Date(c.getTime().toEpochMilli()),
                                    c.getOpen(),
                                    c.getHigh(),
                                    c.getLow(),
                                    c.getClose(),
                                    0
                            ))
                            .toArray(org.jfree.data.xy.OHLCDataItem[]::new);
                    processBand();
                    drawPlot(dataArray, 20, 30);
                }

                if (timeframeCombo.getSelectedItem().equals("4"))
                {
                    candles =
                            signalBuilder.getCandleBuilder1Hour().candles();
                    dataArray = candles.stream()
                            .map(c -> new org.jfree.data.xy.OHLCDataItem(
                                    new java.util.Date(c.getTime().toEpochMilli()),
                                    c.getOpen(),
                                    c.getHigh(),
                                    c.getLow(),
                                    c.getClose(),
                                    0
                            ))
                            .toArray(org.jfree.data.xy.OHLCDataItem[]::new);
                    processBand();
                    drawPlot(dataArray, 40, 30);
                }
            }
        });
    }

    private void drawPlot(org.jfree.data.xy.OHLCDataItem[] dataArray, int yAxixTick, int xAxisTick)
    {
        org.jfree.data.xy.DefaultOHLCDataset dynamicDataset =
                new org.jfree.data.xy.DefaultOHLCDataset("Live Data", dataArray);
        plot.setDataset(dynamicDataset);
        LocalDate today = LocalDate.now();
        Date startOfDay = Date.from(today.atStartOfDay(ZoneId.systemDefault()).plusDays(-1).toInstant());
        Date endOfDay = Date.from(today.atTime(23, 59, 59).atZone(ZoneId.systemDefault()).toInstant());
        yAxis.setRange(4300, 4400);
        yAxis.setTickUnit(new NumberTickUnit(yAxixTick));
        xAxis.setRange(new DateRange(startOfDay, endOfDay));
        xAxis.setTickUnit(new DateTickUnit(DateTickUnitType.MINUTE, xAxisTick));
        xAxis.setDateFormatOverride(new SimpleDateFormat("HH:mm"));
    }


    public void processBand()
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

                NavigableSet<MarketPriceBand> bands = marketStructure5Min.getUpperBands();

                if (this.bandsCombo.getSelectedItem().toString().equals("LOWER"))
                {
                    bands = marketStructure5Min.getLowerBands();
                }
                bands.forEach(b ->
                {
                    drawBands(plot, b);
                });
            }

            if (timeframeCombo.getSelectedItem().toString().equals("15") && marketStructure15Min != null)
            {
                NavigableSet<MarketPriceBand> bands = marketStructure15Min.getUpperBands();

                if (this.bandsCombo.getSelectedItem().toString().equals("LOWER"))
                {
                    bands = marketStructure15Min.getLowerBands();
                }
                bands.forEach(b ->
                {
                    drawBands(plot, b);
                });
            }
            if (timeframeCombo.getSelectedItem().toString().equals("30") && marketStructure30Min != null)
            {
                NavigableSet<MarketPriceBand> bands = marketStructure30Min.getUpperBands();

                if (this.bandsCombo.getSelectedItem().toString().equals("LOWER"))
                {
                    bands = marketStructure30Min.getLowerBands();
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
        lowerEndMarker.setLabelTextAnchor(TextAnchor.BOTTOM_LEFT);
        plot.addRangeMarker(lowerEndMarker);
    }

    public void updatePlot()
    {
        maxClose = signalBuilder.getCandleBuilder1Min().candles().stream().mapToDouble(c -> c.getHigh()).max().getAsDouble();
        minClose = signalBuilder.getCandleBuilder1Min().candles().stream().mapToDouble(c -> c.getHigh()).min().getAsDouble();
        //autoFitChart();
    }

    private void autoFitChart()
    {
        XYPlot plot = (XYPlot) chartPanel.getChart().getPlot();
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