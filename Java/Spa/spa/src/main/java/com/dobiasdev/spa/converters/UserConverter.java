package com.dobiasdev.spa.converters;

import com.dobiasdev.spa.model.dtos.AccountDto;
import com.dobiasdev.spa.persistance.entities.User;
import com.dobiasdev.spa.model.dtos.UserDto;

import java.text.ParseException;


public class UserConverter extends ConverterBase implements EntityConverter<User, UserDto> {

    public UserConverter(ConverterManager converterManager) {
        super(converterManager);
    }

    @Override
    public UserDto convertToDto(User entity, NavigationFields navigation, ConvertingContext context) {
        if(entity == null) return null;

        var dto = new UserDto();

        read("User.id", f -> dto.setId(entity.getId()), context);
        read("User.username", f -> dto.setUsername(entity.getUsername()), context);
        read("User.email", f -> dto.setEmail(entity.getEmail()), context);
        read("User.created", f -> dto.setCreated(entity.getCreated()), context);
        read("User.modified", f -> dto.setModified(entity.getModified()), context);

        navigation("User.accounts", n -> dto.setAccounts(entity.getAccounts().stream().map(a -> converterManager.account().convertToDto(a, n, context)).toArray(AccountDto[]::new)), navigation, context);

        return dto;
    }

    @Override
    public User convertToEntity(UserDto dto) throws ParseException {

        return null;
    }
}
