package com.hkcapital.portflio.ui.panels.instrument.panels;

import com.hkcapital.portflio.market.indicators.TimeFramesUnit;
import com.hkcapital.portflio.market.structure.MarketTypes;
import com.hkcapital.portflio.model.Instrument;
import com.hkcapital.portflio.model.InstrumentMarketStructureConf;
import com.hkcapital.portflio.repository.registry.ServiceRegistery;
import com.hkcapital.portflio.service.instrument.InstrumentService;
import com.hkcapital.portflio.service.instrumentmarketstructureconf.InstrumentMarketStructureConfService;
import com.hkcapital.portflio.service.registry.Service;
import com.hkcapital.portflio.ui.UIBag;
import com.hkcapital.portflio.ui.buttons.ButtonLabels;
import com.hkcapital.portflio.ui.fields.NumberTextField;
import com.hkcapital.portflio.ui.panels.instrument.dialogues.InstrumentConfEditDialogue;
import com.hkcapital.portflio.ui.panels.instrument.labels.Labels;
import com.hkcapital.portflio.ui.panels.instrument.tablemodels.InstrumentConfTableModel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.LocalDateTime;
import java.util.List;

public class InstrumentConfPanel extends UIBag
{

    private final ServiceRegistery<Service> serviceRegistery;

    private final InstrumentService instrumentService;
    private final InstrumentMarketStructureConfService instMarktStrConfService;
    private JComboBox<String> timeFrameUnit = new JComboBox<>(new String[]{TimeFramesUnit.MINUTE.getUnit(), //
            TimeFramesUnit.HOUR.getUnit(), TimeFramesUnit.DAY.getUnit(), //
            TimeFramesUnit.WEEK.getUnit()});

    private final JLabel structureNameLabel = new JLabel("Structure Name");
    private JComboBox<MarketTypes> marketStructName =
            new JComboBox<>(MarketTypes.values());

    private JComboBox<Instrument> instrumentList = new JComboBox<>();
    private final JLabel marketOrderLabel = new JLabel("Market Order");
    private final NumberTextField marketOrder = new NumberTextField(40);
    private final JLabel timeFrameLabel = new JLabel("Timeframe");
    private final NumberTextField timeFrame = new NumberTextField(40);
    private final JLabel moduleLabel = new JLabel("Module:");
    private final NumberTextField module = new NumberTextField(30);
    private final JLabel subLabel = new JLabel("Sub:");
    private final NumberTextField sub = new NumberTextField(30);
    private final JLabel intervalLabel = new JLabel("Interval:");
    private final NumberTextField intrvl = new NumberTextField(30);


    private final JLabel rsiOverSoldLabel = new JLabel(Labels.RsiOverSold.getLabel());
    private final NumberTextField rsiOverSold = new NumberTextField(30);

    private final JLabel rsiOverBoughtLabel = new JLabel(Labels.RsiOverBought.getLabel());
    private final NumberTextField rsiOverBought = new NumberTextField(30);

    private final JLabel rsiBearishFromLabel = new JLabel(Labels.RsiBearishFrom.getLabel());
    private final NumberTextField rsiBearishFrom = new NumberTextField(30);


    private final JLabel rsiBearishToLabel = new JLabel(Labels.RsiBearishTo.getLabel());
    private final NumberTextField rsiBearishTo = new NumberTextField(30);
    private final JLabel rsiBullishFromLabel = new JLabel(Labels.RsiBullishFrom.getLabel());
    private final NumberTextField rsiBullishFrom = new NumberTextField(30);

    private final JLabel rsiBullishToLabel = new JLabel(Labels.RsiBullishTo.getLabel());
    private final NumberTextField rsiBullishTo = new NumberTextField(30);

    private final JLabel atrLabel = new JLabel(Labels.Atr.getLabel());
    private final NumberTextField atr = new NumberTextField(30);

    private final JLabel orderBlockDepthLabel = new JLabel(Labels.OrderBlockDepth.getLabel());
    private final NumberTextField orderBlockDepth = new NumberTextField(30);
    private final JCheckBox active = new JCheckBox("Active");

    private final JLabel trendStructureNameLabel = new JLabel("Trend Structure Name");
    private JComboBox<MarketTypes> trendStructName =
            new JComboBox<>(MarketTypes.values());

    private final JTable instrumentConfTable;
    private final InstrumentConfTableModel instrumentConfTableModel;
    private final JButton saveButton = new JButton(ButtonLabels.Save.getLabel());
    private final JButton cancelButton = new JButton(ButtonLabels.Cancel.getLabel());
    private final JButton closeButton = new JButton(ButtonLabels.Close.getLabel());
    private final JButton removeButton = new JButton(ButtonLabels.Remove.getLabel());
    private final JButton readButton = new JButton(ButtonLabels.Refresh.getLabel());

    public InstrumentConfPanel(final ServiceRegistery serviceRegistery)
    {
        super(InstrumentConfPanel.class);
        this.serviceRegistery = serviceRegistery;
        this.instrumentService = (InstrumentService) this.serviceRegistery.getService(Service.InstrumentService);
        this.instMarktStrConfService = (InstrumentMarketStructureConfService) this.serviceRegistery.getService(Service.InstrumentMarketStructureConfService);

        instrumentConfTableModel = new InstrumentConfTableModel<>(new String[]{Labels.Id.getLabel(),
                Labels.Ticker.getLabel(),
                Labels.Order.getLabel(),
                Labels.TimeFrame.getLabel(),
                Labels.TimeFrameUnit.getLabel(),
                Labels.Module.getLabel(), //
                Labels.Sub.getLabel(),
                Labels.Interval.getLabel(),
                Labels.StructureName.getLabel(),
                Labels.TrendStructureName.getLabel() ,
                Labels.OrderBlockDepth.getLabel() ,
                Labels.RsiOverSold.getLabel(),
                Labels.RsiOverBought.getLabel(),
                Labels.RsiBearishFrom.getLabel(),
                Labels.RsiBearishTo.getLabel(),
                Labels.RsiBullishFrom.getLabel(),
                Labels.RsiBullishTo.getLabel(),
                Labels.Atr.getLabel(),
                Labels.Active.getLabel()
        }, //
                instMarktStrConfService.findAll());

        List<Instrument> instrumentList = instrumentService.findAll();

        for (Instrument instrument : instrumentList)
        {
            this.instrumentList.addItem(instrument);
        }

        setLayout(new GridBagLayout());
        setBorder(BorderFactory.createTitledBorder(Labels.InstrumentPanel.getLabel()));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);

        // Split inputs into Row 1 and Row 2 panels for clean organization
        JPanel row1Panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        row1Panel.add(this.structureNameLabel);
        row1Panel.add(this.marketStructName);
        row1Panel.add(this.marketOrderLabel);
        row1Panel.add(this.marketOrder);
        row1Panel.add(this.instrumentList);
        row1Panel.add(this.timeFrameLabel);
        row1Panel.add(this.timeFrame);
        row1Panel.add(this.timeFrameUnit);
        row1Panel.add(this.moduleLabel);
        row1Panel.add(this.module);
        row1Panel.add(this.subLabel);
        row1Panel.add(this.sub);
        row1Panel.add(this.intervalLabel);
        row1Panel.add(this.intrvl);

        JPanel row2Panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        row2Panel.add(this.trendStructureNameLabel);
        row2Panel.add(this.trendStructName);
        row2Panel.add(this.orderBlockDepthLabel);
        row2Panel.add(this.orderBlockDepth);
        row2Panel.add(this.rsiOverSoldLabel);
        row2Panel.add(this.rsiOverSold);
        row2Panel.add(this.rsiOverBoughtLabel);
        row2Panel.add(this.rsiOverBought);
        row2Panel.add(this.rsiBearishFromLabel);
        row2Panel.add(this.rsiBearishFrom);
        row2Panel.add(this.rsiBearishToLabel);
        row2Panel.add(this.rsiBearishTo);
        row2Panel.add(this.rsiBullishFromLabel);
        row2Panel.add(this.rsiBullishFrom);
        row2Panel.add(this.rsiBullishToLabel);
        row2Panel.add(this.rsiBullishTo);
        row2Panel.add(this.atrLabel);
        row2Panel.add(this.atr);
        row2Panel.add(this.active);

        // Main container holding both rows vertically
        JPanel instrumentConfInputPanel = new JPanel();
        instrumentConfInputPanel.setLayout(new BoxLayout(instrumentConfInputPanel, BoxLayout.Y_AXIS));
        row1Panel.setAlignmentX(Component.LEFT_ALIGNMENT);
        row2Panel.setAlignmentX(Component.LEFT_ALIGNMENT);
        instrumentConfInputPanel.add(row1Panel);
        instrumentConfInputPanel.add(Box.createVerticalStrut(5)); // small vertical gap
        instrumentConfInputPanel.add(row2Panel);

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.weightx = 1.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        add(instrumentConfInputPanel, gbc);

        // Row 1 (Buttons) -> now at gridy = 1
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        buttonPanel.add(saveButton);
        buttonPanel.add(removeButton);
        buttonPanel.add(cancelButton);
        buttonPanel.add(closeButton);
        buttonPanel.add(readButton);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        gbc.weightx = 0;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.EAST;
        add(buttonPanel, gbc);

        // Row 2 (Table) -> now at gridy = 2
        instrumentConfTable = new JTable(instrumentConfTableModel);
        JScrollPane scrollPane = new JScrollPane(instrumentConfTable);

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0; // allows table to expand vertically
        gbc.fill = GridBagConstraints.BOTH;
        add(scrollPane, gbc);

        saveButton.addActionListener(e -> save());
        removeButton.addActionListener(e -> remove());
        readButton.addActionListener(e ->
        {
            InstrumentConfTableModel instConfTableModel =
                    new InstrumentConfTableModel<>(new String[]{Labels.Id.getLabel(),
                            Labels.Ticker.getLabel(), Labels.TimeFrame.getLabel(),
                            Labels.TimeFrameUnit.getLabel(), Labels.Module.getLabel(), //
                            Labels.Sub.getLabel(),
                            Labels.StructureName.getLabel(),
                            Labels.TrendStructureName.getLabel() ,
                            Labels.OrderBlockDepth.getLabel() ,
                            Labels.RsiOverSold.getLabel(),
                            Labels.RsiOverBought.getLabel(),
                            Labels.RsiBearishFrom.getLabel(),
                            Labels.RsiBearishTo.getLabel(),
                            Labels.RsiBullishFrom.getLabel(),
                            Labels.RsiBullishTo.getLabel(),
                            Labels.Atr.getLabel(),
                            Labels.Active.getLabel()}, //
                            instMarktStrConfService.findByInstrumentAndActiveOrdeyByMarketOrder((Instrument) this.instrumentList.getSelectedItem(), true));

            this.instrumentConfTable.setModel(instConfTableModel);
        });
        closeButton.addActionListener(e ->
        {
            SwingUtilities.getWindowAncestor(this).dispose();
        });

        instrumentConfTable.addMouseListener(new MouseClickHandler(this));

    }

    public class MouseClickHandler extends MouseAdapter
    {
        private JPanel frame;

        public MouseClickHandler(JPanel frame)
        {
            this.frame = frame;
        }

        @Override
        public void mouseClicked(MouseEvent e)
        {
            if (e.getClickCount() == 2)
            {
                Integer instrumentId = (Integer) instrumentConfTable.getModel() //
                        .getValueAt(instrumentConfTable.getSelectedRow(), 0);
                new InstrumentConfEditDialogue(frame, instMarktStrConfService, instrumentId);
            }
        }
    }


    public void save()
    {
        int timeFrame = this.timeFrame.getIntValue();
        String timeFrameUnit = (String) this.timeFrameUnit.getSelectedItem();
        int marketOrder = this.marketOrder.getIntValue();
        int module = this.module.getIntValue();
        int sub = this.sub.getIntValue();
        int intrvl = this.intrvl.getIntValue();
        boolean active = this.active.isSelected();
        int orderBlockDepth = this.orderBlockDepth.getIntValue();

        Double rsiOverSold = this.rsiOverSold.getDoubleValue();
        Double rsiOverBought = this.rsiOverBought.getDoubleValue();

        Double rsiBearishFrom = this.rsiBearishFrom.getDoubleValue();
        Double rsiBearishTo = this.rsiBearishTo.getDoubleValue();

        Double rsiBullishFrom = this.rsiBullishFrom.getDoubleValue();
        Double rsiBullishTo = this.rsiBullishTo.getDoubleValue();

        Double atr = this.atr.getDoubleValue();

        final Instrument inst = (Instrument) this.instrumentList.getSelectedItem();

        MarketTypes selectedStructure =
                (MarketTypes) marketStructName.getSelectedItem();

        MarketTypes selectedTrendStructure =
                (MarketTypes) trendStructName.getSelectedItem();

        final InstrumentMarketStructureConf instMrktStrConf = InstrumentMarketStructureConf.builder()
                .structureName(selectedStructure.getValue())
                .instrument(inst)//
                .marketOrder(marketOrder)//
                .timeFrame(timeFrame)//
                .timeFrameUnit(timeFrameUnit)//
                .module(module)//
                .sub(sub)//
                .intrvl(intrvl)//
                .active(active)//
                .trendStructureName(selectedTrendStructure.getValue())
                .structureName(selectedStructure.getValue())
                .orderBlockDepth(orderBlockDepth)
                .rsiOverSold(rsiOverSold)
                .rsiOverBought(rsiOverBought)
                .rsiBearishFrom(rsiBearishFrom)
                .rsiBearishTo(rsiBearishTo)
                .rsiBullishFrom(rsiBullishFrom)
                .rsiBullishTo(rsiBullishTo)
                .atr(atr)
                .creationDate(LocalDateTime.now())//
                .build();
        instMarktStrConfService.add(instMrktStrConf);
        instrumentConfTableModel.addRow(instMrktStrConf);
    }

    public void remove()
    {
        int selectedRow = instrumentConfTable.getSelectedRow();
        if (selectedRow >= 0)
        {
            instMarktStrConfService.remove((InstrumentMarketStructureConf) instrumentConfTableModel.removeRow(selectedRow));
        } //
        else
        {
            JOptionPane.showMessageDialog(this, "Please select an instrumentTicker configuration to remove.", "No Selection", JOptionPane.WARNING_MESSAGE);
        }
    }

    @Override
    protected void paintComponent(Graphics g)
    {
        super.paintComponent(g); // always call super
    }
}