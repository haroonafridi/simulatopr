package com.hkcapital.portoflio.service;

import com.hkcapital.portoflio.model.SRMatrix;
import com.hkcapital.portoflio.model.Strategy;

import java.util.List;

public interface SRMatrixService extends Service
{
    SRMatrix addSRMatrix(SRMatrix sRMatrix);

    void removeSRMatrix(SRMatrix sRMatrix);

    SRMatrix updateSRMatrix(SRMatrix sRMatrix);

    List<SRMatrix> findAll();

    SRMatrix findById(Integer id);

    void removeById(Integer id);

    void removeAll();

}
