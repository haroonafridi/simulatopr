package com.hkcapital.portoflio.service.impl.etoro;

import com.hkcapital.portoflio.etoro.Configuration;
import com.hkcapital.portoflio.etoro.apiinformation.EtoroAPIInformationDemoServiceImpl;
import com.hkcapital.portoflio.etoro.dto.order.EtoroMarketOrderDto;
import com.hkcapital.portoflio.etoro.master.Instruments;
import com.hkcapital.portoflio.etoro.websocket.EToroWSClient;
import com.hkcapital.portoflio.etoro.websocket.LiveInstrumentRate;
import com.hkcapital.portoflio.model.Instrument;
import com.hkcapital.portoflio.model.Position;
import com.hkcapital.portoflio.model.SRMatrix;
import com.hkcapital.portoflio.model.Strategy;
import com.hkcapital.portoflio.order.OderTypes;
import com.hkcapital.portoflio.service.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
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

    private final StrategyService strategyService;

    private final PositionService positionService;



    public EtoroWebServiceSocketManagerImpl(final SRMatrixService srMatrixService,
                                            final OrderManagerService orderManagerService,
                                            final InstrumentService instrumentService,
                                            final StrategyService strategyService,
                                            final PositionService positionService)
    {
        this.srMatrixService = srMatrixService;
        this.orderManagerService = orderManagerService;
        this.instrumentService = instrumentService;
        this.strategyService = strategyService;
        this.positionService = positionService;
    }

    @Override
    public void subscribeAndSchedule()
    {
        EToroWSClient eToroWSClient = new EToroWSClient(instrumentService);
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
                        .filter(e -> e.getEtoroInstrumentId() != null && e.getEtoroInstrumentId().intValue() == Instruments.GOLD.getInstrumentId()//
                                .intValue()).findAny()//
                        .get();

                Double maxSlippage = instrument.getMaxSlippage();

                if (instrumentRate != null && instrumentRate.getAsk() != null && instrumentRate.getBid() != null)
                {
                    if (Configuration.ACTIVATE_AUTOMATIC_TRADING)
                    {
                        logger.info("Sending Automatic trade to etoro!");
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


                        Strategy strategy = strategyService.findById(6);

                        List<Position> positionList = positionService.findByStrategyId(strategy.getId());

                        Position position =  positionList.get(0);

                        Instrument inst = position.getInstrument();

                        Integer leverage = position.getConfigurtaion().getLev();

                        Optional<SRMatrix> srMatrix = this.srMatrixService.findAll()
                                .stream()
                                .filter(matrix -> matrix.getActive() && "MINUTE".equals(matrix.getTimeFrameUnit())  &&
                                        matrix.getTimeFrame()==15).findAny();

                        if(!srMatrix.isPresent())
                        {
                            logger.info("No SR-Matrix found, please check SR-Matrix!");
                            return;
                        }

                        if (ask <= srMatrix.get().getSupport()) //
                        {
                            logger.info("Sending buy order instrument = {} ", inst.getEtoroInstrumentId());
                            EtoroMarketOrderDto etoroMarketOrderDto = new EtoroMarketOrderDto(inst.getEtoroInstrumentId(),
                                    true, //
                                    leverage, //
                                    position.getCurrentPositionEquity(), //
                                    null, //
                                    srMatrix.get().getResistance(), //
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

                        else if(ask >= srMatrix.get().getResistance())
                        {
                            logger.info("Sending sell order order instrument = {} ", inst.getEtoroInstrumentId());
                            EtoroMarketOrderDto etoroMarketOrderDto = new EtoroMarketOrderDto(inst.getEtoroInstrumentId(),
                                    false, //
                                    leverage, //
                                    position.getCurrentPositionEquity(), //
                                    null, //
                                    srMatrix.get().getSupport(), //
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

//                        Double tp = (calculateTargetPrice(Double.parseDouble(instrumentRate.getAsk()), position.getConfigurtaion().getLev(),
//                                position.getCurrentPositionEquity(), 0.50));

                    } else
                    {
                        logger.info("Automatic trading is not yet activated!");
                    }

                }
            } catch (Exception e)
            {
                logger.error("Error in background task", e);
            }
        }, 0, 5, TimeUnit.MINUTES);
    }
}
