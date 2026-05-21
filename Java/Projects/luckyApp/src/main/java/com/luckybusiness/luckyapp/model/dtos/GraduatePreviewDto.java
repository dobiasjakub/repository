package com.luckybusiness.luckyapp.model.dtos;

import com.luckybusiness.luckyapp.model.enums.Availability;
import com.luckybusiness.luckyapp.model.enums.LuckyCourse;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
@Schema(description = "Preview DTO pro zobrazení v seznamu všech absolventů.")
public class GraduatePreviewDto {
    private Long id;
    private String firstName;
    private String lastName;
    private String languages;
    private Availability availability;
    private LuckyCourse luckyCourse;
    private String motivationLetter;
    private boolean gdprConsent;
    private String otherCourses;
    private boolean profileVisible;
}