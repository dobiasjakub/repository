package com.dobiasdev.spa.services.impl;

import com.dobiasdev.spa.persistance.entities.Account;
import com.dobiasdev.spa.model.dtos.AccountDto;
import com.dobiasdev.spa.repositories.UserRepository;
import com.dobiasdev.spa.services.AccountService;
import com.dobiasdev.spa.repositories.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class AccountServiceImpl implements AccountService {

    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private UserRepository userRepository;

    @Override
    public List<Account> getAccounts() {
        return accountRepository.findAll();
    }

    @Override
    public Account getAccountById(Long id) {
        return accountRepository.findById(id).orElseThrow();
    }

    @Override
    public long createAccount(AccountDto account) {
        var dbAccount = new Account();

        dbAccount.setCreated(LocalDateTime.now());

        updateEntityFromDto(dbAccount, account);
        accountRepository.save(dbAccount);

        return dbAccount.getId();
    }

    @Override
    public void deleteAccountById(Long id) {
        accountRepository.deleteById(id);
    }

    @Override
    public long updateAccount(AccountDto account) {
        var dbAccount = accountRepository.findById(account.getId()).orElseThrow();

        dbAccount.setModified(LocalDateTime.now());

        updateEntityFromDto(dbAccount, account);
        accountRepository.save(dbAccount);

        return dbAccount.getId();
    }


    @Override
    public void updateEntityFromDto(Account dbAccount, AccountDto dtoAccount) {

        dbAccount.setName(dtoAccount.getName());
        dbAccount.setAmount(dtoAccount.getAmount());
        dbAccount.setUser(userRepository.existsById(dtoAccount.getUser().getId()) ? userRepository.getById(dtoAccount.getUser().getId()) : null);

        accountRepository.save(dbAccount);
    }
}
