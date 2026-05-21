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
@SequenceGenerator(name = "cv_data_seq", sequenceName = "cv_data_seq", allocationSize = 1)
public class CvData {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "cv_data_seq")
    private Long id;

    @Lob
    @Basic(fetch = FetchType.LAZY)
    private byte[] data;

    @Column(length = 255)
    private String fileName;

    @Column(length = 100)
    private String contentType;

    @OneToOne(mappedBy = "cvData")
    private Graduate graduate;
}
