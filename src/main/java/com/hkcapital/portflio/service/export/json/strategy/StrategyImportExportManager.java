package com.hkcapital.portflio.service.export.json.strategy;

public interface StrategyImportExportManager
{
    void exportStrategy(int strategyId);
    void importStrategy();

    void importSRMatrix();
    void importSRMatrixTolerance();

    void exportSRMatrix();
    void exportSRMatrixTolerance();

    void exportMarketStructureConf();

    void importMarketStructureConf();
}
