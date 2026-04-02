package com.hkcapital.portoflio.etoro;

public enum ETORO_INSTUMENTS
{
    GOLD(18),
    NASDAQ(28),
    ;

    private Integer instumentId;

    ETORO_INSTUMENTS(Integer instumentId)
    {
        this.instumentId = instumentId;
    }


    public Integer getInstumentId()
    {
        return instumentId;
    }
}
