package com.hkcapital.portflio.service.orders;

import com.hkcapital.portflio.market.indicators.CandleDto;

public interface IndicatorProcessor
{
  void onCandleClose(CandleDto candle);

  String getType();
  double getValue();
}
