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
@SequenceGenerator(name = "preferred_roles_seq", sequenceName = "preferred_roles_seq", allocationSize = 1)
public class PreferredRoles {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "preferred_roles_seq")
    private Long id;
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

    @OneToOne(mappedBy = "preferredRoles")
    private Graduate graduate;
}
