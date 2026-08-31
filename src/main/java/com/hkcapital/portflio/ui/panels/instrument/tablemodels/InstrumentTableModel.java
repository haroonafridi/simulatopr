package com.hkcapital.portflio.ui.panels.instrument.tablemodels;

import com.hkcapital.portflio.model.Instrument;
import com.hkcapital.portflio.ui.TableModel;

import java.util.List;

public class InstrumentTableModel<E> extends TableModel
{
    public InstrumentTableModel(String[] columnsName, List<Instrument> elements)
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
        Instrument inst = (Instrument) getElements().get(rowIndex);

        switch (columnIndex)
        {
            case 0:
            {
                return inst.getId();
            }
            case 1:
            {
                return inst.getInstrumentTicker();
            }

            case 2:
            {
                return inst.getName();
            }

            case 3:
            {
                return inst.getMaxSlippage();
            }
            case 4:
            {
                return inst.getEtoroInstrumentId();
            }
            case 5:
            {
                return inst.getUrl();
            }
            case 6:
            {

                return inst.getWithCandle();
            }
            case 7:
            {

                return inst.getWithFeed();
            }
            case 8:
            {

                return inst.getWithBand();
            }
            case 9:
            {

                return inst.getActive();
            }

            default:
            {
                return null;
            }
        }
    }
}
