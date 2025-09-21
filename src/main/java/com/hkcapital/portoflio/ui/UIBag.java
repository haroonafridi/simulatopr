package com.hkcapital.portoflio.ui;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public abstract class UIBag extends JPanel
{
    private static final Map<String, Component> components = new HashMap<>();

    public UIBag(Class<?> id)
    {
        if (!components.containsKey(id.getName()))
        {
            components.put(id.getName(), this);
        } else
        {
            System.err.println("Warning: Component with id '" + id + "' already exists!");
        }
    }

    @SuppressWarnings("unchecked")
    public <T extends Component> T getComponent(Class<T> type)
    {
        Component c = components.get(type.getName());
        if (c == null)
        {
            System.err.println("Component with id '" + type.getName() + "' not found!");
            return null;
        }
        return type.cast(c);
    }
}
