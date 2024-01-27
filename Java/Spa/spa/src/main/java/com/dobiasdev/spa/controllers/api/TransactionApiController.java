package com.dobiasdev.spa.controllers.api;

import com.dobiasdev.spa.converters.ConverterManager;
import com.dobiasdev.spa.converters.ConvertingContext;
import com.dobiasdev.spa.converters.NavigationFields;
import com.dobiasdev.spa.model.dtos.TransactionDto;
import com.dobiasdev.spa.repositories.TransactionRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("api/transactions")
public class TransactionApiController {

    private final ConverterManager converterManager;
    private final TransactionRepository transactionRepository;

    public TransactionApiController(ConverterManager converterManager, TransactionRepository transactionRepository) {
        this.converterManager = converterManager;
        this.transactionRepository = transactionRepository;
    }

    @GetMapping("user/{id}")
    @ResponseBody
    public List<TransactionDto> getTransactionsByUser(@PathVariable(value = "id") Long id)
    {
        var transactions = transactionRepository.findAllByAccount_UserId(id);
        var navigation = NavigationFields.create("");

        return transactions.stream().map(t -> converterManager.transaction().convertToDto(t, navigation, new ConvertingContext())).collect(Collectors.toList());
    }


}
