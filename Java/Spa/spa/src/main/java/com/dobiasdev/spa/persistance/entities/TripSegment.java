package com.dobiasdev.spa.persistance.entities;


import com.dobiasdev.spa.persistance.enums.TravelType;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class TripSegment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "STARTDESTINATION")
    @NotNull
    private String startDestination;

    @Column(name = "ENDDESTINATION")
    @NotNull
    private String endDestination;

    @Column(name = "DISTANCE")
    @NotNull
    private Long distance;

    @Column(name = "TRIPSTART")
    private LocalDateTime tripStart;

    @Column(name = "TRIPEND")
    private LocalDateTime tripEnd;

    @Column(name = "CREATED")
    private LocalDateTime created;

    @Column(name = "MODIFIED")
    private LocalDateTime modified;

    @Enumerated(EnumType.STRING)
    private TravelType travelType;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "trip_id")
    private Trip trip;
}
