package com.hkcapital.portflio.market.structure;

import com.hkcapital.portflio.model.Instrument;
import lombok.Builder;
import lombok.Getter;



public class PriceTolerance
{

    public static Double getPriceToleranceBelow(MarketTypes marketTypes, Instrument instrument)
    {
       // if (instrumentTicker.getEtoroInstrumentId() == 18 && //
            //    MarketTypes.GOLD_15_MIN.equals(marketTypes))
       // {
            return 2d;
     //   }
     //   return null;
    }

    public static Double getPriceToleranceAbove(MarketTypes marketTypes, Instrument instrument)
    {
       // if (instrumentTicker.getEtoroInstrumentId() == 18 && //
            //    MarketTypes.GOLD_15_MIN.equals(marketTypes))
        //{
            return 3d;
       // }
       // return null;
    }
}
