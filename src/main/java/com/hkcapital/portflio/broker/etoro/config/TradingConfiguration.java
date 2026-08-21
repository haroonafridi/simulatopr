package com.hkcapital.portflio.broker.etoro.config;

public class TradingConfiguration
{
    public static Boolean ACTIVATE_AUTOMATIC_TRADING = Boolean.FALSE;
    public static Boolean SHOW_TRADING = Boolean.FALSE;


    public static void showHide()
    {
        if (SHOW_TRADING)
        {
            SHOW_TRADING =
                    Boolean.FALSE;
        } else
        {
            SHOW_TRADING =
                    Boolean.TRUE;
        }
    }
}
