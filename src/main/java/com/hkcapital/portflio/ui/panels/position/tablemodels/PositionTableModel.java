package com.hkcapital.portflio.ui.panels.position.tablemodels;

import com.hkcapital.portflio.model.Position;
import com.hkcapital.portflio.ui.TableModel;

import java.util.ArrayList;
import java.util.List;

public class PositionTableModel<E> extends TableModel
{
    private final String[] columns = {
            "Position Id",
            "Instrument Name",
            "Allowed Slippage",
            "Etoro Instrument Id",
            "% Capital Deploy:",
            "Position equity",
            "Total Exposure",
            "Allowed Fire Power",
            "Remaining Fire Power:",
            "Capital Remaining Firepower:",
            "day low",
            "day high",
            "% move",
            "Support",
            "Resistance",
            "Take Profit",
            "Stop Loss",
            "Short Position",
            "Long Position",
            "Short/Long Position",
            "Time Frame",
            "Time Frame Unite",
            "leverage",
            "active"
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
                return position.getInstrument().getName();
            case 2:
                return position.getInstrument().getMaxSlippage();
            case 3:
                return position.getInstrument().getEtoroInstrumentId();
            case 4:
                return position.getPercentCapitalDeployed();
            case 5:
                return position.getCurrentPositionEquity();
            case 6:
                return position.getCurrentPositionEquity() * position.getConfiguration().getLev();
            case 7:
                return position.getAllowedFirePower();
            case 8:
                return position.getRemainingFirepower();
            case 9:
                return position.getCapitalRemainingFirePower();
            case 10:
                return position.getMarketConditions().getDayLow();
            case 11:
                return position.getMarketConditions().getDayHigh();
            case 12:
                return position.getMarketConditions().getPercentMove();//
            case 13:
                return position.getSrMatrix().getSupport();//
            case 14:
                return position.getSrMatrix().getResistance();//
            case 15:
                return position.getSrMatrix().getTakeProfit();//
            case 16:
                return position.getSrMatrix().getStopLoss();//
            case 17:
                return position.getIsShort();
            case 18:
                return position.getIsLong();
            case 19:
                return position.getIsShortLong();
            case 20:
                return position.getSrMatrix().getTimeFrame();//
            case 21:
                return position.getSrMatrix().getTimeFrameUnit();//
            case 22:
                return position.getConfiguration().getLev();
            case 23:
                return position.getActive();
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
        if (newData != null && newData.size() > 0) {
            List<Position> d = new ArrayList<>(newData);
            data.clear();
            data.addAll(d);
        }
        else
        {
            data.clear();
        }
        fireTableDataChanged();
    }

}
