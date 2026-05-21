package com.luckybusiness.luckyapp.repositories;

import com.luckybusiness.luckyapp.model.entities.CvData;
import com.luckybusiness.luckyapp.model.entities.Graduate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

public interface CvDataRepository extends JpaRepository<CvData, Long>, JpaSpecificationExecutor<CvData> {
    Optional<CvData> findByGraduateId(Long id);
}
