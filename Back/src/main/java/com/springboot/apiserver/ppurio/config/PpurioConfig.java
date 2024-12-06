package com.springboot.apiserver.ppurio.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Getter
@Configuration
public class PpurioConfig {
    @Value("${ppurio.api.id}")
    private String ppurioid;

    @Value("${ppurio.api.key}")
    private String ppruioApikey;

    @Value("${ppurio.api.url}")
    private String ppurioUrl;

    @Value("${ppurio.api.from}")
    private String ppruioFrom;

    @Setter
    private String token;

}
