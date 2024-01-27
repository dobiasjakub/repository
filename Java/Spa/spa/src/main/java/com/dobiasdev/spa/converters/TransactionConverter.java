package com.dobiasdev.spa.converters;

import com.dobiasdev.spa.persistance.entities.Transaction;
import com.dobiasdev.spa.model.dtos.TransactionDto;

import java.text.ParseException;


public class TransactionConverter extends ConverterBase implements EntityConverter<Transaction, TransactionDto> {

    public TransactionConverter (ConverterManager converterManager) {
        super(converterManager);
    }
    @Override
    public TransactionDto convertToDto(Transaction entity, NavigationFields navigation, ConvertingContext context) {
        if (entity==null) return null;

        var dto = new TransactionDto();

        read("Transaction.id", f-> dto.setId(entity.getId()), context);
        read("Transaction.name", f-> dto.setName(entity.getName()), context);
        read("Transaction.amount", f-> dto.setAmount(entity.getAmount()), context);
        read("Transaction.created", f-> dto.setCreated(entity.getCreated()), context);;
        read("Transaction.modified", f-> dto.setModified(entity.getModified()), context);
        read("Transaction.transaction", f-> dto.setTransactionType(entity.getTransactionType()), context);

        if (entity.getAccount() != null)
         navigation("Transaction.account", n->  dto.setAccount(converterManager.account().convertToDto(entity.getAccount(),n, context)), navigation, context);

        return dto;
    }

    @Override
    public Transaction convertToEntity(TransactionDto dto) throws ParseException {
        return null;
    }
}
