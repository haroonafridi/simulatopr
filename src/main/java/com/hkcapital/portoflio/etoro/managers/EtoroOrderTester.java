package com.hkcapital.portoflio.etoro.managers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.hkcapital.portoflio.etoro.apiinformation.EtoroAPIInformationService;
import com.hkcapital.portoflio.etoro.apiinformation.EtoroAPIInformationDemoServiceImpl;
import com.hkcapital.portoflio.repository.mocks.OrderRepositoryMock;
import com.hkcapital.portoflio.service.impl.EtoroOrderManagerServiceImpl;

public class EtoroOrderTester
{
    public static void main(String[] args) throws JsonProcessingException
    {

        EtoroAPIInformationService apiInformationService = new EtoroAPIInformationDemoServiceImpl();
        EtoroOrderManagerServiceImpl etoroOrderManagerServiceImpl = //
                new EtoroOrderManagerServiceImpl(new OrderRepositoryMock(), apiInformationService);
        etoroOrderManagerServiceImpl.etoroPortfolio();
    }
}
