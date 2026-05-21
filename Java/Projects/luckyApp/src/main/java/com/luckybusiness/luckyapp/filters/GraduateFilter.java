package com.luckybusiness.luckyapp.filters;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Schema(description = "Filtr pro hledání absolventů. Každé pole představuje boolean příznak – pokud je true, zahrne se do filtru. " +
        "Pokud je vše false, vyhledají se všichni absolventi dle specifikace.")
public class GraduateFilter {

    // Kraje
    @Schema(description = "Hledat v regionu Praha")
    private boolean praha;
    private boolean stredoceskyKraj;
    private boolean jihoceskyKraj;
    private boolean plzenskyKraj;
    private boolean karlovarskyKraj;
    private boolean usteckyKraj;
    private boolean libereckyKraj;
    private boolean kralovehradeckyKraj;
    private boolean pardubickyKraj;
    private boolean krajVysocina;
    private boolean jihomoravskyKraj;
    private boolean zlinskyKraj;
    private boolean olomouckyKraj;
    private boolean moravskoslezskyKraj;

    // Typ spolupráce
    @Schema(description = "HPP (Hlavní pracovní poměr)")
    private boolean contractTypeHpp;
    @Schema(description = "DPP/DPČ (Dohoda o provedení práce / Dohoda o pracovní činnosti)")
    private boolean contractTypeDppDpc;
    @Schema(description = "IČO (OSVČ)")
    private boolean contractTypeIco;

    // Úvazky
    @Schema(description = "Plný úvazek")
    private boolean fullTime;
    private boolean halfTime;
    private boolean partTime;

    // Režimy práce
    @Schema(description = "Práce na místě (on-site)")
    private boolean onSite;
    private boolean hybrid;
    private boolean remote;

    // Role
    @Schema(description = "Tester")
    private boolean tester;
    private boolean itSupport;
    private boolean automationTester;
    private boolean testAnalyst;
    private boolean testManager;
    private boolean frontendDeveloper;
    private boolean backendDeveloper;
    private boolean dataAnalyst;
    private boolean scrumMaster;
    private boolean productOwner;
}
