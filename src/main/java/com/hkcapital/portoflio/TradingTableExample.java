package com.hkcapital.portoflio;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.Random;

public class TradingTableExample {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new TradingTableExample().createAndShowGUI();
        });
    }

    private void createAndShowGUI() {
        JFrame frame = new JFrame("Trading Portfolio");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Table model
        String[] columnNames = {"Symbol", "Quantity", "Price"};
        Object[][] data = {
                {"GOLD", 10, 2000.0},
                {"NASDAQ", 5, 16000.0}
        };

        DefaultTableModel model = new DefaultTableModel(data, columnNames) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // prevent editing by user
            }
        };

        JTable table = new JTable(model);
        frame.add(new JScrollPane(table), BorderLayout.CENTER);

        // Random price generator (for demo)
        Random rand = new Random();

        // Timer to update prices every second
        Timer timer = new Timer(200, e -> {
            for (int row = 0; row < model.getRowCount(); row++) {
                double oldPrice = (double) model.getValueAt(row, 2);
                // simulate price change
                double newPrice = oldPrice + (rand.nextDouble() - 0.5) * 10;
                model.setValueAt(Math.round(newPrice * 100.0) / 100.0, row, 2);
            }
        });
        timer.start();

        frame.setSize(400, 200);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
