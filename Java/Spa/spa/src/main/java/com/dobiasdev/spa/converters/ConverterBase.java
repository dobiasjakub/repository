package com.dobiasdev.spa.converters;

import java.util.function.Consumer;

public class ConverterBase {
    protected final ConverterManager converterManager;

    public ConverterBase(ConverterManager converterManager) {
        this.converterManager = converterManager;
    }

    protected void read(String field, Consumer<String> updateField, ConvertingContext context)
    {
        updateField.accept(field);
    }

    protected void navigation(String field, Consumer<NavigationFields> navigateAction, NavigationFields navigation, ConvertingContext context)
    {
        if(navigation != null) {
            var plainFieldName = field.split("\\.")[1];

            read(field, s -> navigation.navigate(plainFieldName, navigateAction), context);
        }
    }
}
