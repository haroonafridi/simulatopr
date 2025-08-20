package com.hkcapital.portoflio.model;

public record Position(Integer index,
                       Instrument instrument,
                       double percentCapitalDeployed
                     )
{

}
