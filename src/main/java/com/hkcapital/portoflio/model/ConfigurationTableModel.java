package com.hkcapital.portoflio.model;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.List;

public class ConfigurationTableModel extends AbstractTableModel
{
    private final String[] columns = {"index", "Instrument", "% Capital Deployed", "Capital",
            "%Pnl", "PnL", "Allowed Fire Power", "Remaining Fire Power", "Capital Remaining Firepower"
            , "leverage"};
    private final List<Position> data;

    public ConfigurationTableModel(List<Position> data)
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
        Position c = data.get(row);
        switch (col)
        {
            case 0:
                return c.getId();
            case 1:
                return c.getInstrument().getName();
            case 2:
                return c.getPercentCapitalDeployed();
            case 3:
                return c.getCurrentPositionEquity();
            case 4:
                return c.getPercentPnL();
            case 5:
                return c.getPnl();
            case 6:
                return c.getAllowedFirePower();
            case 7:
                return c.getRemainingFirepower();
            case 8:
                return c.getCapitalRemainingFirePower();
            case 9:
                return c.getConfigurtaion().getLev();
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
            fireTableCellUpdated(row, col);
        }
    }

    public void updateData(List<Position> newData)
    {
        List<Position> d = new ArrayList<>(newData);
        data.clear();
        data.addAll(d);
        fireTableDataChanged();
    }
}
