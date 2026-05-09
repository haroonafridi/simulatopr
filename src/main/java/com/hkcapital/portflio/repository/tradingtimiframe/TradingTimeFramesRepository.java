package com.hkcapital.portflio.repository.tradingtimiframe;

import com.hkcapital.portflio.model.TradingTimeFrames;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TradingTimeFramesRepository extends JpaRepository<TradingTimeFrames, Integer>
{

}
