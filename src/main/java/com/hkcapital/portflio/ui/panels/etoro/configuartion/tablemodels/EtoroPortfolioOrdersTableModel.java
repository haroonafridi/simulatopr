package com.hkcapital.portflio.ui.panels.etoro.configuartion.tablemodels;

import com.hkcapital.portflio.ui.TableModel;
import com.hkcapital.portflio.ui.panels.etoro.configuartion.labels.Labels;

import java.util.List;

public class EtoroPortfolioOrdersTableModel<E> extends TableModel
{
    final static String[] columnsName = new String[]
            {
                    Labels.Id.getLabel(),
                    Labels.Instrument.getLabel(),
                    Labels.Amount.getLabel(),
                    Labels.Info.getLabel()
            };

    public EtoroPortfolioOrdersTableModel(List<EtoroPortfolioOrderDto> elements)
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
        EtoroPortfolioOrderDto etoroPortfolioOrderDto = (EtoroPortfolioOrderDto) getElements().get(rowIndex);

        switch (columnIndex)
        {
            case -1:
            {
                return etoroPortfolioOrderDto;
            }
            case 0:
            {
                return etoroPortfolioOrderDto.getOrderId();
            }
            case 1:
            {
                return etoroPortfolioOrderDto.getInstrumentId();
            }

            case 2:
            {
                return etoroPortfolioOrderDto.getAmount();
            }

            case 3:
            {
                return etoroPortfolioOrderDto.getInfo();
            }
            default:
            {
                return null;
            }
        }
    }
}
