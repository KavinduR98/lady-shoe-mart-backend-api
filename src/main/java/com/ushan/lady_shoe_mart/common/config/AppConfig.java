package com.ushan.lady_shoe_mart.common.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {

    /**
     * Registers ModelMapper as a Spring bean.
     * STRICT matching prevents accidental wrong field mappings —
     * source and destination field names must match exactly.
     * One shared instance across the whole app (singleton by default).
     */
    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration()
                .setMatchingStrategy(MatchingStrategies.STRICT);
        return modelMapper;
    }

    /**
     * Registers Gson as a Spring bean.
     * serializeNulls() ensures null fields are included in JSON output.
     * setPrettyPrinting() makes logs readable during development.
     */
    @Bean
    public Gson gson() {
        return new GsonBuilder()
                .serializeNulls()
                .setPrettyPrinting()
                .create();
    }

}
