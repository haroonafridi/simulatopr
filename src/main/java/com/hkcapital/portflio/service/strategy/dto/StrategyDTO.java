package com.hkcapital.portflio.service.strategy.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.hkcapital.portflio.model.*;
import com.hkcapital.portflio.service.configuration.dto.ConfigurationDTO;
import com.hkcapital.portflio.service.instrument.dto.InstrumentDTO;
import com.hkcapital.portflio.service.marketconditions.dto.MarketConditionsDTO;
import com.hkcapital.portflio.service.positions.dto.PositionDTO;
import com.hkcapital.portflio.service.srmatrix.dto.SRMatrixDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class StrategyDTO
{
    private String name;
    private String description;
    private Double capitalAllocated;
    private LocalDateTime creationDate;
    private Boolean active;
    private List<PositionDTO> positionPnLList;


    @JsonIgnore
    public Strategy buildStrategy()
    {
        Strategy strategy = Strategy.builder()
                .name(name)
                .description(description)
                .capitalAllocated(capitalAllocated)
                .creationDate(creationDate)
                .active(active)
                .build();

        List<Position> positionList = new ArrayList<>();


        for (PositionDTO posDTO : positionPnLList)
        {

            ConfigurationDTO configDTO = posDTO.getConfiguration();

            Configuration config = Configuration.builder()
                    .lev(configDTO.getLev())
                    .maxPercentAllowedPerInstrument(configDTO.getMaxPercentAllowedPerInstrument())
                    .noOfInsutrments(configDTO.getNoOfInsutrments())
                    .noOfPositionsPerInstruments(configDTO.getNoOfPositionsPerInstruments())
                    .build();

            MarketConditionsDTO marketCondDTO = posDTO.getMarketConditions();

            InstrumentDTO instDTO = marketCondDTO.getInstrumentDTO();

            Instrument instrument = Instrument.builder()
                    .instrumentTicker(instDTO.getInstrumentTicker())
                    .maxSlippage(instDTO.getMaxSlippage())
                    .etoroInstrumentId(instDTO.getEtoroInstrumentId())
                    .name(instDTO.getName())
                    .url(instDTO.getUrl())
                    .instrumentDesc(instDTO.getInstrumentDesc())
                    .active(instDTO.getActive())
                    .build();

            MarketConditions marketCond = MarketConditions.builder()
                    .dayLow(marketCondDTO.getDayLow())
                    .dayHigh(marketCondDTO.getDayHigh())
                    .percentMove(marketCondDTO.getPercentMove())
                    .instrument(instrument)
                    .build();

            SRMatrixDTO srMatrixDTO = posDTO.getSrMatrix();

            SRMatrix srMatrix = SRMatrix.builder()
                    .l_r_tolerance(srMatrixDTO.getL_r_tolerance())
                    .r_r_tolerance(srMatrixDTO.getR_r_tolerance())
                    .resistance(srMatrixDTO.getResistance())
                    .support(srMatrixDTO.getSupport())
                    .l_s_tolerance(srMatrixDTO.getL_s_tolerance())
                    .l_r_tolerance(srMatrixDTO.getL_r_tolerance())
                    .stopLoss(srMatrixDTO.getStopLoss())
                    .takeProfit(srMatrixDTO.getTakeProfit())
                    .instrument(instrument)
                    .active(srMatrixDTO.getActive())
                    .timeFrameUnit(srMatrixDTO.getTimeFrameUnit())
                    .timeFrame(srMatrixDTO.getTimeFrame())
                    .creationDate(srMatrixDTO.getCreationDate())
                    .build();
            Position position = Position.builder().configuration(config)
                    .marketConditions(marketCond)
                    .instrument(instrument)
                    .srMatrix(srMatrix)
                    .percentCapitalDeployed(posDTO.getPercentCapitalDeployed())
                    .strategy(strategy)
                    .currentPositionEquity(posDTO.getCurrentPositionEquity())
                    .allowedFirePower(posDTO.getAllowedFirePower())
                    .remainingFirepower(posDTO.getRemainingFirepower())
                    .portfolioValue(posDTO.getPortfolioValue())
                    .leverage(posDTO.getLeverage())
                    .stopLoss(posDTO.getStopLoss())
                    .takeProfit(posDTO.getTakeProfit())
                    .active(posDTO.getActive())
                    .positionType(posDTO.getPositionType())
                    .executionCount(posDTO.getExecutionCount())
                    .build();
            positionList.add(position);
        }
        strategy.setPositionPnLList(positionList);
        return strategy;
    }

}
