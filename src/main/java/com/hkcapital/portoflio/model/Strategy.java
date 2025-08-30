package com.hkcapital.portoflio.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "strategy")
public class Strategy
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "name", length = 200)
    private String name;
    @Column(name = "description", length = 500)
    private String description;

    @Column(name = "creation_date")
    private LocalDateTime creationDate;

    @OneToMany(mappedBy = "strategy", cascade = CascadeType.PERSIST, orphanRemoval = true)
    private List<Position> positionPnLList;

    public Strategy() {}
    public Strategy(final String name,
                    final String description,  //
                    final LocalDateTime creationDate
                    )
    {
        this.name = name;
        this.description = description;
        this.creationDate = creationDate;
    }

    public Integer getId()
    {
        return id;
    }

    public void setId(Integer id)
    {
        this.id = id;
    }

    public String getDescription()
    {
        return description;
    }

    public void setDescription(String description)
    {
        this.description = description;
    }

    public LocalDateTime getCreationDate()
    {
        return creationDate;
    }

    public void setCreationDate(LocalDateTime creationDate)
    {
        this.creationDate = creationDate;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public List<Position> getPositionPnLList()
    {
        return positionPnLList;
    }

    public void setPositionPnLList(List<Position> positionPnLList)
    {
        this.positionPnLList = positionPnLList;
    }
}
