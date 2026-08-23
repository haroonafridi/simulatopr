package com.hkcapital.portflio.service.strategy;

public interface StrategyImportExportManager
{
    void exportStrategy(int strategyId);
    void importStrategy();

    void importSRMatrix();
    void importSRMatrixTolerance();

    void exportSRMatrix();
    void exportSRMatrixTolerance();
}
