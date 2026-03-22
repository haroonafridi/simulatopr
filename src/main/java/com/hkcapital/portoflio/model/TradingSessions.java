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
public class TradingSessions
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;
    @Column(name = "start_time")

    private String startTime;

    @Column(name = "end_time")
    private String endTime;


    public TradingSessions(String name, String description, String startTime, String endTime)
    {
        this.name = name;
        this.description = description;
        this.startTime = startTime;
        this.endTime = endTime;
    }
}
