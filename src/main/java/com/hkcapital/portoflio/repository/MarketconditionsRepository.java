package com.hkcapital.portoflio.repository;

import com.hkcapital.portoflio.model.Instrument;
import com.hkcapital.portoflio.model.MarketConditions;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MarketconditionsRepository extends JpaRepository<MarketConditions, Integer>
{

}
