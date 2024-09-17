package com.tiddev.cinema.service.modelDto;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Map;

@ConfigurationProperties(prefix = "cinema")
public record CinemaInfoDto(Map<String, Object> salon) {
}
