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
@SequenceGenerator(name = "unique_code_seq", sequenceName = "unique_code_seq", allocationSize = 1)
public class UniqueCode {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "unique_code_seq")
    private Long id;

    @Column(unique = true)
    private String code;

    @OneToOne(mappedBy = "uniqueCode")
    private Graduate graduate;
}
