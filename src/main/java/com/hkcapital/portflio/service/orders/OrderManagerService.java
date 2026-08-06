package com.hkcapital.portflio.service.orders;

import com.hkcapital.portflio.broker.etoro.dto.order.EtoroMarketOrderDto;
import com.hkcapital.portflio.broker.etoro.dto.order.EtoroOrderDetails;
import com.hkcapital.portflio.model.etoro.EtoroOrder;
import com.hkcapital.portflio.service.marketfeed.subscriber.MarketFeedSubscriber;
import com.hkcapital.portflio.service.registry.Service;

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

    EtoroOrder findByorderID(Long orderId);

    EtoroOrder closeEtoroOrder(EtoroOrder etoroOrder);

    EtoroOrder addEtoroOrder(EtoroOrder etoroOrder);

    List<EtoroOrder> findByInstrumentIDAndOderTypeAndStatusAndTimeFrameAndTimeFrameUnitAndIsBuy
            (
                    Integer InstrumentID,
                    String oderType,
                    String status,
                    Integer timeFrame,
                    String timeFrameUnit,
                    boolean isBuy
            );

}
