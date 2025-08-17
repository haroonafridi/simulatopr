package com.hkcapital.portoflio.model;

public record Configuraion(int id, double percentAllocationAllowed, int noOfInsutrments,
                           int noOfPositionsPerInstruments,
                           double maxPercentAllowedPerInstrument,
                           double lev)
{
}
