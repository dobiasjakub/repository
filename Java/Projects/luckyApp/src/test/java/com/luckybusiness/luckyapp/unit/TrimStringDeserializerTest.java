package com.luckybusiness.luckyapp.unit;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.luckybusiness.luckyapp.deserialize.JacksonCustomizer;
import org.junit.jupiter.api.Test;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

import static org.junit.jupiter.api.Assertions.*;

class TrimStringDeserializerTest {

    static class SampleDto {
        private String name;
        private String blank;
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        public String getBlank() { return blank; }
        public void setBlank(String blank) { this.blank = blank; }
    }

    @Test
    void trimsAndConvertsBlankStringsToNull() throws Exception {
        JacksonCustomizer customizer = new JacksonCustomizer();
        Jackson2ObjectMapperBuilder builder = new Jackson2ObjectMapperBuilder();
        customizer.stringTrimCustomizer().customize(builder);
        ObjectMapper mapper = builder.build();

        String json = "{\"name\":\"  John  \",\"blank\":\"   \"}";
        SampleDto dto = mapper.readValue(json, SampleDto.class);

        assertEquals("John", dto.getName());
        assertNull(dto.getBlank());
    }
}
