package com.hkcapital.portflio.ui.panels.srmatrix.panels;

import com.hkcapital.portflio.market.indicators.TimeFramesUnit;
import com.hkcapital.portflio.model.Instrument;
import com.hkcapital.portflio.model.SRMatrixTolerance;
import com.hkcapital.portflio.repository.registry.ServiceRegistery;
import com.hkcapital.portflio.service.instrument.InstrumentService;
import com.hkcapital.portflio.service.positions.PositionService;
import com.hkcapital.portflio.service.registry.Service;
import com.hkcapital.portflio.service.srmatrix.SRMatrixToleranceService;
import com.hkcapital.portflio.ui.UIBag;
import com.hkcapital.portflio.ui.buttons.ButtonLabels;
import com.hkcapital.portflio.ui.fields.NumberTextField;
import com.hkcapital.portflio.ui.panels.position.tablemodels.PositionTableModel;
import com.hkcapital.portflio.ui.panels.srmatrix.dialogues.SRMatrixToleranceEditDialogue;
import com.hkcapital.portflio.ui.panels.srmatrix.labels.Labels;
import com.hkcapital.portflio.ui.panels.srmatrix.tablemodels.SRMatrixToleranceTableModel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.LocalDateTime;
import java.util.List;

public class SRMatrixTolerancePanel extends UIBag
{
    private final ServiceRegistery<Service> serviceRegistery;
    private final SRMatrixToleranceService sRMatrixToleranceService;
    private final JLabel lSupportToleranceLabel = new JLabel("L.Support Tolerance.");
    private final NumberTextField lSupportTolerance = new NumberTextField(40);
    private final JLabel rSupportToleranceLabel = new JLabel("R.Support Tolerance.");
    private final NumberTextField rSupportTolerance = new NumberTextField(40);
    private final JLabel lResistanceToleranceLabel = new JLabel("L.Resistance Tolerance.");
    private final NumberTextField lResistanceTolerance = new NumberTextField(40);
    private final NumberTextField rResistanceTolerance = new NumberTextField(40);

    private final JLabel takeProfitLabel = new JLabel("Take Profit %");
    private final NumberTextField takeProfit = new NumberTextField(40);
    private final JLabel stopLossLabel = new JLabel("Stop loss %");
    private final NumberTextField stopLoss = new NumberTextField(40);

    private final JLabel timeFrameLabel = new JLabel("Timeframe");
    private final NumberTextField timeFrame = new NumberTextField(40);
    JComboBox<String> timeFrameUnit = new JComboBox<>(new String[]{TimeFramesUnit.MINUTE.getUnit(), TimeFramesUnit.HOUR.getUnit(), TimeFramesUnit.DAY.getUnit(), TimeFramesUnit.WEEK.getUnit()});
    private JComboBox<Instrument> instrumentList = new JComboBox<>();
    private final JLabel activeLabel = new JLabel("Active");
    private final JCheckBox active = new JCheckBox();
    private final JTable srMatrixTable;
    private final SRMatrixToleranceTableModel tableModel;
    private final JButton saveButton = new JButton(ButtonLabels.Save.getLabel());
    private final JButton cancelButton = new JButton(ButtonLabels.Cancel.getLabel());
    private final JButton closeButton = new JButton(ButtonLabels.Close.getLabel());
    private final JButton removeButton = new JButton(ButtonLabels.Remove.getLabel());
    private final JButton readButton = new JButton(ButtonLabels.Refresh.getLabel());
    private final JButton selectSrMatrix = new JButton(ButtonLabels.Select.getLabel());
    private final InstrumentService instrumentService;
    final SRMatrixSourcePanel srMatrixSourcePanel;

    public SRMatrixTolerancePanel(final ServiceRegistery serviceRegistery,
                                  final SRMatrixSourcePanel srMatrixSourcePanel,

                                  Integer positionId, Integer strategyId) //
    {
        this(serviceRegistery, srMatrixSourcePanel);
    }

    public SRMatrixTolerancePanel(final ServiceRegistery serviceRegistery,
                                  final SRMatrixSourcePanel srMatrixSourcePanel)
    {
        super(SRMatrixTolerancePanel.class);
        this.srMatrixSourcePanel = srMatrixSourcePanel;
        this.serviceRegistery = serviceRegistery;
        this.sRMatrixToleranceService = (SRMatrixToleranceService)this.serviceRegistery.getService(Service.SRMatrixToleranceService);
        this.instrumentService = (InstrumentService) serviceRegistery.getService(Service.InstrumentService);

        List<Instrument> instrumentList = instrumentService.findAll();

        for (Instrument instrument : instrumentList)
        {
            this.instrumentList.addItem(instrument);
        }

        tableModel = new SRMatrixToleranceTableModel<>(new String[]{Labels.Id.getLabel(), Labels.Name.getLabel(), "Date",
                "L.Support Tolerance %" , "R.Support Tolerance %","L.Resistance Tolerance %" ,
                "R.Resistance Tolerance %", "Take Profit %" , "Stop Loss %", "Time Frame", "TimeFrame Unite", "Active"
        }, //
        sRMatrixToleranceService.findAll());
        setLayout(new GridBagLayout());
        setBorder(BorderFactory.createTitledBorder(Labels.SRMatrix.getLabel()));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);

        // Row 0: Instrument label + text field
        JPanel srMatrixInputPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        srMatrixInputPanel.add(this.instrumentList);
        srMatrixInputPanel.add(lSupportToleranceLabel);
        srMatrixInputPanel.add(lSupportTolerance);
        srMatrixInputPanel.add(rSupportToleranceLabel);
        srMatrixInputPanel.add(rSupportTolerance);
        srMatrixInputPanel.add(lResistanceToleranceLabel);
        srMatrixInputPanel.add(lResistanceTolerance);
        JLabel rResistanceToleranceLabel = new JLabel("R.Resistance Tolerance.");
        srMatrixInputPanel.add(rResistanceToleranceLabel);
        srMatrixInputPanel.add(rResistanceTolerance);
        srMatrixInputPanel.add(takeProfitLabel);
        srMatrixInputPanel.add(takeProfit);
        srMatrixInputPanel.add(stopLossLabel);
        srMatrixInputPanel.add(stopLoss);
        srMatrixInputPanel.add(timeFrameLabel);
        srMatrixInputPanel.add(timeFrame);
        srMatrixInputPanel.add(timeFrameUnit);
        srMatrixInputPanel.add(activeLabel);
        srMatrixInputPanel.add(active);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.weightx = 1.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        add(srMatrixInputPanel, gbc);

        // Row 1: Buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        buttonPanel.add(saveButton);
        buttonPanel.add(removeButton);
        buttonPanel.add(cancelButton);
        buttonPanel.add(closeButton);
        buttonPanel.add(readButton);
        //buttonPanel.add(selectSrMatrix);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        gbc.weightx = 0;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.EAST;
        add(buttonPanel, gbc);
        // Row 2: Table inside scroll pane
        srMatrixTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(srMatrixTable);

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        add(scrollPane, gbc);
        saveButton.addActionListener(e -> save());
        removeButton.addActionListener(e -> remove());
        readButton.addActionListener(e -> sRMatrixToleranceService.findAll());

        closeButton.addActionListener(e ->
        {
            SwingUtilities.getWindowAncestor(this).dispose();
        });
        srMatrixTable.addMouseListener(new SRMatrixEditDialogueMouseClickHandler());
    }


    public class SRMatrixEditDialogueMouseClickHandler extends MouseAdapter
    {
        public SRMatrixEditDialogueMouseClickHandler()
        {

        }
        @Override
        public void mouseClicked(MouseEvent e)
        {
            super.mouseClicked(e);
            if (e.getClickCount() == 2) //
            {

                Integer srMatrixId = (Integer) srMatrixTable.getModel() //
                        .getValueAt(srMatrixTable.getSelectedRow(), 0);
                new SRMatrixToleranceEditDialogue(sRMatrixToleranceService, srMatrixId);
            }
        }
    }

    public void save()
    {
        SRMatrixTolerance sRMatrixTolerance = SRMatrixTolerance.builder()
                .creationDate(LocalDateTime.now())
                .timeFrame(this.timeFrame.getIntValue())
                .timeFrameUnit(this.timeFrameUnit.getSelectedItem().toString())
                .instrument((Instrument) this.instrumentList.getModel().getSelectedItem())
                .l_s_tolerance_percent(this.lSupportTolerance.getDoubleValue())
                .r_s_tolerance_percent(this.rSupportTolerance.getDoubleValue())
                .l_r_tolerance_percent(this.lResistanceTolerance.getDoubleValue())
                .r_r_tolerance_percent(this.rResistanceTolerance.getDoubleValue())
                .takeProfitPercent(this.takeProfit.getDoubleValue())
                .stopLossPercent(this.stopLoss.getDoubleValue())
                .active(this.active.isSelected())
                .build();
        sRMatrixToleranceService.addSRMatrixTolerance(sRMatrixTolerance);
        tableModel.addRow(sRMatrixTolerance);
    }

    public void remove()
    {
        int selectedRow = srMatrixTable.getSelectedRow();
        if (selectedRow >= 0)
        {
            SRMatrixTolerance sRMatrixTolerance = (SRMatrixTolerance) tableModel.removeRow(selectedRow);
            sRMatrixToleranceService.removeSRMatrixTolerance(sRMatrixTolerance);
        } else
        {
            JOptionPane.showMessageDialog(this, "Please select an SRMatrix to remove.",
                    "No Selection", JOptionPane.WARNING_MESSAGE);
        }
    }

    @Override
    protected void paintComponent(Graphics g)
    {
        super.paintComponent(g);
    }


//    public void selectSrMatrix()
//    {
//        int selectedRow = srMatrixTable.getSelectedRow();
//        SRMatrix srMatrix = (SRMatrix) tableModel.getElements().get(selectedRow);
//        if (positionId != null)
//        {
//            Position position = positionService.findById(positionId);
//            SRMatrixTolerance srm = sRMatrixToleranceService.getReferenceById(srMatrix.getId());
//            positionService.updatePosition(position);
//            List<Position> positionList = positionService.findByStrategyId(strategyId);
//            positionTableModel.updateData(positionList);
//        } else
//        {
//            srMatrixSourcePanel.getId().setText(srMatrix.getId().toString());
//            srMatrixSourcePanel.getSupport().setText(srMatrix.getSupport().toString());
//            srMatrixSourcePanel.getResistance().setText(srMatrix.getResistance().toString());
//            srMatrixSourcePanel.getTimeFrame().setText(srMatrix.getTimeFrame().toString());
//            srMatrixSourcePanel.getTimeFrameUnit().setText(srMatrix.getTimeFrameUnit());
//            srMatrixSourcePanel.getActive().setSelected(srMatrix.getActive());
//        }
//        SwingUtilities.getWindowAncestor(this).dispose();
//    }
}
