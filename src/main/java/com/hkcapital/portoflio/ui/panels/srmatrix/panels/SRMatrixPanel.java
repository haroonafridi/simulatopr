package com.hkcapital.portoflio.ui.panels.srmatrix.panels;

import com.hkcapital.portoflio.model.Instrument;
import com.hkcapital.portoflio.model.Position;
import com.hkcapital.portoflio.model.SRMatrix;
import com.hkcapital.portoflio.repository.ServiceRegistery;
import com.hkcapital.portoflio.service.InstrumentService;
import com.hkcapital.portoflio.service.PositionService;
import com.hkcapital.portoflio.service.SRMatrixService;
import com.hkcapital.portoflio.service.Service;
import com.hkcapital.portoflio.ui.UIBag;
import com.hkcapital.portoflio.ui.buttons.ButtonLabels;
import com.hkcapital.portoflio.ui.fields.NumberTextField;
import com.hkcapital.portoflio.ui.panels.position.tablemodels.PositionTableModel;
import com.hkcapital.portoflio.ui.panels.srmatrix.dialogues.SRMatrixEditDialogue;
import com.hkcapital.portoflio.ui.panels.srmatrix.labels.Labels;
import com.hkcapital.portoflio.ui.panels.srmatrix.tablemodels.SRMatrixTableModel;

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
    private final JLabel supportLabel = new JLabel("Support");
    private final NumberTextField support = new NumberTextField(40);
    private final JLabel resistenceLabel = new JLabel("Resistence");
    private final NumberTextField resistence = new NumberTextField(40);

    private final JLabel timeFrameLabel = new JLabel("Timeframe");
    private final NumberTextField timeFrame = new NumberTextField(40);

    JComboBox<String> timeFrameUnit = new JComboBox<>(new String[]{"MINUTE", "HOUR", "DAY", "WEEK"});

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
                "Support", "Resistance", "Time Frame", "TimeFrame Unite", "Active"
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
        srMatrixInputPanel.add(resistenceLabel);
        srMatrixInputPanel.add(resistence);
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
                // Frame frame = (Frame) SwingUtilities.getWindowAncestor(null);
                new SRMatrixEditDialogue(srMatrixService, srMatrixId);
            }
        }
    }

    public void save()
    {

        SRMatrix srMatrix = new SRMatrix(LocalDateTime.now(), //
                this.timeFrame.getIntValue(), //
                this.timeFrameUnit.getSelectedItem().toString(), //
                (Instrument) this.instrumentList.getModel().getSelectedItem(), //
                this.support.getDoubleValue(), //
                this.resistence.getDoubleValue(),
                this.active.isSelected());

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
            srMatrixSourcePanel.getResistence().setText(srMatrix.getResistance().toString());
            srMatrixSourcePanel.getTimeFrame().setText(srMatrix.getTimeFrame().toString());
            srMatrixSourcePanel.getTimeFrameUnit().setText(srMatrix.getTimeFrameUnit());
            srMatrixSourcePanel.getActive().setSelected(srMatrix.getActive());
        }

        SwingUtilities.getWindowAncestor(this).dispose();
    }
}
