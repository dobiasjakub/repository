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
@SequenceGenerator(name = "employment_regimes_seq", sequenceName = "employment_regimes_seq", allocationSize = 1)
public class EmploymentRegimes {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "employment_regimes_seq")
    private Long id;
    private boolean onSite;
    private boolean hybrid;
    private boolean remote;

    @OneToOne(mappedBy = "employmentRegimes")
    private Graduate graduate;
}
