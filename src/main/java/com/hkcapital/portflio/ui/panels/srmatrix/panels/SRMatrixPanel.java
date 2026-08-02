package com.hkcapital.portflio.ui.panels.srmatrix.panels;

import com.hkcapital.portflio.market.indicators.TimeFramesUnit;
import com.hkcapital.portflio.model.Instrument;
import com.hkcapital.portflio.model.Position;
import com.hkcapital.portflio.model.SRMatrix;
import com.hkcapital.portflio.repository.registry.ServiceRegistery;
import com.hkcapital.portflio.service.instrument.InstrumentService;
import com.hkcapital.portflio.service.positions.PositionService;
import com.hkcapital.portflio.service.registry.Service;
import com.hkcapital.portflio.service.srmatrix.SRMatrixService;
import com.hkcapital.portflio.ui.UIBag;
import com.hkcapital.portflio.ui.buttons.ButtonLabels;
import com.hkcapital.portflio.ui.fields.NumberTextField;
import com.hkcapital.portflio.ui.panels.position.tablemodels.PositionTableModel;
import com.hkcapital.portflio.ui.panels.srmatrix.dialogues.SRMatrixEditDialogue;
import com.hkcapital.portflio.ui.panels.srmatrix.labels.Labels;
import com.hkcapital.portflio.ui.panels.srmatrix.tablemodels.SRMatrixTableModel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.LocalDateTime;
import java.util.List;

public class SRMatrixPanel extends UIBag
{
    private final ServiceRegistery<Service> serviceRegistery;
    private final SRMatrixService srMatrixService;
    private final JLabel supportLabel = new JLabel("Support.");
    private final JLabel lSupportToleranceLabel = new JLabel("L.Support Tolerance.");
    private final NumberTextField lSupportTolerance = new NumberTextField(40);
    private final JLabel rSupportToleranceLabel = new JLabel("R.Support Tolerance.");
    private final NumberTextField rSupportTolerance = new NumberTextField(40);
    private final NumberTextField support = new NumberTextField(40);
    private final JLabel resistenceLabel = new JLabel("Resistance");
    private final JLabel lResistanceToleranceLabel = new JLabel("L.Resistance Tolerance.");
    private final NumberTextField lResistanceTolerance = new NumberTextField(40);
    private final JLabel rResistanceToleranceLabel = new JLabel("R.Resistance Tolerance.");
    private final NumberTextField rResistanceTolerance = new NumberTextField(40);
    private final NumberTextField resistance = new NumberTextField(40);
    private final JLabel timeFrameLabel = new JLabel("Timeframe");
    private final NumberTextField timeFrame = new NumberTextField(40);

    JComboBox<String> timeFrameUnit = new JComboBox<>(new String[]{TimeFramesUnit.MINUTE.getUnit(), TimeFramesUnit.HOUR.getUnit(), TimeFramesUnit.DAY.getUnit(), TimeFramesUnit.WEEK.getUnit()});

    private JComboBox<Instrument> instrumentList = new JComboBox<>();

    private final JLabel activeLable = new JLabel("Active");
    private final JCheckBox active = new JCheckBox();

    private final JTable srMatrixTable;
    private final SRMatrixTableModel tableModel;

    private final JButton saveButton = new JButton(ButtonLabels.Save.getLabel());
    private final JButton cancelButton = new JButton(ButtonLabels.Cancel.getLabel());
    private final JButton closeButton = new JButton(ButtonLabels.Close.getLabel());
    private final JButton removeButton = new JButton(ButtonLabels.Remove.getLabel());

    private final JButton readButton = new JButton(ButtonLabels.Refresh.getLabel());

    private final JButton selectSrMatrix = new JButton(ButtonLabels.Select.getLabel());

    private final InstrumentService instrumentService;
    final SRMatrixSourcePanel srMatrixSourcePanel;

    final PositionService positionService;

    private Integer positionId;
    private Integer strategyId;

    private PositionTableModel positionTableModel;

    public SRMatrixPanel(final ServiceRegistery serviceRegistery,
                         final SRMatrixSourcePanel srMatrixSourcePanel, PositionTableModel positionTableModel, //
                         Integer positionId, Integer strategyId) //
    {
        this(serviceRegistery, srMatrixSourcePanel);
        this.positionId = positionId;
        this.strategyId = strategyId;
        this.positionTableModel = positionTableModel;
    }

    public SRMatrixPanel(final ServiceRegistery serviceRegistery,
                         final SRMatrixSourcePanel srMatrixSourcePanel)
    {
        super(SRMatrixPanel.class);
        this.srMatrixSourcePanel = srMatrixSourcePanel;
        this.serviceRegistery = serviceRegistery;
        this.srMatrixService = (SRMatrixService) this.serviceRegistery.getService(Service.SRMatrixService);
        this.instrumentService = (InstrumentService) serviceRegistery.getService(Service.InstrumentService);
        this.positionService = (PositionService) serviceRegistery.getService(Service.PositionService);

        List<Instrument> instrumentList = instrumentService.findAll();

        for (Instrument instrument : instrumentList)
        {
            this.instrumentList.addItem(instrument);

        }

        tableModel = new SRMatrixTableModel<>(new String[]{Labels.Id.getLabel(), Labels.Name.getLabel(), "Date",
                "Support", "L.Support Tolerance" , "R.Support Tolerance", "Resistance","L.Resistance Tolerance" ,
                "R.Resistance Tolerance", "Time Frame", "TimeFrame Unite", "Active"
        }, //
                srMatrixService.findAll());

        setLayout(new GridBagLayout());
        setBorder(BorderFactory.createTitledBorder(Labels.SRMatrix.getLabel()));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);

        // Row 0: Instrument label + text field
        JPanel srMatrixInputPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        srMatrixInputPanel.add(this.instrumentList);
        srMatrixInputPanel.add(supportLabel);
        srMatrixInputPanel.add(support);
        srMatrixInputPanel.add(lSupportToleranceLabel);
        srMatrixInputPanel.add(lSupportTolerance);
        srMatrixInputPanel.add(rSupportToleranceLabel);
        srMatrixInputPanel.add(rSupportTolerance);
        srMatrixInputPanel.add(resistenceLabel);
        srMatrixInputPanel.add(resistance);
        srMatrixInputPanel.add(lResistanceToleranceLabel);
        srMatrixInputPanel.add(lResistanceTolerance);
        srMatrixInputPanel.add(rResistanceToleranceLabel);
        srMatrixInputPanel.add(rResistanceTolerance);
        srMatrixInputPanel.add(timeFrameLabel);
        srMatrixInputPanel.add(timeFrame);
        srMatrixInputPanel.add(timeFrameUnit);
        srMatrixInputPanel.add(activeLable);
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
        buttonPanel.add(selectSrMatrix);

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
        gbc.weighty = 1.0; // allows table to expand vertically
        gbc.fill = GridBagConstraints.BOTH;
        add(scrollPane, gbc);
        saveButton.addActionListener(e -> save());
        removeButton.addActionListener(e -> remove());
        readButton.addActionListener(e -> srMatrixService.findAll());
        cancelButton.addActionListener(e -> clear());
        selectSrMatrix.addActionListener(e -> selectSrMatrix());

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
                new SRMatrixEditDialogue(srMatrixService, srMatrixId);
            }
        }
    }

    public void save()
    {
        SRMatrix srMatrix = SRMatrix.builder()
                .creationDate(LocalDateTime.now())
                .timeFrame(this.timeFrame.getIntValue())
                .timeFrameUnit(this.timeFrameUnit.getSelectedItem().toString())
                .instrument((Instrument) this.instrumentList.getModel().getSelectedItem())
                .support(this.support.getDoubleValue())
                .l_s_tolerance(this.lSupportTolerance.getDoubleValue())
                .r_s_tolerance(this.rSupportTolerance.getDoubleValue())
                .resistance(this.resistance.getDoubleValue())
                .l_r_tolerance(this.lResistanceTolerance.getDoubleValue())
                .r_r_tolerance(this.rResistanceTolerance.getDoubleValue())
                .active(this.active.isSelected())
                .build();
        srMatrixService.addSRMatrix(srMatrix);
        tableModel.addRow(srMatrix);
    }

    public void remove()
    {
        int selectedRow = srMatrixTable.getSelectedRow();
        if (selectedRow >= 0)
        {
            SRMatrix srMatrix = (SRMatrix) tableModel.removeRow(selectedRow);
            srMatrixService.removeSRMatrix(srMatrix);
        } else
        {
            JOptionPane.showMessageDialog(this, "Please select an SRMatrix to remove.",
                    "No Selection", JOptionPane.WARNING_MESSAGE);
        }
    }

    public void clear()
    {
        //instrumentName.setText(null);
    }


    @Override
    protected void paintComponent(Graphics g)
    {
        super.paintComponent(g);
    }


    public void selectSrMatrix()
    {
        int selectedRow = srMatrixTable.getSelectedRow();
        SRMatrix srMatrix = (SRMatrix) tableModel.getElements().get(selectedRow);
        if (positionId != null)
        {
            Position position = positionService.findById(positionId);
            SRMatrix srm = srMatrixService.getReferenceById(srMatrix.getId());
            position.setSrMatrix(srm);
            positionService.updatePosition(position);
            List<Position> positionList = positionService.findByStrategyId(strategyId);
            positionTableModel.updateData(positionList);
        } else
        {
            srMatrixSourcePanel.getId().setText(srMatrix.getId().toString());
            srMatrixSourcePanel.getSupport().setText(srMatrix.getSupport().toString());
            srMatrixSourcePanel.getResistance().setText(srMatrix.getResistance().toString());
            srMatrixSourcePanel.getTimeFrame().setText(srMatrix.getTimeFrame().toString());
            srMatrixSourcePanel.getTimeFrameUnit().setText(srMatrix.getTimeFrameUnit());
            srMatrixSourcePanel.getActive().setSelected(srMatrix.getActive());
        }

        SwingUtilities.getWindowAncestor(this).dispose();
    }
}
