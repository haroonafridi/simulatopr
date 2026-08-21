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

    Instrument findByInstrumentTicker(String instrumentTicker);
}
