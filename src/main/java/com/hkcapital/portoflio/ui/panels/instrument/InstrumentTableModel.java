package com.hkcapital.portoflio.ui.panels.instrument;

import com.hkcapital.portoflio.model.Instrument;
import com.hkcapital.portoflio.service.InstrumentService;
import com.hkcapital.portoflio.ui.TableModel;

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
                return inst.getName();
            }
            default:
            {
                return null;
            }
        }
    }
}
