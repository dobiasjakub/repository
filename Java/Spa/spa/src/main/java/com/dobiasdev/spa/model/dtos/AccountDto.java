package com.dobiasdev.spa.model.dtos;

import com.fasterxml.jackson.annotation.JsonBackReference;
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
public class AccountDto {

    private Long id;

    private String name;

    private Long amount;

    private LocalDateTime created;

    private LocalDateTime modified;

    private TransactionDto[] transactions;

    private UserDto user;
}
