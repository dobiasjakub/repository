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
@SequenceGenerator(name = "employment_types_seq", sequenceName = "employment_types_seq", allocationSize = 1)
public class EmploymentTypes {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "employment_types_seq")
    private Long id;
    private boolean fullTime;
    private boolean halfTime;
    private boolean partTime;
    private String partTimeHours;

    @OneToOne(mappedBy = "employmentTypes")
    private Graduate graduate;
}
