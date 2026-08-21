package com.hkcapital.portflio.repository.srmatrix;

import com.hkcapital.portflio.model.SRMatrix;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;


public class SRMatrixSpecification
{
    public static Specification<SRMatrix> byFilter(SRMatrixFilter sRMatrixFilter)
    {

        return (root, query, cb) ->
        {

            List<Predicate> predicates = new ArrayList<>();

            if (sRMatrixFilter.getTimeFrame() != null)
            {
                predicates.add(
                        cb.equal(
                                root.get("timeFrame"),
                                sRMatrixFilter.getTimeFrame()
                        )
                );
            }

            if (sRMatrixFilter.getTimeFrameUnit() != null)
            {
                predicates.add(
                        cb.equal(
                                root.get("timeFrameUnit"),
                                sRMatrixFilter.getTimeFrameUnit()
                        )
                );
            }

            if (sRMatrixFilter.getInstrumentId() != null)
            {
                predicates.add(
                        cb.equal(
                                root.get("instrument").get("id"),
                                sRMatrixFilter.getInstrumentId()
                        )
                );
            }

            if (sRMatrixFilter.getL_s_tolerance() != null)
            {
                predicates.add(
                        cb.equal(
                                root.get("l_s_tolerance"),
                                sRMatrixFilter.getL_s_tolerance()
                        )
                );
            }

            if (sRMatrixFilter.getR_s_tolerance() != null)
            {
                predicates.add(
                        cb.equal(
                                root.get("r_s_tolerance"),
                                sRMatrixFilter.getR_s_tolerance()
                        )
                );
            }

            if (sRMatrixFilter.getSupport() != null)
            {
                predicates.add(
                        cb.equal(
                                root.get("support"),
                                sRMatrixFilter.getSupport()
                        )
                );
            }

            if (sRMatrixFilter.getResistance() != null)
            {
                predicates.add(
                        cb.equal(
                                root.get("resistance"),
                                sRMatrixFilter.getResistance()
                        )
                );
            }

            if (sRMatrixFilter.getL_r_tolerance() != null)
            {
                predicates.add(
                        cb.equal(
                                root.get("l_r_tolerance"),
                                sRMatrixFilter.getL_r_tolerance()
                        )
                );
            }

            if (sRMatrixFilter.getR_r_tolerance() != null)
            {
                predicates.add(
                        cb.equal(
                                root.get("r_r_tolerance"),
                                sRMatrixFilter.getR_r_tolerance()
                        )
                );
            }

            if (sRMatrixFilter.getTakeProfit() != null)
            {
                predicates.add(
                        cb.equal(
                                root.get("takeProfit"),
                                sRMatrixFilter.getTakeProfit()
                        )
                );
            }

            if (sRMatrixFilter.getStopLoss() != null)
            {
                predicates.add(
                        cb.equal(
                                root.get("stopLoss"),
                                sRMatrixFilter.getStopLoss()
                        )
                );
            }

            if (sRMatrixFilter.getActive() != null)
            {
                predicates.add(
                        cb.equal(
                                root.get("active"),
                                sRMatrixFilter.getActive()
                        )
                );
            }

            return cb.and(
                    predicates.toArray(new Predicate[0])
            );
        };
    }
}