package com.hkcapital.portoflio.ui;

import javax.swing.table.AbstractTableModel;
import java.util.List;

public abstract class TableModel<E> extends AbstractTableModel
{
    private final String[] columnsName;
    private final List<E> elements;

    public TableModel(String[] columnsName, List<E> elements)
    {
        this.columnsName = columnsName;
        this.elements = elements;
    }

    /**
     * Returns the number of rows in the model. A
     * <code>JTable</code> uses this method to determine how many rows it
     * should display.  This method should be quick, as it
     * is called frequently during rendering.
     *
     * @return the number of rows in the model
     * @see #getColumnCount
     */
    @Override
    public int getRowCount()
    {
        return elements.size();
    }

    /**
     * Returns the number of columns in the model. A
     * <code>JTable</code> uses this method to determine how many columns it
     * should create and display by default.
     *
     * @return the number of columns in the model
     * @see #getRowCount
     */
    @Override
    public int getColumnCount()
    {
        return columnsName.length;
    }

    /**
     * Returns a default name for the column using spreadsheet conventions:
     * A, B, C, ... Z, AA, AB, etc.  If <code>column</code> cannot be found,
     * returns an empty string.
     *
     * @param column the column being queried
     * @return a string containing the default name of <code>column</code>
     */
    @Override
    public String getColumnName(int column)
    {
        return columnsName[column];
    }

    public String[] getColumnsName()
    {
        return columnsName;
    }

    public List<E> getElements()
    {
        return elements;
    }

    public void addRow(E element)
    {
        elements.add(element);
        fireTableDataChanged();
    }

    public E removeRow(int index)
    {
        E element = elements.remove(index);
        fireTableDataChanged();
        return element;
    }
}
