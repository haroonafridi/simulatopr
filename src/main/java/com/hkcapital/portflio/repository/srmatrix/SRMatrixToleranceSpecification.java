package com.hkcapital.portflio.repository.srmatrix;

import com.hkcapital.portflio.model.SRMatrixTolerance;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;


public class SRMatrixToleranceSpecification
{
    public static Specification<SRMatrixTolerance> byFilter(SRMatrixToleranceFilter filter)
    {

        return (root, query, cb) ->
        {

            List<Predicate> predicates = new ArrayList<>();

            if (filter.getInstrumentId() != null)
            {
                predicates.add(
                        cb.equal(
                                root.get("instrumentTicker").get("id"),
                                filter.getInstrumentId()
                        )
                );
            }


            if (filter.getTimeFrame() != null)
            {
                predicates.add(
                        cb.equal(
                                root.get("timeFrame"),
                                filter.getTimeFrame()
                        )
                );
            }

            if (filter.getTimeFrameUnit() != null)
            {
                predicates.add(
                        cb.equal(
                                root.get("timeFrameUnit"),
                                filter.getTimeFrameUnit()
                        )
                );
            }

            if (filter.getL_s_tolerance_percent() != null)
            {
                predicates.add(
                        cb.equal(
                                root.get("l_s_tolerance_percent"),
                                filter.getL_s_tolerance_percent()
                        )
                );
            }

            if (filter.getR_s_tolerance_percent() != null)
            {
                predicates.add(
                        cb.equal(
                                root.get("r_s_tolerance_percent"),
                                filter.getR_s_tolerance_percent()
                        )
                );
            }


            if (filter.getL_r_tolerance_percent() != null)
            {
                predicates.add(
                        cb.equal(
                                root.get("l_r_tolerance_percent"),
                                filter.getL_r_tolerance_percent()
                        )
                );
            }


            if (filter.getR_r_tolerance_percent() != null)
            {
                predicates.add(
                        cb.equal(
                                root.get("r_r_tolerance_percent"),
                                filter.getR_r_tolerance_percent()
                        )
                );
            }


            if (filter.getTakeProfitPercent() != null)
            {
                predicates.add(
                        cb.equal(
                                root.get("takeProfitPercent"),
                                filter.getTakeProfitPercent()
                        )
                );
            }


            if (filter.getStopLossPercent() != null)
            {
                predicates.add(
                        cb.equal(
                                root.get("stopLossPercent"),
                                filter.getStopLossPercent()
                        )
                );
            }

            if (filter.getActive() != null)
            {
                predicates.add(
                        cb.equal(
                                root.get("active"),
                                filter.getActive()
                        )
                );
            }


            if (filter.getCreationDate() != null)
            {
                predicates.add(
                        cb.equal(
                                root.get("creationDate"),
                                filter.getCreationDate()
                        )
                );
            }

            return cb.and(
                    predicates.toArray(new Predicate[0])
            );
        };
    }
}