package com.luckybusiness.luckyapp.services;

import com.luckybusiness.luckyapp.filters.GraduateFilter;
import com.luckybusiness.luckyapp.model.dtos.CvDataDto;
import com.luckybusiness.luckyapp.model.dtos.GraduateDto;
import com.luckybusiness.luckyapp.model.entities.CvData;
import com.luckybusiness.luckyapp.model.entities.Graduate;

import java.util.List;
import java.util.Optional;

public interface GraduateService {
    List<Graduate> getGraduates(GraduateFilter filter);
    Graduate getGraduateById(Long id);
    Optional<Graduate> getGraduateByUniqueCode(String uniqueCode);
    long createGraduate(GraduateDto dto);
    long updateGraduate(GraduateDto dto, Long id);
    void deleteGraduate(Long id, String uniqueCode);
    CvData getCv(Long id);
    void setGraduateVisibility(Long id, String uniqueCode, boolean visibility);
}
