package com.hkcapital.portflio.repository.instrument;

import com.hkcapital.portflio.model.Instrument;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InstrumentRepository extends JpaRepository<Instrument, Integer>
{
    Instrument findByEtoroInstrumentId(Integer id);
    List<Instrument> findByActive(Boolean active);

    List<Instrument> findByActiveAndWithCandle(Boolean active, Boolean withCandle);

    List<Instrument> findByActiveAndWithFeed(Boolean active, Boolean withFeed);

    Instrument findByInstrumentTicker(String instrumentTicker);

    List<Instrument> findByActiveAndWithBand(Boolean active, Boolean withBand);

    Instrument findByName(String name);

    List<Instrument> findAllByOrderByNameAsc();
}
