package com.hkcapital.portflio.repository.tradingindicators;

import com.hkcapital.portflio.model.TradingIndicator;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;


public class TradingIndicatorSpecification
{
    public static Specification<TradingIndicator> byFilter(TradingIndicatorsFilter indicatorFilter)
    {
        return (root, query, cb) ->
        {
            List<Predicate> predicates = new ArrayList<>();

            if (indicatorFilter.getName() != null)
            {
                predicates.add(
                        cb.equal(
                                root.get("name"),
                                indicatorFilter.getName()
                        )
                );
            }

            if (indicatorFilter.getTimeFrame() != null)
            {
                predicates.add(
                        cb.equal(
                                root.get("timeFrame"),
                                indicatorFilter.getTimeFrame()
                        )
                );
            }

            if (indicatorFilter.getTimeFrameUnit() != null)
            {
                predicates.add(
                        cb.equal(
                                root.get("timeFrameUnit"),
                                indicatorFilter.getTimeFrameUnit()
                        )
                );
            }

            if (indicatorFilter.getInstrument() != null)
            {
                predicates.add(
                        cb.equal(
                                root.get("instrument").get("instrumentTicker"),
                                indicatorFilter.getInstrument().getInstrumentTicker()
                        )
                );
            }

            if (indicatorFilter.getPeriod() != null)
            {
                predicates.add(
                        cb.equal(
                                root.get("period"),
                                indicatorFilter.getPeriod()
                        )
                );
            }

            return cb.and(
                    predicates.toArray(new Predicate[0])
            );
        };
    }
}