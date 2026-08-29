package com.hkcapital.portflio.ui.panels.instrument.tablemodels;

import com.hkcapital.portflio.model.Instrument;
import com.hkcapital.portflio.model.InstrumentMarketStructureConf;
import com.hkcapital.portflio.ui.TableModel;

import java.util.List;

public class InstrumentConfTableModel<E> extends TableModel
{
    public InstrumentConfTableModel(String[] columnsName, List<InstrumentMarketStructureConf> elements)
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
        InstrumentMarketStructureConf instConf = (InstrumentMarketStructureConf) getElements().get(rowIndex);

        switch (columnIndex)
        {
            case 0:
            {
                return instConf.getId();
            }
            case 1:
            {
                return instConf.getInstrument().getInstrumentTicker();
            }

            case 2:
            {
                return instConf.getTimeFrameUnit();
            }

            case 3:
            {
                return instConf.getTimeFrame();
            }
            case 4:
            {
                return instConf.getIntrvl();
            }
            case 5:
            {
                return instConf.getModule();
            }
            case 6:
            {

                return instConf.getSub();
            }

            case 7:
            {

                return instConf.getStructureName();
            }

            case 8:
            {

                return instConf.isActive();
            }

            default:
            {
                return null;
            }
        }
    }
}
