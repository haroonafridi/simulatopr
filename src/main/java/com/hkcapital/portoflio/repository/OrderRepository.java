package com.hkcapital.portoflio.repository;

import com.hkcapital.portoflio.order.EtoroOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<EtoroOrder, Integer>
{
}
