package com.hkcapital.portoflio;

public record Position(int index,
                       Instrument instrument,
                       double percentCapitalDeployed
                     )
{

}
