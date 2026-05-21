package com.luckybusiness.luckyapp.deserialize;

import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JacksonCustomizer {

    @Bean
    public Jackson2ObjectMapperBuilderCustomizer stringTrimCustomizer() {
        return builder -> builder.deserializerByType(String.class, new TrimStringDeserializer());
    }
}