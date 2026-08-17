package com.hkcapital.portflio.model;

import com.hkcapital.portflio.service.positions.dto.PositionDTO;
import com.hkcapital.portflio.service.strategy.dto.StrategyDTO;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "strategy")
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class Strategy
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "name", length = 200)
    private String name;
    @Column(name = "description", length = 500)
    private String description;
    @Column(name = "capital_allocated")
    private Double capitalAllocated;
    @Column(name = "creation_date")
    private LocalDateTime creationDate;
    @Column(name = "active")
    private Boolean active;
    @OneToMany(mappedBy = "strategy", cascade = CascadeType.PERSIST, orphanRemoval = true)
    private List<Position> positionPnLList;

    public StrategyDTO buildStrategyDTO()
    {

        List<PositionDTO> positionDTOList = new ArrayList<>();

        for (Position position : positionPnLList){
            positionDTOList.add(position.buildPositionDTO());
        }

        return StrategyDTO
                .builder()
                .name(name)
                .description(description)
                .capitalAllocated(capitalAllocated)
                .creationDate(creationDate)
                .active(active)
                .positionPnLList(positionDTOList)
                .build();
    }

}
