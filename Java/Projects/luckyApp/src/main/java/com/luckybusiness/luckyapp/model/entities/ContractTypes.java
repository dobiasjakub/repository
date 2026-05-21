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
@SequenceGenerator(name = "contract_types_seq", sequenceName = "contract_types_seq", allocationSize = 1)
public class ContractTypes {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "contract_types_seq")
    private Long id;
    private boolean hpp;
    private boolean dppDpc;
    private boolean ico;

    @OneToOne(mappedBy = "contractTypes")
    private Graduate graduate;
}
