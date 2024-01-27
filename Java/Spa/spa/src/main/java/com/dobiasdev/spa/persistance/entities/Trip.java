package com.dobiasdev.spa.persistance.entities;


import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Trip {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "CREATED")
    private LocalDateTime created;

    @Column(name = "MODIFIED")
    private LocalDateTime modified;

    @OneToMany(mappedBy = "trip",
            fetch = FetchType.LAZY,
            cascade = CascadeType.ALL)
    private List<TripSegment> tripSegments;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    @JsonBackReference
    private User user;
}
