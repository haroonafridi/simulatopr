package com.hkcapital.portoflio.repository.candle;

import com.hkcapital.portoflio.model.Candle;
import com.hkcapital.portoflio.model.Position;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CandleRepository extends JpaRepository<Candle, Integer>
{

}
