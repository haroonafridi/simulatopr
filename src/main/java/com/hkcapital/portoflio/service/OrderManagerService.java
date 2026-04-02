package com.hkcapital.portoflio.service;

import com.hkcapital.portoflio.etoro.dto.order.EtoroMarketOrderDto;
import com.hkcapital.portoflio.etoro.dto.order.EtoroOrderDetails;
import com.hkcapital.portoflio.order.EtoroOrder;

import java.util.List;

public interface OrderManagerService extends Service
{

    EtoroOrder createAndSaveMarketOrder(final EtoroMarketOrderDto etoroMarketOrderDto);

    EtoroOrder saveOrder(EtoroMarketOrderDto etoroMarketOrderDto,
                         EtoroOrderDetails orderDetails,
                         String etoroOrderToken);

    List<EtoroOrder> findByInstrumentIDAndOderType(Integer InstrumentID, String oderType);

    EtoroOrder closeEtoroOrder(Integer etoroOrderId);

    List<EtoroOrder> findByInstrumentID(Integer InstrumentID);

    List<EtoroOrder> fetchAndCloseEtoroOrder();

}
