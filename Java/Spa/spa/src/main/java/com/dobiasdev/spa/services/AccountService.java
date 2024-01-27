package com.dobiasdev.spa.services;

import com.dobiasdev.spa.persistance.entities.Account;
import com.dobiasdev.spa.model.dtos.AccountDto;

import java.util.List;

public interface AccountService {
    List<Account> getAccounts();

    Account getAccountById(Long id);

    long createAccount(AccountDto account);

    void deleteAccountById(Long id);

    long updateAccount(AccountDto account);

    void updateEntityFromDto(Account dbAccount, AccountDto dtoAccount);
}
