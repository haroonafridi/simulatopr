package com.hkcapital.portflio.ui.panels.instrument.dialogues;

import com.hkcapital.portflio.market.structure.MarketTypes;
import com.hkcapital.portflio.model.InstrumentMarketStructureConf;
import com.hkcapital.portflio.service.instrumentmarketstructureconf.InstrumentMarketStructureConfService;
import com.hkcapital.portflio.ui.fields.NumberTextField;
import com.hkcapital.portflio.ui.panels.instrument.labels.Labels;

import javax.swing.*;
import java.awt.*;
import java.util.Arrays;
import java.util.Optional;

public class InstrumentConfEditDialogue extends JDialog
{

    private final InstrumentMarketStructureConfService instMarktStrConfService;
    private final Integer instConfId;
    private final InstrumentMarketStructureConf instMrktConf;

    private final JLabel moduleLabel = new JLabel("Module:");
    private final NumberTextField module = new NumberTextField(40);
    private final JLabel subLabel = new JLabel("Sub:");
    private final NumberTextField sub = new NumberTextField(40);

    private final JLabel intervalLabel = new JLabel("Interval:");
    private final NumberTextField intrvl = new NumberTextField(40);

    private final JLabel trendStructureNameLabel = new JLabel(Labels.TrendStructureName.getLabel());
    private JComboBox<MarketTypes> trendStructName =
            new JComboBox<>(MarketTypes.values());

    private final JLabel orderBlockDepthLabel = new JLabel(Labels.OrderBlockDepth.getLabel());
    private final NumberTextField orderBlockDepth = new NumberTextField(40);
    private final JCheckBox active = new JCheckBox("Active");

    private final JButton save = new JButton("Save");
    private final JButton cancel = new JButton("Cancel");

    public InstrumentConfEditDialogue(JPanel owner, InstrumentMarketStructureConfService instMarktStrConfService, Integer instConfId)
    {
        super(SwingUtilities.getWindowAncestor(owner),
                "Configuration for Instrument = [" + instMarktStrConfService.findById(instConfId).getInstrument().getName() + "]",
                ModalityType.APPLICATION_MODAL);

        this.instMarktStrConfService = instMarktStrConfService;
        this.instConfId = instConfId;
        this.instMrktConf = instMarktStrConfService.findById(instConfId);

        initializeValues();
        setupLayout();
        setupListeners();

        setSize(800, 600);
        setLocationRelativeTo(owner);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setVisible(true);
    }

    private void initializeValues()
    {
        Optional<MarketTypes> marketType = Arrays.stream(MarketTypes.values())
                .filter(v -> v.getValue().equals(instMrktConf.getTrendStructureName()))
                .findFirst();
        module.setText(String.valueOf(instMrktConf.getModule()));
        sub.setText(String.valueOf(instMrktConf.getSub()));
        intrvl.setText(String.valueOf(instMrktConf.getIntrvl()));
        trendStructName.setSelectedItem(marketType.isPresent() ? marketType.get() : null);
        orderBlockDepth.setText(String.valueOf(instMrktConf.getOrderBlockDepth()));
        active.setSelected(instMrktConf.isActive());
    }

    private void setupLayout()
    {
        setLayout(new BorderLayout());

        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Row 0: Module
        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(moduleLabel, gbc);
        gbc.gridx = 1;
        panel.add(module, gbc);

        // Row 1: Sub
        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(subLabel, gbc);
        gbc.gridx = 1;
        panel.add(sub, gbc);

        // Row 2: Interval
        gbc.gridx = 0;
        gbc.gridy = 2;
        panel.add(intervalLabel, gbc);
        gbc.gridx = 1;
        panel.add(intrvl, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        panel.add(trendStructureNameLabel, gbc);
        gbc.gridx = 1;
        panel.add(trendStructName, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        panel.add(orderBlockDepthLabel, gbc);
        gbc.gridx = 1;
        panel.add(orderBlockDepth, gbc);

        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.gridwidth = 1;
        panel.add(active, gbc);
        add(panel, BorderLayout.CENTER);

        // Button Panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 20, 20));
        buttonPanel.add(save);
        buttonPanel.add(cancel);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    private void setupListeners()
    {
        save.addActionListener(e -> save());
        cancel.addActionListener(e -> dispose());
    }

    public void save()
    {
        try
        {
            MarketTypes selectedTrendStructure =
                    (MarketTypes) trendStructName.getSelectedItem();
            instMrktConf.setModule(Integer.parseInt(module.getText().trim()));
            instMrktConf.setSub(Integer.parseInt(sub.getText().trim()));
            instMrktConf.setIntrvl(Integer.parseInt(intrvl.getText().trim()));
            instMrktConf.setActive(active.isSelected());
            instMrktConf.setTrendStructureName(selectedTrendStructure.getValue());
            instMrktConf.setOrderBlockDepth(orderBlockDepth.getIntValue());
            instMarktStrConfService.add(instMrktConf);
            dispose();
        } catch (NumberFormatException ex)
        {
            JOptionPane.showMessageDialog(this,
                    "Please enter valid numeric values for Module, Sub, Interval and Order Block Depth",
                    "Invalid Input",
                    JOptionPane.ERROR_MESSAGE);
        }
    }
}