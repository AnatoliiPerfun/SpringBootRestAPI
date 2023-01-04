package com.parser.api.mapper;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;



/**
 * @author tolik
 * @project xyz
 * @created 04.01.2023 - 11:48
 */

@Configuration
public class XyzMapper {
    @Bean
    XmlMapper xmlMapper() {
        return new XmlMapper();
    }
    @Bean
    ObjectMapper objectMapper() {
         return new ObjectMapper();
      }
}
