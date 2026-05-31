package com.hkcapital.portflio.repository.bandlogger;

import com.hkcapital.portflio.model.BandLogger;
import com.hkcapital.portflio.model.Candle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface BandLoggerRepository extends JpaRepository<BandLogger, Integer>
{
}
