package com.hkcapital.portflio.model;

import com.hkcapital.portflio.service.instrument.dto.InstrumentDTO;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "instruments")
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class Instrument
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "instrument_name")
    private String name;
    @Column(name = "url")
    private String url;
    @Column(name = "instrument_ticker")
    private String instrumentTicker;
    @Lob
    @Column(name = "instrument_desc", columnDefinition = "TEXT")
    private String instrumentDesc;
    @Column(name = "etoro_instrument_id")
    private Integer etoroInstrumentId;
    @Column(name = "max_slippage")
    private Double maxSlippage;
    @Column(name = "active")
    private Boolean active;

    @Column(name = "with_candle")
    private Boolean withCandle = false;

    @Column(name = "with_feed")
    private Boolean withFeed = false;

    @Column(name = "with_band")
    private Boolean withBand = false;


    public Boolean getWithFeed()
    {
        if (withFeed == null)
        {
            return false;
        }

        return withFeed;
    }


    public Boolean getWithBand()
    {
        if (withBand == null)
        {
            return false;
        }
        return withBand;
    }

    public Boolean getWithCandle()
    {
        if (withCandle == null)
        {
            return false;
        }
        return withCandle;
    }

    @Override
    public String toString()
    {
        return name;
    }

    public InstrumentDTO buildDto()
    {
        return InstrumentDTO.builder()
                .name(name)
                .url(url)
                .instrumentTicker(instrumentTicker)
                .instrumentDesc(instrumentDesc)
                .etoroInstrumentId(etoroInstrumentId)
                .maxSlippage(maxSlippage)
                .active(active)
                .build();
    }
}
