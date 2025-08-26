package com.hkcapital.portoflio.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.hkcapital.portoflio.model.Configuration;

@Repository
public interface ConfigurationRepository extends JpaRepository<Configuration, Integer>
{

}
