package com.hkcapital.portoflio.repository.orders.etoro;

import com.hkcapital.portoflio.model.etoro.EtoroOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EtoroOrderRepository extends JpaRepository<EtoroOrder, Integer>
{
    EtoroOrder findByorderID(Long orderId);
    List<EtoroOrder> findByInstrumentIDAndOderType(Integer InstrumentID, String oderType);

    List<EtoroOrder> findByInstrumentIDAndOderTypeAndStatus(Integer InstrumentID, String oderType , String statu);
    List<EtoroOrder> findByInstrumentID(Integer InstrumentID);
}
