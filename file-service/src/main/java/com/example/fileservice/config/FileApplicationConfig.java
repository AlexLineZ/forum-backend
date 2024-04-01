package com.example.fileservice.config;

import com.ibm.icu.text.Transliterator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FileApplicationConfig {

    private static final String CYRILLIC_TO_LATIN = "Russian-Latin/BGN";

    @Bean
    Transliterator cyrillicTransliterator() {
        return Transliterator.getInstance(CYRILLIC_TO_LATIN);
    }
}
