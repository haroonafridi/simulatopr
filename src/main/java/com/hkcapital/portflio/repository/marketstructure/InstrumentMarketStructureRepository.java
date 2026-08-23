package com.hkcapital.portflio.repository.marketstructure;

import com.hkcapital.portflio.model.InstrumentMarketStructure;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InstrumentMarketStructureRepository extends JpaRepository<InstrumentMarketStructure, Integer>
{
    List<InstrumentMarketStructure> findByMarketStructureKey(String marketStructureKey);
}
