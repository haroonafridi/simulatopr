package com.hkcapital.portflio.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "TradingIndicators")
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class TradingIndicators
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

    public TradingIndicators(Instrument instrument, String name,
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
}
