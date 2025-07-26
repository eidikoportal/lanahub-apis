package org.lanahub.lanahub.config;

import org.springframework.beans.factory.annotation.Value;

import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {

    @Value("${app.api.version}")
    private String apiVersion;

    public String getApiVersion() {
        return apiVersion;
    }
}
