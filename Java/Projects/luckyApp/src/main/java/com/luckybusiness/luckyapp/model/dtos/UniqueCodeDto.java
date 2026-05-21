package com.luckybusiness.luckyapp.model.dtos;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class UniqueCodeDto {
    @NotBlank(message = "Chybí unikátní kód")
    private String code;
}
