package com.hkcapital.portoflio.ui.panels.marketconditions;

import com.hkcapital.portoflio.model.Instrument;
import com.hkcapital.portoflio.model.MarketConditions;
import com.hkcapital.portoflio.ui.TableModel;

import java.util.List;

public class MarketConditionsTableModel<E> extends TableModel
{
    public MarketConditionsTableModel(String[] columnsName, List<MarketConditions> elements)
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
        MarketConditions marketConditions = (MarketConditions) getElements().get(rowIndex);

        switch (columnIndex)
        {
            case 0:
            {
                return marketConditions.getId();
            }
            case 1:
            {
                return marketConditions.getInstrument().getName();
            }

            case 2:
            {
                return marketConditions.getDayHigh();
            }


            case 3:
            {
                return marketConditions.getDayLow();
            }

            case 4:
            {
                return marketConditions.getPercentMove();
            }

            default:
            {
                return null;
            }
        }
    }
}
