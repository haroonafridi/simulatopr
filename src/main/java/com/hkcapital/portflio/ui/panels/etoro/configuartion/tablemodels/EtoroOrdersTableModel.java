package com.hkcapital.portflio.ui.panels.etoro.configuartion.tablemodels;

import com.hkcapital.portflio.model.etoro.EtoroOrder;
import com.hkcapital.portflio.ui.TableModel;
import com.hkcapital.portflio.ui.panels.etoro.configuartion.labels.Labels;

import java.util.List;

public class EtoroOrdersTableModel<E> extends TableModel
{
    final static String[] columnsName = new String[]
            {
                    Labels.Id.getLabel(),
                    Labels.Instrument.getLabel(),
                    Labels.Type.getLabel(),
                    Labels.TimeFrame.getLabel(),
                    Labels.Status.getLabel(),
                    Labels.Amount.getLabel()
            };

    public EtoroOrdersTableModel(List<EtoroOrder> elements)
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
        EtoroOrder etoroOrder = (EtoroOrder) getElements().get(rowIndex);

        switch (columnIndex)
        {
            case 0:
            {
                return etoroOrder.getOrderID();
            }
            case 1:
            {
                return "GOLD";
            }

            case 2:
            {
                return etoroOrder.getOderType();
            }

            case 3:
            {
                return etoroOrder.getTimeFrame()+"-"+etoroOrder.getTimeFrameUnit();
            }

            case 4:
            {
                return etoroOrder.getStatus();
            }

            case 5:
            {
                return etoroOrder.getAmount();
            }

            default:
            {
                return null;
            }
        }
    }
}
