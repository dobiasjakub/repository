package com.dobiasdev.spa.model.dtos;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TripDto {

    private Long id;

    private LocalDateTime created;

    private LocalDateTime modified;

    private TripSegmentDto[] tripSegments;

    private UserDto user;
}
