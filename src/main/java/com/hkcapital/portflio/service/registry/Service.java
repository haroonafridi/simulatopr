package com.hkcapital.portflio.service.registry;

public interface Service
{

    String ConfigurationService = "ConfigurationService";
    String InstrumentService = "InstrumentService";
    String MarketConditionsService = "MarketConditionsService";
    String PositionService = "PositionService";
    String StrategyService = "StrategyService";
    String TradingSessionsService = "TradingSessionsService";
    String TradingTimeFramesService = "TradingTimeFramesService";
    String EtoroCandleService = "EtoroCandleService";
    String EtoroAPIConfiguration = "EtoroAPIInformationService";
    String OrderManagerService = "OrderManagerService";
    String EtoroWebSocketManagerService = "EtoroWebSocketManagerService";
    String MarketStructureManagerCache = "MarketStructureManagerCache";

    String SRMatrixService = "SRMatrixService";
    String SRMatrixToleranceService = "SRMatrixToleranceService";
    String ProfileService = "ProfileService";
    String EtoroApiService = "EtoroApiService";

    String EnvService = "EnvService";
    String LiveInstrumentFeedService = "LiveInstrumentFeedService";
    String InstrumentMarketStructureService = "InstrumentMarketStructureService";

    String InstrumentMarketStructureConfService = "InstrumentMarketStructureConfService";
    String EtoroInstrumentService = "EtoroInstrumentService";
    String DataPathConfig = "DataPathConfig";
    String CandleCSVGenerator = "CandleCSVGenerator";
    String CSVCandleFileGenerator = "CSVCandleFileGenerator";

    String StrategyImporter = "StrategyImporter";
    String StrategyExporter = "StrategyExporter";
    String MarketStructureConfigurationImporter = "MarketStructureConfigurationImporter";
    String MarketStructureConfigurationExporter = "MarketStructureConfigurationExporter";
    String SRMatrixToleranceImporter = "SRMatrixToleranceImporter";
    String SRMatrixToleranceExporter = "SRMatrixToleranceExporter";

    String SRMatrixImporter = "SRMatrixImporter";
    String SRMatrixExporter = "SRMatrixExporter";

}
