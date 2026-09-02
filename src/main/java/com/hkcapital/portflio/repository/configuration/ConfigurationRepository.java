package com.hkcapital.portflio.repository.configuration;

import com.hkcapital.portflio.model.Configuration;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ConfigurationRepository extends JpaRepository<Configuration, Integer>
{
    Configuration findByPercentAllocationAllowedAndNoOfInsutrmentsAndNoOfPositionsPerInstrumentsAndMaxPercentAllowedPerInstrumentAndLev(Double percentAllocationAllowed,
                                                                                                                                        Integer noOfInsutrments,
                                                                                                                                        Integer noOfPositionsPerInstruments, Double maxPercentAllowedPerInstrument,
                                                                                                                                        Integer lev);


    Configuration findByCode(String conde);
}
