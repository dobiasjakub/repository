package com.luckybusiness.luckyapp.model.entities;

import jakarta.persistence.*;
import lombok.*;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity
@SequenceGenerator(name = "work_regions_seq", sequenceName = "work_regions_seq", allocationSize = 1)
public class WorkRegions {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "work_regions_seq")
    private Long id;
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

    @OneToOne(mappedBy = "workRegions")
    private Graduate graduate;
}
