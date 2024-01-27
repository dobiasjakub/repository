package com.dobiasdev.spa.converters;

import java.text.ParseException;

public interface EntityConverter<E, D> {
    D convertToDto(E entity, NavigationFields navigation, ConvertingContext context);
    E convertToEntity(D dto) throws ParseException;


}
