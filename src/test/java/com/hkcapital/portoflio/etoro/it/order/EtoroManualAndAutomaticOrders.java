package com.hkcapital.portoflio.etoro.it.order;


import com.hkcapital.portoflio.etoro.EtoroAbstractTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;


@SpringBootTest
@Transactional
public class EtoroManualAndAutomaticOrders extends EtoroAbstractTest
{
    @DisplayName("Should persist gold manual and automatic orders")
    @Test
    public void shouldCreateGoldManualAndAutomaticOrder()
    {
        Assertions.assertEquals(0, orderRepository.findAll().size());
        shouldCreateAndAssertAutomaticGoldOrdersEtoroOrders(AUTO_ORDER_ID_27);
        shouldCreateAndAssertGoldManualMarketOrderEtoroOrders();
        Assertions.assertEquals(2, orderRepository.findAll().size());
    }


}
