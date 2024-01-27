package com.dobiasdev.spa.serialization;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Component
public class JsonSerialization {

    private ObjectMapper objectMapper;

    public JsonSerialization() {
        this.objectMapper = new ObjectMapper();
        this.objectMapper.configure(SerializationFeature.WRITE_DATE_KEYS_AS_TIMESTAMPS, false);
        JavaTimeModule module = new JavaTimeModule();
        LocalDateTimeDeserializer deserializer = new LocalDateTimeDeserializer(DateTimeFormatter.ISO_DATE_TIME);
        LocalDateTimeSerializer serializer = new LocalDateTimeSerializer(DateTimeFormatter.ISO_DATE_TIME);
        module.addDeserializer(LocalDateTime.class, deserializer);
        module.addSerializer(LocalDateTime.class, serializer);
        this.objectMapper.registerModule(module);
        this.objectMapper.configure(DeserializationFeature.READ_UNKNOWN_ENUM_VALUES_AS_NULL, true);
    }

    public String serializeToJson(Object object) {
        try {
            return objectMapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error in Json serialization", e);
        }
    }

    public <T> T deserializeFromJson(String json, Class<T> classOfT) {
       try {
           return objectMapper.readValue(json, classOfT);
       } catch (JsonProcessingException e) {
           throw new RuntimeException("Error in Json serialization", e);
       }
    }

    public <T> T deserializeFromJson(String json, TypeReference<T> valueTypeRef) {
       try {
           return objectMapper.readValue(json, valueTypeRef);
       } catch (JsonProcessingException e) {
           throw new RuntimeException("Error in Json serialization", e);
       }
    }

    public <T> T deepClone(Object object, Class<T> classOfT) {
        return convertByJson(object, classOfT);
    }

    public <T> T convertByJson(Object objectToConvert, Class<T> classOfT) {
        return deserializeFromJson(serializeToJson(objectToConvert), classOfT);
    }

    public <T> T convertByJson(Object objectToConvert, TypeReference<T> classOfT) {
        return deserializeFromJson(serializeToJson(objectToConvert), classOfT);
    }
}
