package com.hkcapital.portflio.repository.marketconditions;

import com.hkcapital.portflio.model.MarketConditions;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MarketconditionsRepository extends JpaRepository<MarketConditions, Integer>
{

}
