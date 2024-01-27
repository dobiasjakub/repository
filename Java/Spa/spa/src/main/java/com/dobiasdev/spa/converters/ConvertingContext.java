package com.dobiasdev.spa.converters;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.HashMap;
import java.util.Map;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
@Getter
@Setter
public class ConvertingContext {
    private boolean system;
    private NavigationFields navigationFields;

    @Getter
    @Setter
    private final Map<String, Object> additionalData = new HashMap<>();

    public <T> T getAdditionalData(String key) {
        return (T) additionalData.get(key);
    }

    public <T> void setAdditionalData(String key, T value) {
        additionalData.put(key, value);
    }
}