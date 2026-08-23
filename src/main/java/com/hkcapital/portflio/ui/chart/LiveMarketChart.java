package com.hkcapital.portflio.ui.chart;

import com.hkcapital.portflio.broker.etoro.config.TradingConfiguration;
import com.hkcapital.portflio.market.indicators.CandleDto;
import com.hkcapital.portflio.market.indicators.TimeFramesUnit;
import com.hkcapital.portflio.market.structure.MarketPriceBand;
import com.hkcapital.portflio.market.structure.MarketStructure;
import com.hkcapital.portflio.market.structure.MarketStructureCache;
import com.hkcapital.portflio.market.structure.MarketTypes;
import com.hkcapital.portflio.repository.registry.ServiceRegistery;
import com.hkcapital.portflio.service.candle.etoro.EtoroCandleService;
import com.hkcapital.portflio.service.candle.etoro.impl.SignalBuilder;
import org.jfree.chart.ChartMouseEvent;
import org.jfree.chart.ChartMouseListener;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.*;
import org.jfree.chart.panel.CrosshairOverlay;
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
import java.awt.geom.Rectangle2D;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.NavigableSet;

public class LiveMarketChart extends JFrame
{

    private final TimeSeries series;
    private final ChartPanel chartPanel;
    private JComboBox<String> timeframeCombo;
    private JComboBox<String> unitCombo;
    private JComboBox<String> bandsCombo;

    private JButton showHide = new JButton("Hide");

    private double minClose = 4300;
    private double maxClose = 4400;
    private Crosshair xCrosshair = new Crosshair(
            Double.NaN,
            Color.GRAY,
            new BasicStroke(1.0f)
    );

    private Crosshair yCrosshair = new Crosshair(
            Double.NaN,
            Color.GRAY,
            new BasicStroke(1.0f)
    );

    CrosshairOverlay crosshairOverlay = new CrosshairOverlay();
    XYPlot plot;
    private MarketStructureCache marketStructureCache;
    private SignalBuilder signalBuilder;
    private final EtoroCandleService etoroCandleService;
    private final ServiceRegistery serviceRegistery;
    DateAxis xAxis = new DateAxis("Time");
    NumberAxis yAxis = new NumberAxis("Close Price");

    public LiveMarketChart(MarketStructureCache marketStructureCache,
                           SignalBuilder signalBuilder,
                           ServiceRegistery serviceRegistery
    )
    {

        this.serviceRegistery = serviceRegistery;
        this.marketStructureCache = marketStructureCache;
        this.signalBuilder = signalBuilder;

        this.etoroCandleService = (EtoroCandleService) serviceRegistery.getService(EtoroCandleService.EtoroCandleService);

        setTitle("Live - Market Information of GOLD");
        // ================= DATA =================
        series = new TimeSeries("Close Price");
        TimeSeriesCollection dataset = new TimeSeriesCollection();
        dataset.addSeries(series);

        // ================= CHART =================

        List<CandleDto> candleDtoList =
                this.etoroCandleService.findCandleDtoByInstrumentIDAndCreationDateTimeBetween(18,
                        LocalDateTime.of(2026, 8, 14, 0, 0, 1),
                        LocalDateTime.of(2026, 8, 14, 23, 59, 59));

        candleDtoList.stream().mapToDouble(e -> e.getHigh()).max().ifPresent(e ->
        {
            maxClose = e;
        });

        candleDtoList.stream().mapToDouble(e -> e.getLow()).min().ifPresent(e ->
        {
            minClose = e;
        });

        yCrosshair.setLabelVisible(true);

        crosshairOverlay.addDomainCrosshair(xCrosshair);
        crosshairOverlay.addRangeCrosshair(yCrosshair);

        yAxis.setRange(minClose, maxClose);
        yAxis.setTickUnit(ChartUtil.createYaxisNumberTickUnit(TimeFramesUnit.MINUTE, 5));
        xAxis.setRange(ChartUtil.createDateRange(TimeFramesUnit.MINUTE, 5));
        xAxis.setTickUnit(new DateTickUnit(DateTickUnitType.MINUTE, 30));
        xAxis.setDateFormatOverride(new SimpleDateFormat("HH:mm"));

        CandlestickRenderer renderer = new CandlestickRenderer();
        renderer.setAutoWidthMethod(CandlestickRenderer.WIDTHMETHOD_SMALLEST);


        ArrayList<OHLCDataItem> ohlc = new ArrayList<>();

        candleDtoList.forEach(c ->
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

        chartPanel.addOverlay(crosshairOverlay);

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
        toolBar.add(showHide);
        // ================= LAYOUT =================
        setLayout(new BorderLayout());
        add(toolBar, BorderLayout.NORTH);
        add(chartPanel, BorderLayout.CENTER);
        setSize(1100, 700);
        setLocationRelativeTo(null);
        autoFitChart();

        chartPanel.addChartMouseListener(new ChartMouseListener()
        {
            @Override
            public void chartMouseMoved(ChartMouseEvent event)
            {
                if (event.getTrigger().getPoint() == null) return;

                Rectangle2D dataArea = chartPanel.getScreenDataArea();

                double x = plot.getDomainAxis().java2DToValue(
                        event.getTrigger().getX(),
                        dataArea,
                        plot.getDomainAxisEdge()
                );

                double y = plot.getRangeAxis().java2DToValue(
                        event.getTrigger().getY(),
                        dataArea,
                        plot.getRangeAxisEdge()
                );

                xCrosshair.setValue(x);
                yCrosshair.setValue(y);
            }

            @Override
            public void chartMouseClicked(ChartMouseEvent event)
            {
            }
        });

        showHide.addActionListener(e -> {
            TradingConfiguration.showHide();

            if(TradingConfiguration.SHOW_TRADING) {
                setVisible(true);
            } else {
                setVisible(false);
            }

        });
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
                    drawPlot(dataArray, ChartUtil.createYaxisNumberTickUnit(TimeFramesUnit.MINUTE, 1),
                            ChartUtil.createDateRange(TimeFramesUnit.MINUTE, 1),
                            ChartUtil.createXaxisNumberTickUnit(TimeFramesUnit.MINUTE, 1));
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
                    drawPlot(dataArray, ChartUtil.createYaxisNumberTickUnit(TimeFramesUnit.MINUTE, 5),
                            ChartUtil.createDateRange(TimeFramesUnit.MINUTE, 5),
                            ChartUtil.createXaxisNumberTickUnit(TimeFramesUnit.MINUTE, 5));
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
                    drawPlot(dataArray, ChartUtil.createYaxisNumberTickUnit(TimeFramesUnit.MINUTE, 15),
                            ChartUtil.createDateRange(TimeFramesUnit.MINUTE, 5),
                            ChartUtil.createXaxisNumberTickUnit(TimeFramesUnit.MINUTE, 15));
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
                    drawPlot(dataArray, ChartUtil.createYaxisNumberTickUnit(TimeFramesUnit.MINUTE, 30),
                            ChartUtil.createDateRange(TimeFramesUnit.MINUTE, 30),
                            ChartUtil.createXaxisNumberTickUnit(TimeFramesUnit.MINUTE, 30));
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
                    drawPlot(dataArray, ChartUtil.createYaxisNumberTickUnit(TimeFramesUnit.HOUR, 1),
                            ChartUtil.createDateRange(TimeFramesUnit.HOUR, 1),
                            ChartUtil.createXaxisNumberTickUnit(TimeFramesUnit.HOUR, 1));
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
                    drawPlot(dataArray, ChartUtil.createYaxisNumberTickUnit(TimeFramesUnit.HOUR, 4),
                            ChartUtil.createDateRange(TimeFramesUnit.HOUR, 4),
                            ChartUtil.createXaxisNumberTickUnit(TimeFramesUnit.HOUR, 4));
                }
            }
        });
    }

    private void drawPlot(org.jfree.data.xy.OHLCDataItem[] dataArray, //
                          NumberTickUnit yAxisTick, //
                          DateRange xAxisRange,
                          DateTickUnit xAxisTick)
    {
        org.jfree.data.xy.DefaultOHLCDataset dynamicDataset =
                new org.jfree.data.xy.DefaultOHLCDataset("Live Data", dataArray);
        plot.setDataset(dynamicDataset);
        yAxis.setRange(minClose, maxClose);
        yAxis.setTickUnit(yAxisTick);
        xAxis.setRange(xAxisRange);
        xAxis.setTickUnit(xAxisTick);
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