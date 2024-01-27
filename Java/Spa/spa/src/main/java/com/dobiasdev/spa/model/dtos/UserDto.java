package com.dobiasdev.spa.model.dtos;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserDto {

    private long id;

    private String username;

    private String email;

    private String password;

    private LocalDateTime created;

    private LocalDateTime modified;
    
    private TripDto[] trips;
}
