package com.hkcapital.portflio.model;

import com.hkcapital.portflio.service.tradingindicators.dto.TradingIndicatorDTO;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "trading_indicators")
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class TradingIndicator
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "instrument_id", referencedColumnName = "id")
    private Instrument instrument;
    private String name;
    private int timeFrame;
    private String timeFrameUnit;
    private int period;

    public TradingIndicator(Instrument instrument, String name,
                            int timeFrame,
                            String timeFrameUnit,
                            int period)
    {
        this.instrument = instrument;
        this.name = name;
        this.timeFrame = timeFrame;
        this.timeFrameUnit = timeFrameUnit;
        this.period = period;
    }

    public TradingIndicatorDTO buildTradingIndicatorDTO()
    {

        return TradingIndicatorDTO.builder().instrument(this.instrument.buildDto())
                .period(this.period)
                .name(this.name)
                .timeFrame(this.timeFrame)
                .timeFrameUnit(this.timeFrameUnit)
                .build();
    }

    public String getKey()
    {
        String key = name + "_" + period + "_" + timeFrame + "_" + timeFrameUnit;
        if (key == null)
        {
            throw new IllegalArgumentException("Invalid indicator key");
        }
        return key;
    }
}
