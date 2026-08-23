package com.hkcapital.portflio.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.time.LocalDateTime;

@Entity
@Table(name = "market_structure")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class InstrumentMarketStructure
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String bandType;
    private String bandKey;
    private Double lowerBound;
    private Double upperBound;
    private Integer marketVisitCount;
    private Instant initialVisitedTime;
    private Instant lastVisitedTime;
    private long timeDifference;
    private Integer timeFrame;
    private String timeFrameUnit;
    private LocalDateTime creationDate;
    private String marketStructureKey;
    @ManyToOne(cascade = CascadeType.MERGE)

    @JoinColumn(name = "instrument_id", referencedColumnName = "id")

    private Instrument instrument;
}
