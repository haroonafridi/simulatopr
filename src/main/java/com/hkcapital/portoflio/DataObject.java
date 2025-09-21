package com.hkcapital.portoflio;

import java.util.HashMap;

public class DataObject<String, V> extends HashMap
{

    /**
     * Constructs an empty {@code HashMap} with the default initial capacity
     * (16) and the default load factor (0.75).
     */
    public DataObject(String key, V value)
    {
        put(key, value);
    }

    /**
     * Constructs an empty {@code HashMap} with the default initial capacity
     * (16) and the default load factor (0.75).
     */
    public DataObject()
    {
    }
}
