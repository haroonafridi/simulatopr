package com.hkcapital.portoflio.model;

public record Position(int index,
                       Instrument instrument,
                       double percentCapitalDeployed
                     )
{

}
