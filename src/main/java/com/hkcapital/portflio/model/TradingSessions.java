package com.hkcapital.portflio.model;

import com.hkcapital.portflio.service.tradingsessions.dto.TradingSessionsDTO;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "trading_sessions")
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
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

    public TradingSessionsDTO buildTradingSessionsDTO()
    {
        return TradingSessionsDTO.builder()
                .description(description)
                .startTime(startTime)
                .endTime(endTime)
                .name(name)
                .build();
    }

}
