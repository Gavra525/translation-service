package com.mt.api.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.client.RestClient;

@Configuration
@Profile("!test")
@EnableScheduling
public class SchedulingConfig {

    @Bean
    public RestClient restClient() {
        return RestClient.create();
    }
}
