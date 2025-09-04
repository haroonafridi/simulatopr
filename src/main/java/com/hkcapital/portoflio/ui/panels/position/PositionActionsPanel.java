package com.hkcapital.portoflio.ui.panels.position;

import com.hkcapital.portoflio.model.*;
import com.hkcapital.portoflio.service.InstrumentService;
import com.hkcapital.portoflio.service.MarketConditionsService;
import com.hkcapital.portoflio.ui.fields.NumberTextField;
import com.hkcapital.portoflio.ui.panels.RunningCapitalPanel;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class PositionActionsPanel extends JPanel {

    private final JButton addPosition =  new JButton("Add Position");
    private final JButton removePosition =  new JButton("Remove Position");
    private final JButton removePositionAll =  new JButton("Remove All Positions.");
    private static List<Instrument> instruments;

    private Label caiptalLabel = new Label("Capital:");
    private NumberTextField runningCapital = new NumberTextField();
    private JPanel buttonPanel = new JPanel();
    private JPanel positionPanel = new JPanel();

    private RunningCapitalPanel runningCapitalPanel = new RunningCapitalPanel(new RunningCapital(1, 5000));

    private final JLabel instrumentLabel = new JLabel("Instrument:");
    private final JLabel positionSizeLabel = new JLabel("Position size in %:");
    private final NumberTextField positionSize = new NumberTextField(30, 2);

    private  JComboBox<Instrument> instrumentComboBox = new JComboBox<>();

    private  JComboBox<MarketConditions> marketConditionsComboBox = new JComboBox<>();

    private final InstrumentService instrumentService;

    private final MarketConditionsService marketConditionsService;

    public PositionActionsPanel(final InstrumentService instrumentService,
                                final MarketConditionsService marketConditionsService)
    {
        this.instrumentService = instrumentService;
        this.marketConditionsService = marketConditionsService;
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
        add(runningCapitalPanel);
        positionPanel.add(instrumentLabel);
        positionPanel.add(instrumentComboBox);
        positionPanel.add(marketConditionsComboBox);
        positionPanel.add(positionSizeLabel);
        positionPanel.add(positionSize);

        for (Instrument instrument : instrumentService.findAll())
        {
            instrumentComboBox.addItem(instrument);
        }

        for (MarketConditions marketCondition : marketConditionsService.findAll())
        {
            marketConditionsComboBox.addItem(marketCondition);
        }
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


        marketConditionsComboBox.setRenderer(new DefaultListCellRenderer() {
            @Override
            public java.awt.Component getListCellRendererComponent(JList<?> list, Object value, int index,
                                                                   boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof MarketConditions) {
                    setText(((MarketConditions) value).toString());
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



    public RunningCapitalPanel getRunningCapitalPanel()
    {
        return runningCapitalPanel;
    }

    public void setRunningCapitalPanel(RunningCapitalPanel runningCapitalPanel)
    {
        this.runningCapitalPanel = runningCapitalPanel;
    }
}
