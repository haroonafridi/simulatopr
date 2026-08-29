package com.hkcapital.portflio.repository.instrumentmarketstructureconf;

import com.hkcapital.portflio.model.Instrument;
import com.hkcapital.portflio.model.InstrumentMarketStructureConf;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InstrumentMarketStructureConfRepository //
        extends JpaRepository<InstrumentMarketStructureConf, Integer>
{
    List<InstrumentMarketStructureConf> findByActive(Boolean active);
    List<InstrumentMarketStructureConf>  findByInstrumentAndActiveOrderByMarketOrder(Instrument instrument, boolean active);

}
