package com.dobiasdev.spa.controllers.api;

import com.dobiasdev.spa.converters.ConverterManager;
import com.dobiasdev.spa.converters.ConvertingContext;
import com.dobiasdev.spa.converters.NavigationFields;
import com.dobiasdev.spa.persistance.entities.Account;
import com.dobiasdev.spa.model.dtos.AccountDto;
import com.dobiasdev.spa.services.AccountService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("api/accounts")
public class AccountApiController {

    private final AccountService accountService;
    private final ConverterManager converterManager;

    protected AccountApiController(AccountService accountService, ConverterManager converterManager) {
        this.accountService = accountService;
        this.converterManager = converterManager;
    }

    @PostMapping("")
    public long createAccount(@RequestBody AccountDto account) {
        return accountService.createAccount(account);
    }

    @GetMapping("")
    @ResponseBody
    public List<AccountDto> getAccounts() {
        var accounts = accountService.getAccounts();
        var navigation = NavigationFields.create(new String[]{"user.id", "transactions"});

        return accounts.stream().map(a -> converterManager.account().convertToDto(a, navigation, new ConvertingContext())).collect(Collectors.toList());
    }

    @GetMapping("{id}")
    @ResponseBody
    public AccountDto getSingleAccount(@PathVariable(name = "id") Long id) {

        return converterManager.account().convertToDto(accountService.getAccountById(id), NavigationFields.create(new String[] {"user"}), new ConvertingContext());
    }

    @PostMapping("update")
    public long updateAccount(@RequestBody AccountDto account) {
        return accountService.updateAccount(account);
    }

    @PostMapping("delete/{id}")
    public void deleteAccount(@PathVariable Long id) {
        accountService.deleteAccountById(id);
    }

}
