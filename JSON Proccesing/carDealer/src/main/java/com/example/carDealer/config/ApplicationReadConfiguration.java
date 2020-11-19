package com.example.carDealer.config;

import com.google.gson.*;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.lang.reflect.Type;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

@Configuration
public class ApplicationReadConfiguration {
    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }

    @Bean
    public Gson gson() {
        return new GsonBuilder()
                .excludeFieldsWithoutExposeAnnotation()
                .setPrettyPrinting()
                .setDateFormat("yyyy-MM-dd'T'hh:mm:ss")
//                .registerTypeAdapter(LocalDate.class, new JsonDeserializer<LocalDateTime>() {
//                    @Override
//                    public LocalDateTime deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
//                            throws JsonParseException {
//                        return LocalDateTime.parse(json.getAsString(),
//                                DateTimeFormatter.ofPattern("yyyy-MM-ddTHH:mm:ss").withLocale(Locale.ENGLISH));
//                    }
////                    public LocalDateTime deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
////                    throws JsonParseException {
////                        return LocalDateTime
////                                .parse(json.getAsString(),DateTimeFormatter.ofPattern("yyyy-MM-ddTHH:mm:ss")); }
//                })
                .create();
    }
}
