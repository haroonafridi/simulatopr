package com.hkcapital.portoflio.service.impl.etoro;

import com.hkcapital.portoflio.etoro.apiinformation.EtoroAPIInformationDemoServiceImpl;
import com.hkcapital.portoflio.etoro.dto.order.EtoroMarketOrderDto;
import com.hkcapital.portoflio.etoro.master.Instruments;
import com.hkcapital.portoflio.etoro.websocket.EToroWSClient;
import com.hkcapital.portoflio.etoro.websocket.LiveInstrumentRate;
import com.hkcapital.portoflio.model.Instrument;
import com.hkcapital.portoflio.model.SRMatrix;
import com.hkcapital.portoflio.order.OderTypes;
import com.hkcapital.portoflio.service.EtoroWebSocketManagerService;
import com.hkcapital.portoflio.service.InstrumentService;
import com.hkcapital.portoflio.service.OrderManagerService;
import com.hkcapital.portoflio.service.SRMatrixService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static com.hkcapital.portoflio.etoro.CalcUtils.calculateTargetPrice;

@Service
public class EtoroWebServiceSocketManagerImpl implements EtoroWebSocketManagerService
{
    private final Logger logger = LoggerFactory.getLogger(EtoroWebServiceSocketManagerImpl.class);

    private final SRMatrixService srMatrixService;
    private final OrderManagerService orderManagerService;

    private final InstrumentService instrumentService;


    public EtoroWebServiceSocketManagerImpl(final SRMatrixService srMatrixService,
                                            final OrderManagerService orderManagerService,
                                            final InstrumentService instrumentService)
    {
        this.srMatrixService = srMatrixService;
        this.orderManagerService = orderManagerService;
        this.instrumentService = instrumentService;
    }

    @Override
    public void subscribeAndSchedule()
    {
        EToroWSClient eToroWSClient = new EToroWSClient();
        ScheduledExecutorService schedulerWs = Executors.newSingleThreadScheduledExecutor();
        schedulerWs.submit(() ->
        {
            try
            {
                eToroWSClient.start(new EtoroAPIInformationDemoServiceImpl());

            } catch (InterruptedException e)
            {
                throw new RuntimeException(e);
            }
            logger.error("Cannot subscribe to etoro WS");
        });


        ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();

        scheduler.scheduleAtFixedRate(() ->
        {
            try
            {
                logger.info("Executing background orders...");
                LiveInstrumentRate instrumentRate = eToroWSClient.getLiveInstrumentRate();
                Instrument instrument = instrumentService.findAll().stream()
                        .filter(e -> e.getEtoroInstrumentId() != null && e.getEtoroInstrumentId().intValue() == Instruments.BTC.getInstrumentId()//
                                .intValue()).findAny()//
                        .get();

                SRMatrix srMatrix = this.srMatrixService.findAll().stream().findAny().get();

                Double maxSlippage = instrument.getMaxSlippage();

                if (instrumentRate != null && instrumentRate.getAsk() != null && instrumentRate.getBid() != null)
                {
                    Double ask = Double.parseDouble(instrumentRate.getAsk());
                    Double bid = Double.parseDouble(instrumentRate.getBid());
                    Double slippage = ask - bid;

                    if (slippage > maxSlippage)
                    {
                        logger.info("Unusual price received, Order rejected due to high slippage,  bid = [{}] , ask = [{}], slippage = [{}] , maxSlippage = [{}]"
                                , bid, ask, slippage, maxSlippage);
                        return;
                    }

                    logger.info("Instrument price received bid = [{}] , ask = [{}] slippage = [{}] , maxSlippage = [{}] sending order for execution",
                            bid, ask, slippage, maxSlippage);

                    Double tp = (calculateTargetPrice(Double.parseDouble(instrumentRate.getAsk()), 20, 50d, 2d));
                    EtoroMarketOrderDto etoroMarketOrderDto = new EtoroMarketOrderDto(Instruments.BTC.getInstrumentId(),
                            true, //
                            1, //
                            50d, //
                            null, //
                            null, //
                            null, //
                            null, //
                            null,
                            OderTypes.AUTO.getOrderType(),
                            bid,
                            ask,
                            maxSlippage,
                            slippage);
                    orderManagerService.createAndSaveMarketOrder((etoroMarketOrderDto));
                }
                //etoroOrderManagerService.createAndSaveMarketOrder(EtoroMarketOrderDto.createDummyOrderGold());
                //    etoroOrderManagerService.createAndSaveMarketOrder(EtoroMarketOrderDto.createDummyOrderNasdaq100());
                //   etoroOrderManagerService.createAndSaveMarketOrder(EtoroMarketOrderDto.createDummyOrderBtc());
            } catch (Exception e)
            {
                logger.error("Error in background task", e);
            }
        }, 0, 1, TimeUnit.MINUTES);

    }
}
