package com.hkcapital.portflio.ui.panels.srmatrix.tablemodels;

import com.hkcapital.portflio.model.SRMatrixTolerance;
import com.hkcapital.portflio.ui.TableModel;

import java.time.format.DateTimeFormatter;
import java.util.List;

public class SRMatrixToleranceTableModel<E> extends TableModel
{
    public SRMatrixToleranceTableModel(String[] columnsName, List<SRMatrixTolerance> elements)
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
        SRMatrixTolerance srMatrix = (SRMatrixTolerance) getElements().get(rowIndex);

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
                return srMatrix.getL_s_tolerance_percent();
            }
            case 4:
            {
                return srMatrix.getR_s_tolerance_percent();
            }
            case 5:
            {
                return srMatrix.getL_r_tolerance_percent();
            }
            case 6:
            {
                return srMatrix.getR_r_tolerance_percent();
            }

            case 7:
            {
                return srMatrix.getTakeProfitPercent();
            }

            case 8:
            {
                return srMatrix.getStopLossPercent();
            }

            case 9:
            {
                return srMatrix.getTimeFrame();
            }
            case 10:
            {
                return srMatrix.getTimeFrameUnit();
            }
            case 11:
            {
                return srMatrix.getActive();
            }
            default:
            {
                return null;
            }
        }
    }
}
