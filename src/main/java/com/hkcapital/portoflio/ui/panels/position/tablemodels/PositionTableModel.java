package com.hkcapital.portoflio.ui.panels.position.tablemodels;

import com.hkcapital.portoflio.model.Position;
import com.hkcapital.portoflio.ui.TableModel;

import java.util.ArrayList;
import java.util.List;

public class PositionTableModel extends TableModel
{
    private final String[] columns = {"index",
            "Position Index",
            "Instrument Name",
            "Allowed Slippage",
            "Etoro Instrument Id",
            "% Capital Deploye:",
            "Capital",
            "%Pnl",
            "PnL",
            "Allowed Fire Power",
            "Remaining Fire Power:",
            "Capital Remaining Firepower:",
            "day low",
            "day high",
            "% move",
            "Support",
            "Resistance",
            "Time Frame",
            "Time Frame Unite",
            "leverage"
    };
    private List<Position> data;


    public PositionTableModel(List<Position> data)
    {
        super(null, data);
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
                return position.getRecordIndex();
            case 2:
                return position.getInstrument().getName();
            case 3:
                return position.getInstrument().getMaxSlippage();
            case 4:
                return position.getInstrument().getEtoroInstrumentId();
            case 5:
                return position.getPercentCapitalDeployed();
            case 6:
                return 0;
            case 7:
                return position.getPercentPnL();
            case 8:
                return position.getPnl();
            case 9:
                return position.getAllowedFirePower();
            case 10:
                return position.getRemainingFirepower();
            case 11:
                return position.getCapitalRemainingFirePower();
            case 12:
                return position.getMarketConditions().getDayLow();
            case 13:
                return position.getMarketConditions().getDayHigh();
            case 14:
                return position.getMarketConditions().getPercentMove();//
            case 15:
                return position.getSrMatrix().getSupport();//
            case 16:
                return position.getSrMatrix().getResistance();//
            case 17:
                return position.getSrMatrix().getTimeFrame();//
            case 18:
                return position.getSrMatrix().getTimeFrameUnit();//
            case 19:
                return position.getConfigurtaion().getLev();
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
