package com.hkcapital.portoflio.ui.panels.srmatrix.tablemodels;

import com.hkcapital.portoflio.model.SRMatrix;
import com.hkcapital.portoflio.ui.TableModel;

import javax.swing.*;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class SRMatrixTableModel<E> extends TableModel
{
    public SRMatrixTableModel(String[] columnsName, List<SRMatrix> elements)
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
        SRMatrix srMatrix = (SRMatrix) getElements().get(rowIndex);

        switch (columnIndex)
        {
            case 0:
            {
                return srMatrix.getId();
            }

            case 1:
            {
                return srMatrix.getInstrument().getName();
            }
            case 2:
            {
                return srMatrix.getCreationDate().format(DateTimeFormatter.ofPattern("d MMM uuuu"));
            }
            case 3:
            {
                return srMatrix.getSupport();
            }
            case 4:
            {
                return srMatrix.getResistance();
            }

            case 5:
            {
                return srMatrix.getTimeFrame();
            }

            case 6:
            {
                return srMatrix.getTimeFrameUnit();
            }

            case 7:
            {
                return  srMatrix.getActive();
            }
            default:
            {
                return null;
            }
        }
    }
}
