package com.hkcapital.portflio.service.marketstructure.impl;

import com.hkcapital.portflio.model.InstrumentMarketStructure;
import com.hkcapital.portflio.repository.marketstructure.InstrumentMarketStructureRepository;
import com.hkcapital.portflio.service.marketstructure.InstrumentMarketStructureService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InstrumentMarketStructureServiceImpl implements InstrumentMarketStructureService
{
    private final InstrumentMarketStructureRepository instMarkStrctrRepo;

    public InstrumentMarketStructureServiceImpl(InstrumentMarketStructureRepository instMarkStrctrRepo)
    {
        this.instMarkStrctrRepo = instMarkStrctrRepo;
    }

    @Override
    public InstrumentMarketStructure add(InstrumentMarketStructure InstMarkStrctr)
    {
        return instMarkStrctrRepo.save(InstMarkStrctr);
    }

    @Override
    public List<InstrumentMarketStructure> finalAll()
    {
        return instMarkStrctrRepo.findAll();
    }

    @Override
    public List<InstrumentMarketStructure> findByMarketStructureKey(String marketStructureKey)
    {
        return instMarkStrctrRepo.findByMarketStructureKey(marketStructureKey);
    }
}
