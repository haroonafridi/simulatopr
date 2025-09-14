package com.hkcapital.portoflio.ui.panels.tradingsessions;

import com.hkcapital.portoflio.model.TradingSessions;
import com.hkcapital.portoflio.ui.TableModel;

import java.util.List;

public class TradingSessionTableModel<E> extends TableModel
{
    final static String[] columnsName = TradingSessionTableHeader.labels();

    public TradingSessionTableModel(List<TradingSessions> elements)
    {
        super(columnsName, elements);
    }


    /**
     * Returns the value for the cell at <code>columnIndex</code> and
     * <code>rowIndex</code>.
     *
     * @param rowIndex    the row whose value is to be queried
     * @param columnIndex the column whose value is to be queried
     * @return the value Object at the specified cell
     */
    @Override
    public Object getValueAt(int rowIndex, int columnIndex)
    {
        TradingSessions tradingSessions = (TradingSessions) getElements().get(rowIndex);

        switch (columnIndex)
        {
            case 0:
            {
                return tradingSessions.getId();
            }
            case 1:
            {
                return tradingSessions.getName();
            }

            case 2:
            {
                return tradingSessions.getDescription();
            }

            case 3:
            {
                return tradingSessions.getStartTime();
            }

            case 4:
            {
                return tradingSessions.getEndTime();
            }
            default:
            {
                return null;
            }
        }
    }
}
