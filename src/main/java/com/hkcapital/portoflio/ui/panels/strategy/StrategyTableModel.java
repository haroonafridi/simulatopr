package com.hkcapital.portoflio.ui.panels.strategy;

import com.hkcapital.portoflio.model.Strategy;
import com.hkcapital.portoflio.ui.TableModel;

import java.util.List;

public class StrategyTableModel<E> extends TableModel
{
    public StrategyTableModel(String[] columnsName, List<Strategy> elements)
    {
        super(columnsName, elements);
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex)
    {
        Strategy strategy = (Strategy) getElements().get(rowIndex);

        switch (columnIndex)
        {
            case 0:
            {
                return strategy.getId();
            }
            case 1:
            {
                return strategy.getName();
            }
            case 2:
            {
                return strategy.getName();
            }

            default:
            {
                return null;
            }
        }
    }
}
