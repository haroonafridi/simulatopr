package com.hkcapital.portflio.repository.tradingsession;

import com.hkcapital.portflio.model.TradingSessions;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TradingSessionsRepository extends JpaRepository<TradingSessions, Integer>
{

}
