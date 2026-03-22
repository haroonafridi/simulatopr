package com.hkcapital.portoflio.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "market_conditions")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class MarketConditions
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "instrument_id", referencedColumnName = "id")
    private Instrument instrument;
    @Column(name="day_low")
    private Double dayLow;
    @Column(name="day_high")
    private Double dayHigh;
    @Column(name = "percent_move")
    private Double percentMove;

    public MarketConditions(Instrument instrument, Double dayLow, Double dayHigh, Double percentMove)
    {
        this.instrument = instrument;
        this.dayLow = dayLow;
        this.dayHigh = dayHigh;
        this.percentMove = percentMove;
    }
}
