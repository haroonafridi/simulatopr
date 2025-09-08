package com.hkcapital.portoflio.ui.panels.position;

import com.hkcapital.portoflio.model.Position;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.List;

public class PositionTableModel extends AbstractTableModel
{
    private final String[] columns = {"index", "Position Index", "Instrument", "% Capital Deployed", "Capital",
            "%Pnl", "PnL", "Allowed Fire Power", "Remaining Fire Power", "Capital Remaining Firepower",
            "day low", "day high", "% move",
             "leverage"};
    private List<Position> data;

    public PositionTableModel(List<Position> data)
    {
        this.data = data;
    }

    public List<Position> getData()
    {
        return data;
    }

    public void setData(List<Position> data)
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
        Position position = data.get(row);
        switch (col)
        {
            case 0:
                return position.getId();
            case 1:
                position.getRecordIndex();
            case 2:
                return position.getInstrument().getName();
            case 3:
                return position.getPercentCapitalDeployed();
            case 4:
                //capital
                // position.getCapitalRemainingFirePower();
            case 5:
                return position.getPercentPnL();
            case 6:
                return position.getPnl();
            case 7:
                return position.getAllowedFirePower();
            case 8:
                return position.getRemainingFirepower();
            case 9:
                return position.getCapitalRemainingFirePower();
            case 10:
                return position.getConfigurtaion().getLev();
            case 11:
                return position.getPortfolioValue();
            case 12:
                return position.getMarketConditions().getDayLow();
            case 13:
                return position.getMarketConditions().getDayHigh();
            case 14:
                return position.getMarketConditions().getPercentMove();
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
