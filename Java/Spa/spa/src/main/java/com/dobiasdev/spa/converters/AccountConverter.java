package com.dobiasdev.spa.converters;

import com.dobiasdev.spa.model.dtos.TransactionDto;
import com.dobiasdev.spa.persistance.entities.Account;
import com.dobiasdev.spa.model.dtos.AccountDto;

import java.text.ParseException;

public class AccountConverter extends ConverterBase implements EntityConverter<Account, AccountDto> {

    public AccountConverter(ConverterManager converterManager) {
        super(converterManager );
    }


    @Override
    public AccountDto convertToDto(Account entity, NavigationFields navigation, ConvertingContext context) {
        if (entity == null) return null;

        var dto = new AccountDto();

        read("Account.id", f -> dto.setId(entity.getId()), context);
        read("Account.amount", f -> dto.setAmount(entity.getAmount()), context);
        read("Account.created", f -> dto.setCreated(entity.getCreated()), context);
        read("Account.id", f -> dto.setModified(entity.getModified()), context);

        navigation("Account.user", n -> dto.setUser(converterManager.user().convertToDto(entity.getUser(), n, context)), navigation, context);
        navigation("Account.transactions", n -> dto.setTransactions(entity.getTransactions().stream().map(t -> converterManager.transaction().convertToDto(t, n, context)).toArray(TransactionDto[]::new)), navigation, context);

        return dto;
    }

    @Override
    public Account convertToEntity(AccountDto dto) throws ParseException {
        return null;
    }
}
