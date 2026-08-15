package com.hkcapital.portflio.ui.chart;

import com.hkcapital.portflio.market.structure.MarketPriceBand;
import com.hkcapital.portflio.market.structure.MarketStructure;
import com.hkcapital.portflio.market.structure.MarketStructureCache;
import com.hkcapital.portflio.market.structure.MarketTypes;
import com.hkcapital.portflio.service.candle.etoro.impl.SignalBuilder;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.*;
import org.jfree.chart.panel.CrosshairOverlay;
import org.jfree.chart.plot.Crosshair;
import org.jfree.chart.plot.ValueMarker;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.chart.ui.TextAnchor;
import org.jfree.data.time.DateRange;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.NavigableSet;

public class LiveMarketChartTest extends JFrame
{

    private final TimeSeries series;
    private final ChartPanel chartPanel;
    private JComboBox<String> timeframeCombo;
    private JComboBox<String> unitCombo;
    private JComboBox<String> bandsCombo;

    private double minClose = Double.MAX_VALUE;
    private double maxClose = Double.MIN_VALUE;
    private Crosshair yCrosshair;


    public static void main(String[] args)
    {
        LiveMarketChartTest f = new LiveMarketChartTest();
        f.setVisible(true);
    }

    public LiveMarketChartTest()
    {

        setTitle("Live - Market Information of GOLD");

        // ================= DATA =================
        series = new TimeSeries("Close Price");

        TimeSeriesCollection dataset = new TimeSeriesCollection();

        dataset.addSeries(series);

        // ================= CHART =================
        DateAxis xAxis = new DateAxis("Time");
        NumberAxis yAxis = new NumberAxis("Close Price");

        yAxis.setRange(4350, 4450);
        yAxis.setTickUnit(new NumberTickUnit(5));

        long now = System.currentTimeMillis();
        Date startTime = new Date(now);
        Date endTime = new Date(now + (2 * 60 * 60 * 1000));

        xAxis.setRange(new DateRange(startTime, endTime));
        xAxis.setTickUnit(new DateTickUnit(DateTickUnitType.MINUTE, 5));
        xAxis.setDateFormatOverride(new SimpleDateFormat("HH:mm"));

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

        setSize(1100, 700);
        setLocationRelativeTo(null);
        //processBand();
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


}