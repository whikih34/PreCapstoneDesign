package com.springboot.apiserver.sd.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
@Getter
public class StableDiffusionItoIConfig {
    @Value("${sd.api.key}")
    private String sdApiKey;

    @Value("${sd.api.url}")
    private String sdApiURL;
    @Bean(name="sdRestTemplate")//OpenAI RestTemplate이랑 중복방지 이름설정
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

}
