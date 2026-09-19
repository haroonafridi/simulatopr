package com.hkcapital.portflio.repository.instrumentmarketstructureconf;

import com.hkcapital.portflio.model.InstrumentMarketStructureConf;
import com.hkcapital.portflio.model.SRMatrix;
import com.hkcapital.portflio.repository.srmatrix.SRMatrixFilter;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;


public class InstrumentMarketStructureConfSpecification
{
    public static Specification<InstrumentMarketStructureConf>
    byFilter(InstrumentMarketStructureConfFilter filter)
    {
        return (root, query, cb) ->
        {
            List<Predicate> predicates = new ArrayList<>();

            if (filter.getStructureName() != null)
            {
                predicates.add(
                        cb.equal(
                                root.get("structureName"),
                                filter.getStructureName()
                        )
                );
            }
            return cb.and(
                    predicates.toArray(new Predicate[0])
            );
        };
    }
}