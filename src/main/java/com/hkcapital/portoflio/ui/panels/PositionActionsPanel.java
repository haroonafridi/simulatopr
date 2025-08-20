package com.hkcapital.portoflio.ui.panels;

import com.hkcapital.portoflio.model.Instrument;
import com.hkcapital.portoflio.model.Position;
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
    private JPanel buttonPanel = new JPanel();
    private JPanel positionPanel = new JPanel();

    private final JLabel instrumentLabel = new JLabel("Instrument:");
    private final JLabel positionSizeLabel = new JLabel("Position size in %:");
    private final NumberTextField positionSize = new NumberTextField(30);
    private final JComboBox<Instrument> instrumentComboBox = new JComboBox<>(instruments);

    public PositionActionsPanel() {
        setLayout(new FlowLayout(FlowLayout.CENTER, 20, 15));
        setBorder(BorderFactory.createTitledBorder("Positions Panel"));
        buttonPanel.setBorder(BorderFactory.createTitledBorder("Actions"));
        buttonPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 20, 15));
        positionPanel.setBorder(BorderFactory.createTitledBorder("Position Parameters:"));
        buttonPanel.add(addPosition);
        buttonPanel.add(removePosition);
        buttonPanel.add(removePositionAll);
        add(positionPanel);
        add(buttonPanel);
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
                    setText(((Instrument) value).name());
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
}
