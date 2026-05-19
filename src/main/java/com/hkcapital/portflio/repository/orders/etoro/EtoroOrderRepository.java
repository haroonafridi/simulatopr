package com.hkcapital.portflio.repository.orders.etoro;

import com.hkcapital.portflio.model.etoro.EtoroOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EtoroOrderRepository extends JpaRepository<EtoroOrder, Integer>
{
    EtoroOrder findByorderID(Long orderId);

    List<EtoroOrder> findByInstrumentIDAndOderType(Integer InstrumentID, String oderType);

    List<EtoroOrder> findByInstrumentIDAndOderTypeAndStatus(Integer InstrumentID, String oderType, String status);

    List<EtoroOrder> findByInstrumentIDAndOderTypeAndStatusAndTimeFrameAndTimeFrameUnit
            (
                    Integer InstrumentID,
                    String oderType,
                    String status,
                    Integer timeFrame,
                    String timeFrameUnit
            );

    List<EtoroOrder> findByInstrumentID(Integer InstrumentID);
}
