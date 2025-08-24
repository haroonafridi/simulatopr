package com.hkcapital.portoflio.ui.panels;

import com.hkcapital.portoflio.model.*;
import com.hkcapital.portoflio.ui.fields.NumberTextField;

import javax.swing.*;
import java.awt.*;

public class PositionActionsPanel extends JPanel {

    private final JButton addPosition =  new JButton("Add Position");
    private final JButton removePosition =  new JButton("Remove Position");
    private final JButton removePositionAll =  new JButton("Remove All Positions.");
    private static Instrument[] instruments = {new Instrument(1, "GOLD"), //
            new Instrument(2, "NASDAQ"), new Instrument(3, "CRUD OIL"), //
            new Instrument(4, "Nikkei")};

    private Label caiptalLabel = new Label("Capital:");
    private NumberTextField runningCapital = new NumberTextField();
    private JPanel buttonPanel = new JPanel();
    private JPanel positionPanel = new JPanel();

    private RunningCapitalPanel runningCapitalPanel = new RunningCapitalPanel(new RunningCapital(1, 5000));

    private MarketConditionsPanel marketConditionsPanel;

    private final JLabel instrumentLabel = new JLabel("Instrument:");
    private final JLabel positionSizeLabel = new JLabel("Position size in %:");
    private final NumberTextField positionSize = new NumberTextField(30, 2);

    private final JComboBox<Instrument> instrumentComboBox = new JComboBox<>(instruments);

    public PositionActionsPanel() {
        setLayout(new FlowLayout(FlowLayout.CENTER, 20, 15));
        setBorder(BorderFactory.createTitledBorder("Positions Panel"));
        buttonPanel.setBorder(BorderFactory.createTitledBorder("Actions"));
        buttonPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 20, 15));
        positionPanel.setBorder(BorderFactory.createTitledBorder("Position Parameters:"));
        marketConditionsPanel = new MarketConditionsPanel((Instrument) instrumentComboBox.getModel().getSelectedItem(),
                3321.15,3378.40, 1.06);
        marketConditionsPanel.setBorder(BorderFactory.createTitledBorder("Market Conditions"));
        buttonPanel.add(addPosition);
        buttonPanel.add(removePosition);
        buttonPanel.add(removePositionAll);
        add(positionPanel);
        add(buttonPanel);
        add(marketConditionsPanel);
        add(runningCapitalPanel);
        positionPanel.add(instrumentLabel);
        positionPanel.add(instrumentComboBox);
        positionPanel.add(positionSizeLabel);
        positionPanel.add(positionSize);
        instrumentComboBox.setRenderer(new DefaultListCellRenderer() {
            @Override
            public java.awt.Component getListCellRendererComponent(JList<?> list, Object value, int index,
                                                                   boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof Instrument) {
                    setText(((Instrument) value).getName());
                }
                return this;
            }
        });
    }

    public JButton getAddPosition()
    {
        return addPosition;
    }


    public JButton getRemovePosition()
    {
        return removePosition;
    }

    public JButton getRemoveAllPositions()
    {
        return removePositionAll;
    }

    public JComboBox<Instrument> getInstrumentComboBox() {
        return instrumentComboBox;
    }


    public Position getPosition()
    {
        return new Position(null, (Instrument)instrumentComboBox.getModel().getSelectedItem(),
                positionSize.getDoubleValue());
    }

    public MarketConditions getMarketConditions()
    {
        return marketConditionsPanel.getMarketConditions();
    }

    public RunningCapitalPanel getRunningCapitalPanel()
    {
        return runningCapitalPanel;
    }

    public void setRunningCapitalPanel(RunningCapitalPanel runningCapitalPanel)
    {
        this.runningCapitalPanel = runningCapitalPanel;
    }
}
