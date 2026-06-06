package com.hkcapital.portflio;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.category.DefaultCategoryDataset;

import javax.swing.*;
import java.util.List;

public class BandChartApp {

    // --- DTO ---
    public static class Band {
        public String bandKey;
        public String bandType;
        public double lowerBound;
        public double upperBound;
        public int marketVisitCount;
    }

    public static void main(String[] args) throws Exception {

        String json = """
        [
           {
              "bandKey":"low_4530_4540",
              "bandType":"LOW",
              "lowerBound":4530.0,
              "marketVisitCount":3,
              "upperBound":4540.0
           },
           {
              "bandKey":"low_4540_4550",
              "bandType":"LOW",
              "lowerBound":4540.0,
              "marketVisitCount":6,
              "upperBound":4550.0
           },
           {
              "bandKey":"low_4550_4560",
              "bandType":"LOW",
              "lowerBound":4550.0,
              "marketVisitCount":12,
              "upperBound":4560.0
           },
           {
              "bandKey":"low_4560_4570",
              "bandType":"LOW",
              "lowerBound":4560.0,
              "marketVisitCount":6,
              "upperBound":4570.0
           },
           {
              "bandKey":"low_4570_4580",
              "bandType":"LOW",
              "lowerBound":4570.0,
              "marketVisitCount":3,
              "upperBound":4580.0
           },
           {
              "bandKey":"low_4580_4590",
              "bandType":"LOW",
              "lowerBound":4580.0,
              "marketVisitCount":0,
              "upperBound":4590.0
           },
           {
              "bandKey":"low_4650_4660",
              "bandType":"LOW",
              "lowerBound":4650.0,
              "marketVisitCount":1,
              "upperBound":4660.0
           }
        ]
        """;

        // 1. Parse JSON
        ObjectMapper mapper = new ObjectMapper();
        List<Band> bands = mapper.readValue(json, new TypeReference<List<Band>>() {});

        // 2. Build dataset
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();

        for (Band b : bands) {
            String label = b.bandKey; // or: b.lowerBound + "-" + b.upperBound
            dataset.addValue(b.marketVisitCount, "Visits", label);
        }

        // 3. Create chart
        JFreeChart chart = ChartFactory.createBarChart(
                "Market Band Visits",
                "Price Band",
                "Visit Count",
                dataset
        );

        // 4. Swing UI
        ChartPanel chartPanel = new ChartPanel(chart);
        JFrame frame = new JFrame("Band Chart Viewer");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setContentPane(chartPanel);
        frame.setSize(1200, 600);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}