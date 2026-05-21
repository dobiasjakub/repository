package com.luckybusiness.luckyapp.filters.specifications;

import com.luckybusiness.luckyapp.filters.GraduateFilter;
import com.luckybusiness.luckyapp.model.entities.*;
import jakarta.persistence.criteria.*;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class GraduateSpecification {

    public static Specification<Graduate> filterBy(GraduateFilter f) {
        return (root, query, cb) -> {
            query.distinct(true);
            List<Predicate> predicates = new ArrayList<>();

            Join<Graduate, WorkRegions> workRegionsJoin = root.join("workRegions");
            Join<Graduate, ContractTypes> contractTypesJoin = root.join("contractTypes");
            Join<Graduate, EmploymentTypes> employmentTypesJoin = root.join("employmentTypes");
            Join<Graduate, EmploymentRegimes> employmentRegimesJoin = root.join("employmentRegimes");
            Join<Graduate, PreferredRoles> preferredRolesJoin = root.join("preferredRoles");

            Predicate regionPredicate = createPredicate(cb, workRegionsJoin, new LinkedHashMap<>() {{
                put("praha", f.isPraha());
                put("stredoceskyKraj", f.isStredoceskyKraj());
                put("jihoceskyKraj", f.isJihoceskyKraj());
                put("plzenskyKraj", f.isPlzenskyKraj());
                put("karlovarskyKraj", f.isKarlovarskyKraj());
                put("usteckyKraj", f.isUsteckyKraj());
                put("libereckyKraj", f.isLibereckyKraj());
                put("kralovehradeckyKraj", f.isKralovehradeckyKraj());
                put("pardubickyKraj", f.isPardubickyKraj());
                put("krajVysocina", f.isKrajVysocina());
                put("jihomoravskyKraj", f.isJihomoravskyKraj());
                put("zlinskyKraj", f.isZlinskyKraj());
                put("olomouckyKraj", f.isOlomouckyKraj());
                put("moravskoslezskyKraj", f.isMoravskoslezskyKraj());
            }});

            Predicate contractPredicate = createPredicate(cb, contractTypesJoin, Map.of(
                    "hpp", f.isContractTypeHpp(),
                    "dppDpc", f.isContractTypeDppDpc(),
                    "ico", f.isContractTypeIco()
            ));

            Predicate employmentTypePredicate = createPredicate(cb, employmentTypesJoin, Map.of(
                    "fullTime", f.isFullTime(),
                    "halfTime", f.isHalfTime(),
                    "partTime", f.isPartTime()
            ));

            Predicate regimePredicate = createPredicate(cb, employmentRegimesJoin, Map.of(
                    "onSite", f.isOnSite(),
                    "hybrid", f.isHybrid(),
                    "remote", f.isRemote()
            ));

            Predicate rolesPredicate = createPredicate(cb, preferredRolesJoin, new LinkedHashMap<>() {{
                put("tester", f.isTester());
                put("itSupport", f.isItSupport());
                put("automationTester", f.isAutomationTester());
                put("testAnalyst", f.isTestAnalyst());
                put("testManager", f.isTestManager());
                put("frontendDeveloper", f.isFrontendDeveloper());
                put("backendDeveloper", f.isBackendDeveloper());
                put("dataAnalyst", f.isDataAnalyst());
                put("scrumMaster", f.isScrumMaster());
                put("productOwner", f.isProductOwner());
            }});

            addIfNotNull(predicates, regionPredicate);
            addIfNotNull(predicates, contractPredicate);
            addIfNotNull(predicates, employmentTypePredicate);
            addIfNotNull(predicates, regimePredicate);
            addIfNotNull(predicates, rolesPredicate);

            if (predicates.isEmpty()) {
                return null;
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }

    private static Predicate createPredicate(CriteriaBuilder cb, Path<?> path, Map<String, Boolean> conditions) {
        List<Predicate> preds = new ArrayList<>();
        conditions.forEach((key, value) -> {
            if (value) {
                preds.add(cb.isTrue(path.get(key)));
            }
        });
        return preds.isEmpty() ? null : cb.or(preds.toArray(new Predicate[0]));
    }

    private static void addIfNotNull(List<Predicate> list, Predicate predicate) {
        if (predicate != null) list.add(predicate);
    }
}
