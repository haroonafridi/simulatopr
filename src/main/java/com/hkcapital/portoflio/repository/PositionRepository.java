package com.hkcapital.portoflio.repository;

import com.hkcapital.portoflio.model.Position;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PositionRepository extends JpaRepository<Position, Integer>
{

}
