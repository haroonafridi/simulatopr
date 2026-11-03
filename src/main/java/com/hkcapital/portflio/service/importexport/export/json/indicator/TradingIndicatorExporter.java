package com.hkcapital.portflio.service.importexport.export.json.indicator;

import com.hkcapital.portflio.model.TradingIndicator;
import com.hkcapital.portflio.service.importexport.*;
import com.hkcapital.portflio.service.srmatrix.dto.SRMatrixDTO;
import com.hkcapital.portflio.service.tradingindicators.TradingIndicatorsService;
import com.hkcapital.portflio.service.tradingindicators.dto.TradingIndicatorDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


@Slf4j
@Service("indicatorConfExporter")
public class TradingIndicatorExporter extends ImporterExporterAbstract implements Exporter
{
    private final FileGenerator fileGenerator;
    private final TradingIndicatorsService indicatorsService;

    public TradingIndicatorExporter(final ImporterExporterDependencies dependencies,
                                    final @Qualifier("jsonFileGenerator") FileGenerator fileGenerator,
                                    final TradingIndicatorsService indicatorsService
    )
    {
        super(dependencies);
        this.fileGenerator = fileGenerator;
        this.indicatorsService = indicatorsService;
    }

    @Override
    public void export()
    {
        final List<TradingIndicator> indicators = indicatorsService.findAll();

        final List<TradingIndicatorDTO> tradingIndicatorDTOs = new ArrayList<>();

        indicators.forEach(indicator -> {
            tradingIndicatorDTOs.add(indicator.buildTradingIndicatorDTO());
        });

        try
        {
            fileGenerator.fileOf(objectWriter.writeValueAsString(tradingIndicatorDTOs),
                    "indicator.json",null, FileTypes.INDICATOR);
        } catch (IOException e)
        {
            log.error("Error in generating file indicator.json");
            throw new RuntimeException(e.getMessage());
        }
    }

}
