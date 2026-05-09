package com.hkcapital.portflio.repository.strategy;

import com.hkcapital.portflio.model.Strategy;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StrategyRepository extends JpaRepository<Strategy, Integer>
{

}
