package com.hkcapital.portoflio.service.impl;

import com.hkcapital.portoflio.model.SRMatrix;
import com.hkcapital.portoflio.model.Strategy;
import com.hkcapital.portoflio.repository.SRMatrixRepository;
import com.hkcapital.portoflio.repository.StrategyRepository;
import com.hkcapital.portoflio.service.SRMatrixService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SRMatrixServiceImpl implements SRMatrixService
{
    private final SRMatrixRepository srMatrixRepository;

    public SRMatrixServiceImpl(SRMatrixRepository srMatrixRepository)
    {
        this.srMatrixRepository = srMatrixRepository;
    }

    @Override
    public SRMatrix addSRMatrix(SRMatrix sRMatrix)
    {
        return srMatrixRepository.save(sRMatrix);
    }

    @Override
    public void removeSRMatrix(SRMatrix sRMatrix)
    {
        srMatrixRepository.delete(sRMatrix);
    }

    @Override
    public SRMatrix updateSRMatrix(SRMatrix sRMatrix)
    {
        return null;
    }

    @Override
    public List<SRMatrix> findAll()
    {
        return srMatrixRepository.findAll();
    }

    @Override
    public SRMatrix findById(Integer id)
    {
        final Optional<SRMatrix> strategy = srMatrixRepository.findById(id);
        return !strategy.isEmpty() ? strategy.get() : null;
    }

    @Override
    public void removeById(Integer id)
    {
        srMatrixRepository.findById(id).ifPresent(strategy -> srMatrixRepository.delete(strategy));
    }

    @Override
    public void removeAll()
    {
        srMatrixRepository.deleteAll();
    }

}
