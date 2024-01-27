package com.dobiasdev.spa.model.dtos;

import com.dobiasdev.spa.persistance.enums.TravelType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TripSegmentDto {

    private Long id;

    private String startDestination;

    private String endDestination;

    private Long distance;

    private LocalDateTime tripStart;

    private LocalDateTime tripEnd;

    private LocalDateTime created;

    private LocalDateTime modified;

    private TravelType travelType;

    private TripDto trip;
}
