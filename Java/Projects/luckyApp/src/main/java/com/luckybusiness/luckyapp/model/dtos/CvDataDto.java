package com.luckybusiness.luckyapp.model.dtos;

import com.luckybusiness.luckyapp.validations.OnCreate;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.*;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class CvDataDto {
    @NotBlank(groups = OnCreate.class, message = "Nahrání CV je povinné.")
    private String data;
    @NotNull(message = "Chybí název souboru CV.")
    private String fileName;
    @Pattern(regexp = "^application/pdf$", message = "Nepodporovaný formát, prosím nahrajte soubor v pdf formátu")
    @NotNull
    private String contentType;
}
