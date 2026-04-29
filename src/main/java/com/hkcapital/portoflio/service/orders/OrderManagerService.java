package com.hkcapital.portoflio.service.orders;

import com.hkcapital.portoflio.broker.etoro.dto.order.EtoroMarketOrderDto;
import com.hkcapital.portoflio.broker.etoro.dto.order.EtoroOrderDetails;
import com.hkcapital.portoflio.model.etoro.EtoroOrder;
import com.hkcapital.portoflio.service.marketfeed.subscriber.MarketFeedSubscriber;
import com.hkcapital.portoflio.service.registry.Service;

import java.util.List;

public interface OrderManagerService extends Service , MarketFeedSubscriber
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
