package com.hkcapital.portflio.service.orders.impl.etoro;

import com.hkcapital.portflio.broker.etoro.dto.order.EtoroMarketOrderDto;
import com.hkcapital.portflio.model.Instrument;
import com.hkcapital.portflio.model.Position;
import com.hkcapital.portflio.service.api.etoro.websocket.LiveInstrumentRate;
import com.hkcapital.portflio.values.order.OrderTypes;
import com.hkcapital.portflio.values.timeframe.TimeFrame;

public class EtoroOrderUtil
{
    public static EtoroMarketOrderDto buildSellOrder(LiveInstrumentRate instrumentRate,
                                                     Double sl,
                                                     Double tp,
                                                     Position position,
                                                     Instrument inst,
                                                     String orderInfo,
                                                     TimeFrame timeFrame)
    {
        Double slippage = instrumentRate.getAsk() - instrumentRate.getBid();
        return EtoroMarketOrderDto.builder().instrumentId(inst.getEtoroInstrumentId()) //
                .isBuy(false)
                .leverage(position.getLeverage())
                .amount(position.getCurrentPositionEquity())//
                .stopLossRate(sl)
                .takeProfitRate(tp)//
                .isTslEnabled(null)
                .isNoTakeProfit(null)
                .isNoStopLoss(null)//
                .orderType(OrderTypes.AUTO.getOrderType())//
                .bid(instrumentRate.getBid()).ask(instrumentRate.getAsk())
                .maxAllowedSlippage(inst.getMaxSlippage()) //
                .etoroSlippage(slippage) //
                .orderInfo(orderInfo)
                .timeFrame(timeFrame)
                .build();
    }


    private static EtoroMarketOrderDto buildSellOrderDynamicSlTp(LiveInstrumentRate instrumentRate, Double sl, Double tp, Position position, Instrument inst,
                                                                 String orderInfo,
                                                                 TimeFrame timeFrame)
    {
        Double slippage = instrumentRate.getAsk() - instrumentRate.getBid();
        return EtoroMarketOrderDto.builder().instrumentId(inst.getEtoroInstrumentId()) //
                .isBuy(false)
                .leverage(position.getLeverage())
                .amount(position.getCurrentPositionEquity())//
                .stopLossRate(sl).takeProfitRate(tp)//
                .isTslEnabled(null)
                .isNoTakeProfit(null)
                .isNoStopLoss(null)//
                .orderType(OrderTypes.AUTO.getOrderType())//
                .bid(instrumentRate.getBid()).ask(instrumentRate.getAsk())
                .maxAllowedSlippage(inst.getMaxSlippage()) //
                .etoroSlippage(slippage) //
                .orderInfo(orderInfo)
                .timeFrame(timeFrame)
                .build();
    }


    public static EtoroMarketOrderDto buildBuyOrder(LiveInstrumentRate instrumentRate,
                                                    Double maxSlippage,
                                                    Double tp,
                                                    Double sl,
                                                    Position position,
                                                    Instrument inst,
                                                    Integer leverage,
                                                    String info,
                                                    TimeFrame timeFrame)
    {
        Double slippage = instrumentRate.getAsk() - instrumentRate.getBid();
        return EtoroMarketOrderDto.builder()//
                .instrumentId(inst.getEtoroInstrumentId())//
                .isBuy(true)//
                .leverage(leverage)//
                .amount(position.getCurrentPositionEquity())//
                .stopLossRate(sl)//
                .takeProfitRate(tp)//
                .isTslEnabled(null)
                .isNoTakeProfit(null)
                .isNoStopLoss(null)//
                .orderType(OrderTypes.AUTO.getOrderType())//
                .bid(instrumentRate.getBid())//
                .ask(instrumentRate.getAsk())//
                .maxAllowedSlippage(maxSlippage)//
                .etoroSlippage(slippage)
                .orderInfo(info)
                .timeFrame(timeFrame)
                .build();
    }


    public static EtoroMarketOrderDto buildFlatBuyOrder(LiveInstrumentRate instrumentRate,
                                                        Double maxSlippage,
                                                        Double sl,
                                                        Double tp,
                                                        Double equity,
                                                        Integer instrumentId,
                                                        Integer leverage,
                                                        String info,
                                                        TimeFrame timeFram)
    {
        Double slippage = instrumentRate.getAsk() - instrumentRate.getBid();
        return EtoroMarketOrderDto.builder()//
                .instrumentId(instrumentId)//
                .isBuy(true)//
                .leverage(leverage)//
                .amount(equity)//
                .stopLossRate(sl)//
                .takeProfitRate(tp)//
                .isTslEnabled(null)
                .isNoTakeProfit(null)
                .isNoStopLoss(null)//
                .orderType(OrderTypes.AUTO.getOrderType())//
                .bid(instrumentRate.getBid())//
                .ask(instrumentRate.getAsk())//
                .maxAllowedSlippage(maxSlippage)//
                .etoroSlippage(slippage)
                .orderInfo(info)
                .timeFrame(timeFram)
                .build();
    }
}
