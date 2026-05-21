package com.luckybusiness.luckyapp.repositories;

import com.luckybusiness.luckyapp.model.entities.Graduate;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface GraduateRepository extends JpaRepository<Graduate, Long>, JpaSpecificationExecutor<Graduate> {

    @EntityGraph(attributePaths = {
            "contractTypes",
            "employmentTypes",
            "employmentRegimes",
            "preferredRoles",
            "workRegions"
    })
    List<Graduate> findAll(Specification<Graduate> spec, Sort sort);
    Optional<Graduate> findGraduateByIdAndProfileVisibleAndGdprConsent(long id, boolean profileVisible, boolean gdprConsent);
}
