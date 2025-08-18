package com.hkcapital.portoflio.model;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ConfigurationTableModel extends AbstractTableModel
{
    private final String[] columns = {"index", "Instrument", "% Capital Deployed", "Capital",
            "%Pnl", "PnL", "Allowed Fire Power", "Remaining Fire Power", "Capital Remaining Firepower"
            , "leverage"};
    private final List<PositionPnL> data;

    public ConfigurationTableModel(List<PositionPnL> data)
    {
        this.data = data;
    }

    @Override
    public int getRowCount()
    {
        return data.size();
    }

    @Override
    public int getColumnCount()
    {
        return columns.length;
    }

    @Override
    public String getColumnName(int column)
    {
        return columns[column];
    }

    @Override
    public Object getValueAt(int row, int col)
    {
        PositionPnL c = data.get(row);
        switch (col)
        {
            case 0:
                return c.index();
            case 1:
                return c.position().instrument().name();
            case 2:
                return  c.position().percentCapitalDeployed();
            case 3:
                return c.currentPositionEquity();
            case 4:
                return c.percentPnL();
            case 5:
                return c.pnl();
            case 6:
                return c.configuraion().percentAllocationAllowed();
            case 7:
                return c.configuraion().percentAllocationAllowed();
            case 8:
                return c.configuraion().percentAllocationAllowed();
            case 9:
                return c.configuraion().lev();
            default:
                return null;
        }
    }

    @Override
    public boolean isCellEditable(int row, int col)
    {
        return col == 1; // allow editing only in "Value"
    }

    @Override
    public void setValueAt(Object value, int row, int col)
    {
        if (col == 1)
        {
            // data.get(row).currentPositionEquity(Integer.parseInt(value.toString()));
            fireTableCellUpdated(row, col);
        }
    }

    public void updateData(List<PositionPnL> newData) {
        List<PositionPnL> d = new ArrayList<>(newData);
        data.clear();
        data.addAll(d);
        fireTableDataChanged();
    }
}
