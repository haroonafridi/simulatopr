package com.hkcapital.portoflio.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "trading_sessions")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class TradingTimeFrames
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "value")
    private Integer value;

    @Column(name = "unit")
    private String unit;

    public TradingTimeFrames(Integer value, String unit)
    {
        this.value = value;
        this.unit = unit;
    }
}
