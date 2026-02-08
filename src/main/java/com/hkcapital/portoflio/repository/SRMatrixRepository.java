package com.hkcapital.portoflio.repository;

import com.hkcapital.portoflio.model.SRMatrix;
import com.hkcapital.portoflio.model.TradingSessions;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SRMatrixRepository extends JpaRepository<SRMatrix, Integer>
{

}
