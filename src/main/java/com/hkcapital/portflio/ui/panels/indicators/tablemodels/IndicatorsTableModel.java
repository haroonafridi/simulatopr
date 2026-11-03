package com.hkcapital.portflio.ui.panels.indicators.tablemodels;

import com.hkcapital.portflio.model.TradingIndicator;
import com.hkcapital.portflio.ui.TableModel;
import com.hkcapital.portflio.ui.panels.indicators.labels.Labels;

import java.util.List;

public class IndicatorsTableModel<E> extends TableModel
{
    final static String[] columnsName = new String[]{
            Labels.Id.getLabel(), Labels.Instrument.getLabel(), Labels.Name.getLabel(),
            Labels.Period.getLabel(),
            Labels.TimeFrame.getLabel(),
            Labels.TimeFrameUnit.getLabel(),
    };

    public IndicatorsTableModel(List<TradingIndicator> elements)
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
        TradingIndicator tradingIndicators = (TradingIndicator) getElements().get(rowIndex);

        switch (columnIndex)
        {
            case 0:
            {
                return tradingIndicators.getId();
            }
            case 1: {
                return tradingIndicators.getInstrument() == null ? //
                        null : tradingIndicators.getInstrument() .getInstrumentTicker();
            }
            case 2:
            {
                return tradingIndicators.getName();
            }

            case 3:
            {
                return tradingIndicators.getPeriod();
            }

            case 4:
            {
                return tradingIndicators.getTimeFrame();
            }

            case 5:
            {
                return tradingIndicators.getTimeFrameUnit();
            }

            default:
            {
                return null;
            }
        }
    }
}
