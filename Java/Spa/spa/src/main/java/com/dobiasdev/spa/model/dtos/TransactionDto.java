package com.dobiasdev.spa.model.dtos;

import com.dobiasdev.spa.persistance.enums.TransactionType;
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
public class TransactionDto {
    private long id;

    private String name;

    private Long amount;

    private TransactionType transactionType;

    private LocalDateTime created;

    private LocalDateTime modified;

    private AccountDto account;
}
