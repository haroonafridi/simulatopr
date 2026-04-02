package com.hkcapital.portoflio.repository;

import com.hkcapital.portoflio.order.EtoroOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<EtoroOrder, Integer>
{
    EtoroOrder findByorderID(Long orderId);
    List<EtoroOrder> findByInstrumentIDAndOderType(Integer InstrumentID, String oderType);

    List<EtoroOrder> findByInstrumentIDAndOderTypeAndStatus(Integer InstrumentID, String oderType , String statu);
    List<EtoroOrder> findByInstrumentID(Integer InstrumentID);
}
