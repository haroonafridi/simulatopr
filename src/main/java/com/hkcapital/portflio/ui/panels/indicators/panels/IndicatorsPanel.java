package com.hkcapital.portflio.ui.panels.indicators.panels;

import com.hkcapital.portflio.market.indicators.TimeFramesUnit;
import com.hkcapital.portflio.model.Configuration;
import com.hkcapital.portflio.model.Instrument;
import com.hkcapital.portflio.model.TradingIndicators;
import com.hkcapital.portflio.repository.registry.ServiceRegistery;
import com.hkcapital.portflio.service.instrument.InstrumentService;
import com.hkcapital.portflio.service.registry.Service;
import com.hkcapital.portflio.service.tradingindicators.TradingIndicatorsService;
import com.hkcapital.portflio.ui.UIBag;
import com.hkcapital.portflio.ui.buttons.ButtonLabels;
import com.hkcapital.portflio.ui.fields.NumberTextField;
import com.hkcapital.portflio.ui.panels.indicators.labels.Labels;
import com.hkcapital.portflio.ui.panels.indicators.tablemodels.IndicatorsTableModel;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class IndicatorsPanel extends UIBag
{

    private final ServiceRegistery<Service> serviceRegister;
    private final TradingIndicatorsService tradingIndicatorsService;
    private final InstrumentService instrumentService;
    private final JLabel instrumentLabel = new JLabel(Labels.Instrument.getLabel());
    private JComboBox<Instrument> instrumentList = new JComboBox<>();
    private final JLabel nameLabel = new JLabel(Labels.Name.getLabel());
    private final TextField name = new TextField(40);
    private final JLabel periodLabel = new JLabel(Labels.Period.getLabel());
    private final NumberTextField period = new NumberTextField(30);

    private final JLabel timeFrameLabel = new JLabel(Labels.TimeFrame.getLabel());
    private final NumberTextField timeFrame = new NumberTextField(30);

    private final JLabel timeFrameUnitLabel = new JLabel(Labels.TimeFrameUnit.getLabel());
    private JComboBox<String> timeFrameUnit = new JComboBox<>(new String[]{TimeFramesUnit.MINUTE.getUnit(), //
            TimeFramesUnit.HOUR.getUnit(), TimeFramesUnit.DAY.getUnit(), //
            TimeFramesUnit.WEEK.getUnit()});

    private final JTable indicatorsTable;
    private final IndicatorsTableModel tableModel;

    private final JButton saveButton = new JButton(ButtonLabels.Save.getLabel());
    private final JButton cancelButton = new JButton(ButtonLabels.Cancel.getLabel());
    private final JButton closeButton = new JButton(ButtonLabels.Close.getLabel());
    private final JButton removeButton = new JButton(ButtonLabels.Remove.getLabel());
    private final JButton selectButton = new JButton(ButtonLabels.Select.getLabel());
    private final JButton readButton = new JButton(ButtonLabels.Refresh.getLabel());

    public IndicatorsPanel(final ServiceRegistery<Service> serviceRegistery)
    {
        super(IndicatorsPanel.class);
        this.serviceRegister = serviceRegistery;
        this.tradingIndicatorsService = (TradingIndicatorsService) serviceRegister.getService(Service.TradingIndicatorsService);
        this.instrumentService = (InstrumentService) this.serviceRegister.getService(Service.InstrumentService);

        tableModel = new IndicatorsTableModel<TradingIndicators>(tradingIndicatorsService.findAll());

        List<Instrument> instrumentList = instrumentService.findByActiveAndWithCandle(true, true);

        for (Instrument instrument : instrumentList)
        {
            this.instrumentList.addItem(instrument);
        }

        setLayout(new GridBagLayout());
        setBorder(BorderFactory.createTitledBorder("Trading Indicators Panel"));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Configuration Fields Panel
        JPanel tradingIndicatorPanel = new JPanel(new GridBagLayout());
        GridBagConstraints innerGbc = new GridBagConstraints();
        innerGbc.insets = new Insets(4, 4, 4, 4);
        innerGbc.anchor = GridBagConstraints.WEST;
        innerGbc.fill = GridBagConstraints.HORIZONTAL;
        innerGbc.gridx = 0;
        innerGbc.gridy = 0;

        tradingIndicatorPanel.add(instrumentLabel, innerGbc);
        innerGbc.gridx = 1;

        tradingIndicatorPanel.add(this.instrumentList, innerGbc);
        innerGbc.gridx = 0;
        innerGbc.gridy++;

        tradingIndicatorPanel.add(nameLabel, innerGbc);
        innerGbc.gridx = 1;
        tradingIndicatorPanel.add(name, innerGbc);
        innerGbc.gridx = 0;
        innerGbc.gridy++;
        tradingIndicatorPanel.add(periodLabel, innerGbc);
        innerGbc.gridx = 1;
        tradingIndicatorPanel.add(period, innerGbc);
        innerGbc.gridx = 0;
        innerGbc.gridy++;
        tradingIndicatorPanel.add(timeFrameUnitLabel, innerGbc);
        innerGbc.gridx = 1;
        tradingIndicatorPanel.add(timeFrameUnit, innerGbc);
        innerGbc.gridx = 0;
        innerGbc.gridy++;
        tradingIndicatorPanel.add(timeFrameLabel, innerGbc);
        innerGbc.gridx = 1;
        tradingIndicatorPanel.add(timeFrame, innerGbc);

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.weightx = 1.0;
        add(tradingIndicatorPanel, gbc);

        // Buttons Panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        buttonPanel.add(saveButton);
        buttonPanel.add(removeButton);
        buttonPanel.add(cancelButton);
        buttonPanel.add(closeButton);
        buttonPanel.add(selectButton);
        buttonPanel.add(readButton);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        gbc.weightx = 0;
        gbc.anchor = GridBagConstraints.EAST;
        add(buttonPanel, gbc);

        // Table
        indicatorsTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(indicatorsTable);

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        add(scrollPane, gbc);

        saveButton.addActionListener(e -> save());
        cancelButton.addActionListener(e -> getGraphics().dispose());
        removeButton.addActionListener(e -> remove());

    }

    public JButton getSaveButton()
    {
        return saveButton;
    }

    public JButton getCancelButton()
    {
        return cancelButton;
    }

    public JButton getCloseButton()
    {
        return closeButton;
    }

    public JButton getRemoveButton()
    {
        return removeButton;
    }

    public void save()
    {
        TradingIndicators tradingIndicators = new TradingIndicators((Instrument) instrumentList.getSelectedItem(),
                name.getText(),
                timeFrame.getIntValue(),
                timeFrameUnit.getSelectedItem().toString(),
                period.getIntValue()
        );
        tableModel.addRow(tradingIndicatorsService.save(tradingIndicators));
    }

    public void remove()
    {
        TradingIndicators tradingIndicators = (TradingIndicators) tableModel.removeRow(indicatorsTable.getSelectedRow());
        tradingIndicatorsService.remove(tradingIndicators);
    }


    @Override
    protected void paintComponent(Graphics g)
    {
        super.paintComponent(g); // always call super
    }
}
