package com.hkcapital.portflio.repository.marketstructure;

import com.hkcapital.portflio.model.InstrumentMarketStructure;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InstrumentMarketStructureRepository extends JpaRepository<InstrumentMarketStructure, Integer>
{
}
