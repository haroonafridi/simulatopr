package com.hkcapital.portoflio.repository;

import com.hkcapital.portoflio.model.TradingSessions;
import com.hkcapital.portoflio.model.TradingTimeFrames;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TradingSessionsRepository extends JpaRepository<TradingSessions, Integer>
{

}
