package com.hkcapital.portflio.ui.fields;

import javax.swing.*;
import java.awt.*;

public class NumberTextField extends JTextField
{

    public NumberTextField()
    {
        setPreferredSize(new Dimension(200, 20));
    }

    public NumberTextField(int width, Double value)
    {
        setPreferredSize(new Dimension(width, 20));
        setText(value != null ? value.toString() : "");
    }

    public NumberTextField(int width, Integer value)
    {
        setPreferredSize(new Dimension(width, 20));
        setText(value != null ? value.toString() : "");
    }

    public NumberTextField(int width)
    {
        setPreferredSize(new Dimension(width, 20));
    }

    public double getDoubleValue()
    {
        String value = getText();

        if (value == null || value.trim().isEmpty())
        {
            throw new NumberFormatException("Value cannot be null or empty");
        }

        return Double.valueOf(value.trim());
    }

    public Integer getIntValue()
    {
        String value = getText();

        if (value == null || value.trim().isEmpty())
        {
            return null;
        }

        return Integer.valueOf(value.trim());
    }

    public Integer intValue()
    {
        return getIntValue();
    }

    public boolean isNull()
    {
        String value = getText();
        return value == null || value.trim().isEmpty();
    }

    public boolean isNotNull()
    {
        return !isNull();
    }
}
