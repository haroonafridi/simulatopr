package com.hkcapital.portflio.market.structure;

import org.junit.jupiter.api.Test;

class DateTimeUtilTest
{
    @Test
    public void shouldReturnToday()//
    {
        System.out.println(DateTimeUtil.toDay().getValue());
    }
}