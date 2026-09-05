package com.hkcapital.portflio.ui.panels.instrument.panels;

import com.hkcapital.portflio.model.Instrument;
import com.hkcapital.portflio.repository.registry.ServiceRegistery;
import com.hkcapital.portflio.service.instrument.InstrumentService;
import com.hkcapital.portflio.service.registry.Service;
import com.hkcapital.portflio.ui.UIBag;
import com.hkcapital.portflio.ui.buttons.ButtonLabels;
import com.hkcapital.portflio.ui.fields.NumberTextField;
import com.hkcapital.portflio.ui.panels.instrument.dialogues.InstrumentEditDialogue;
import com.hkcapital.portflio.ui.panels.instrument.labels.Labels;
import com.hkcapital.portflio.ui.panels.instrument.tablemodels.InstrumentTableModel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class InstrumentPanel extends UIBag
{

    private final ServiceRegistery<Service> srvRgstry;

    private final InstrumentService insServ;

    private final JLabel tckrLbl = new JLabel(Labels.Ticker.getLabel());
    private final JTextField ticker = new JTextField(30);

    private final JLabel etoroIdLbl = new JLabel(Labels.Name.getLabel());
    private final NumberTextField etoroId = new NumberTextField(30);
    private final JLabel instNameLbl = new JLabel(Labels.Name.getLabel());
    private final JTextField instName = new JTextField(30);
    private final JTable instTable;
    private final InstrumentTableModel tableModel;

    private final JButton saveButton = new JButton(ButtonLabels.Save.getLabel());
    private final JButton cancelButton = new JButton(ButtonLabels.Cancel.getLabel());
    private final JButton closeButton = new JButton(ButtonLabels.Close.getLabel());
    private final JButton removeButton = new JButton(ButtonLabels.Remove.getLabel());
    private final JButton readButton = new JButton(ButtonLabels.Refresh.getLabel());

    public InstrumentPanel(final ServiceRegistery serviceRegistery)
    {
        super(InstrumentPanel.class);
        this.srvRgstry = serviceRegistery;
        this.insServ = (InstrumentService) this.srvRgstry.getService(Service.InstrumentService);
        tableModel = new InstrumentTableModel<>(new String[]{Labels.Ticker.getLabel(), Labels.Name.getLabel(),
                Labels.MaxSlippage.getLabel(), Labels.EtoroInstrumentId.getLabel(), Labels.Url.getLabel(),
                Labels.WithCandle.getLabel(),
                Labels.WithFeed.getLabel(),
                Labels.WithBands.getLabel(),
                Labels.Active.getLabel()}, //
                insServ.findAllOrderByName());
        setLayout(new GridBagLayout());
        setBorder(BorderFactory.createTitledBorder(Labels.InstrumentPanel.getLabel()));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);

        // Row 0: Instrument label + text field
        JPanel instrumentInputPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        instrumentInputPanel.add(tckrLbl);
        instrumentInputPanel.add(ticker);
        instrumentInputPanel.add(instNameLbl);
        instrumentInputPanel.add(instName);
        instrumentInputPanel.add(etoroIdLbl);
        instrumentInputPanel.add(etoroId);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.weightx = 1.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        add(instrumentInputPanel, gbc);

        // Row 1: Buttons
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

        // Row 2: Table inside scroll pane
        instTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(instTable);

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0; // allows table to expand vertically
        gbc.fill = GridBagConstraints.BOTH;
        add(scrollPane, gbc);
        saveButton.addActionListener(e -> save());
        removeButton.addActionListener(e -> remove());
        readButton.addActionListener(e -> insServ.findAll());
        cancelButton.addActionListener(e -> clear());
        closeButton.addActionListener(e ->
        {
            SwingUtilities.getWindowAncestor(this).dispose();
        });
        instTable.addMouseListener(new MouseClickHandler(this));
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
                Integer instrumentId = (Integer) instTable.getModel() //
                        .getValueAt(instTable.getSelectedRow(), 0);
                new InstrumentEditDialogue(frame, insServ, instrumentId);
            }
        }
    }


    public void save()
    {
        String name = instName.getText();

        String instTicker = ticker.getText();

        if (instTicker == null || instTicker.trim().isEmpty())
        {
            JOptionPane.showMessageDialog(this, "Instrument ticker should not be null!",
                    "Instrument Error Message", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Instrument inst = insServ.findByInstrumentTicker(ticker.getText());

        if (inst != null)
        {
            JOptionPane.showMessageDialog(this, "Instrument with ticker [" + ticker.getText() + "] already exist",
                    "Instrument Error Message", JOptionPane.ERROR_MESSAGE);
            return;
        }


        if (name != null && !name.trim().isEmpty())
        {
            String instName = name.trim();

            inst = insServ.findByName(instName);

            if (inst != null)
            {
                JOptionPane.showMessageDialog(this, "Instrument with name [" + instName + "] already exist",
                        "Instrument Error Message", JOptionPane.ERROR_MESSAGE);
                return;
            }

            Instrument instrument = new Instrument();
            instrument.setName(name.trim());
            instrument.setInstrumentTicker(ticker.getText());
            instrument.setEtoroInstrumentId(etoroId.getIntValue());
            if (instrument.getActive() == null) //
            {
                instrument.setActive(false);
            }
            insServ.addInstrument(instrument);
            tableModel.addRow(instrument);
            this.instName.setText(null);
            ticker.setText(null);
            etoroId.setText(null);
        } else
        {
            JOptionPane.showMessageDialog(this, "Please enter an instrument Ticker name.",
                    "Validation Error", JOptionPane.WARNING_MESSAGE);
        }
    }

    public void remove()
    {
        int selectedRow = instTable.getSelectedRow();
        if (selectedRow >= 0)
        {
            Instrument instrument = (Instrument) tableModel.removeRow(selectedRow);
            insServ.removeInstrument(instrument);
        } else
        {
            JOptionPane.showMessageDialog(this, "Please select an instrumentTicker to remove.",
                    "No Selection", JOptionPane.WARNING_MESSAGE);
        }
    }

    public void clear()
    {
        instName.setText(null);
    }


    @Override
    protected void paintComponent(Graphics g)
    {
        super.paintComponent(g); // always call super
    }
}
