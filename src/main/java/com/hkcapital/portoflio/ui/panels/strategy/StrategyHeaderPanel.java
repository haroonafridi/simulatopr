package com.hkcapital.portoflio.ui.panels.strategy;

import com.hkcapital.portoflio.model.Position;
import com.hkcapital.portoflio.model.Strategy;
import com.hkcapital.portoflio.repository.ServiceRegistery;
import com.hkcapital.portoflio.service.PositionService;
import com.hkcapital.portoflio.service.Service;
import com.hkcapital.portoflio.service.StrategyService;
import com.hkcapital.portoflio.ui.panels.position.PositionActionsPanel;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDateTime;
import java.util.List;
public class StrategyHeaderPanel extends JPanel
{
    private final StrategyService strategyService;
    private final JLabel strategyNameLabel = new JLabel("Strategy Name:");
    private  final JTextField strategyName = new JTextField(20);
    private final JLabel strategyDescriptionLabel = new JLabel("Strategy Description:");

    private  final JTextField strategyDescription = new JTextField(40);

    private final JButton saveStrategy = new JButton("Save Strategy");

    private final JButton cancelButton = new JButton("Cancel");
    private final JButton closeButton = new JButton("Close");
    private final JButton removeButton = new JButton("Remove");

    private final JTable strategyTable;
    private final StrategyTableModel<Strategy>  tableModel;

    private PositionActionsPanel positionActionsPanel;

    private final PositionService positionService;

    private final ServiceRegistery<Service> serviceRegistery;


    public StrategyHeaderPanel(final ServiceRegistery<Service> serviceRegistery)
    {
        this.serviceRegistery = serviceRegistery;
        this.strategyService = (StrategyService)serviceRegistery.getService(Service.StrategyService);
        this.positionService = (PositionService)serviceRegistery.getService(Service.PositionService);
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createTitledBorder("⚙ Strategy Details"));

        tableModel = new StrategyTableModel<>(new String[]{"Id", "Name" ,
                "Description:"}, strategyService.findAll());

        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));

        topPanel.add(strategyNameLabel);
        topPanel.add(strategyName);
        topPanel.add(strategyDescriptionLabel);
        topPanel.add(strategyDescription);
        topPanel.add(saveStrategy);
        topPanel.add(removeButton);
        add(topPanel, BorderLayout.NORTH);
        strategyTable = new JTable(tableModel);
        int rowCountToShow = 50;
        int rowHeight = strategyTable.getRowHeight();          // default row height
        int tableHeaderHeight = strategyTable.getTableHeader().getPreferredSize().height;
        int preferredHeight = rowHeight * rowCountToShow + tableHeaderHeight;

        strategyTable.setFillsViewportHeight(true);
        strategyTable.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        strategyTable.setPreferredScrollableViewportSize(new Dimension(500, preferredHeight));
        JScrollPane scrollPane = new JScrollPane(strategyTable);

        add(scrollPane, BorderLayout.CENTER);

        saveStrategy.addActionListener(s ->  {
            Strategy strategy = new Strategy(strategyName.getText(), strategyDescription.getText(), LocalDateTime.now());
            strategyService.addStrategy(strategy);
            strategyName.setText(null);
            strategyDescription.setText(null);
            tableModel.addRow(strategy);
            tableModel.fireTableDataChanged();
        });

        strategyTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        strategyTable.getSelectionModel().addListSelectionListener(e-> {
            if (!e.getValueIsAdjusting()) {
                int selectedRow = strategyTable.getSelectedRow();
                if (selectedRow >= 0)
                {
                    // Convert view row index to model index (important if table is sorted)
                    int modelRow = strategyTable.convertRowIndexToModel(selectedRow);
                    Strategy strategy =   (Strategy)tableModel.getElements().get(selectedRow);
                    setHeaderFieldsFromRow(modelRow);
                    List<Position> positionList =  positionService.findByStrategyId(strategy.getId());
                    positionActionsPanel.getModel().updateData(positionList);
                }
            }
        });

        removeButton.addActionListener(e -> remove());
        cancelButton.addActionListener(e -> clear());

    }

    private void setHeaderFieldsFromRow(int rowIndex) {

        Object id = tableModel.getValueAt(rowIndex, 0);       // Column 0

        Object name = tableModel.getValueAt(rowIndex, 1);     // Column 1

        Object description = tableModel.getValueAt(rowIndex, 2); // Column 2

        strategyName.setText(name != null ? name.toString() : "");

        strategyDescription.setText(description != null ? description.toString() : "");
    }

    public Strategy getStrategy()
    {
        return (Strategy) tableModel.getElements().get(strategyTable.getSelectedRow());
    }


    public void remove() {
        int selectedRow = strategyTable.getSelectedRow();
        if (selectedRow >= 0) {
            Strategy strategy = (Strategy) tableModel.removeRow(selectedRow);
            strategyService.removeStrategy(strategy);
            clear();
        } else {
            JOptionPane.showMessageDialog(this, "Please select an instrument to remove.",
                    "No Selection", JOptionPane.WARNING_MESSAGE);
        }
    }

    public void clear() {
       strategyName.setText(null);
       strategyDescription.setText(null);
    }


    public void setPositionActionsPanel(PositionActionsPanel positionActionsPanel)
    {
        this.positionActionsPanel = positionActionsPanel;
    }
}
