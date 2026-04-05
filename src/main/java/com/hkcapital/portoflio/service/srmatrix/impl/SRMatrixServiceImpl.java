package com.hkcapital.portoflio.service.srmatrix.impl;

import com.hkcapital.portoflio.model.Instrument;
import com.hkcapital.portoflio.model.SRMatrix;
import com.hkcapital.portoflio.repository.srmatrix.SRMatrixRepository;
import com.hkcapital.portoflio.service.srmatrix.SRMatrixService;
import org.springframework.data.domain.Sort;
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
        return srMatrixRepository.save(sRMatrix);
    }

    @Override
    public List<SRMatrix> findAll()
    {
        return srMatrixRepository.findAll(Sort.by(Sort.Direction.DESC, "id"));
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

    @Override
    public SRMatrix getReferenceById(Integer id) {
       return srMatrixRepository.getReferenceById(id);
    }

    @Override
    public List<SRMatrix> findByTimeFrameAndTimeFrameUnitAndInstrument(Integer timeFrame, String timeFrameUnit, Instrument instrument)
    {
        return srMatrixRepository.findByTimeFrameAndTimeFrameUnitAndInstrument(timeFrame, timeFrameUnit, instrument);
    }

}
