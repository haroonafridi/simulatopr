package com.hkcapital.portoflio.ui.panels.configuartion.tablemodels;

import com.hkcapital.portoflio.model.Configuration;
import com.hkcapital.portoflio.ui.TableModel;
import com.hkcapital.portoflio.ui.panels.configuartion.labels.Labels;

import java.util.List;

public class ConfiguarionTableModel<E> extends TableModel
{
    final static String[] columnsName = new String[]{
            Labels.Id.getLabel(), Labels.PercentAllowedAllocation.getLabel(),
            Labels.NoOfInstruments.getLabel(),
            Labels.NoofPositionsPerinstruments.getLabel(),
            Labels.MaxPercentAllowedPerPosition.getLabel(),
            Labels.Lev.getLabel()
    };

    public ConfiguarionTableModel(List<Configuration> elements)
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
        Configuration configuration = (Configuration) getElements().get(rowIndex);

        switch (columnIndex)
        {
            case 0:
            {
                return configuration.getId();
            }
            case 1:
            {
                return configuration.getPercentAllocationAllowed();
            }

            case 2:
            {
                return configuration.getNoOfInsutrments();
            }

            case 3:
            {
                return configuration.getNoOfPositionsPerInstruments();
            }

            case 4:
            {
                return configuration.getMaxPercentAllowedPerInstrument();
            }

            case 5:
            {
                return configuration.getLev();
            }


            default:
            {
                return null;
            }
        }
    }
}
