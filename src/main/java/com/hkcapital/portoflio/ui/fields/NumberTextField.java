package com.hkcapital.portoflio.ui.fields;

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
        setText(value.toString());
    }

    public NumberTextField(int width, Integer value)
    {
        setPreferredSize(new Dimension(width, 20));
        setText(value.toString());
    }



    public NumberTextField(int width)
    {
        setPreferredSize(new Dimension(width, 20));
    }

    public double getDoubleValue()
    {
        String value = this.getText();
        if(value == null)
        {
            throw new NumberFormatException("Value cannot be null");
        }
        return Double.valueOf(value);
    }


    public Integer getIntValue()
    {
        String value = this.getText();
        if(value == null)
        {
            throw new NumberFormatException("Value cannot be null");
        }
        return Integer.valueOf(value);
    }
}
