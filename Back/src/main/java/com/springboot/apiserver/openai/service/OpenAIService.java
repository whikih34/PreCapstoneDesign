package com.springboot.apiserver.openai.service;

import com.springboot.apiserver.openai.openaiapidto.CustomRequestDto;
import com.springboot.apiserver.openai.openaiapidto.MessageDto;
import com.springboot.apiserver.openai.openaiapidto.RequestSDDto;
import com.springboot.apiserver.openai.openaiapidto.ResponseDto;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
public class OpenAIService {
    private final RestTemplate restTemplate;
    private final String apiUrl;
    private final String model;

    public OpenAIService(@Value("${openai.api.url}") String apiUrl,
                            @Value("${openai.model}") String model,
                            RestTemplate restTemplate) {
        this.apiUrl = apiUrl;
        this.model = model;
        this.restTemplate = restTemplate;
    }

    public String generateNegativePrompt( String userPrompt) {
        RequestSDDto requestSDDto = new RequestSDDto();
        requestSDDto.negativePrompt(userPrompt);
        List<MessageDto> messages = requestSDDto.getMessages();
        CustomRequestDto customRequestDto = new CustomRequestDto(model, messages);
        ResponseDto chatGPTResponse = restTemplate.postForObject(apiUrl, customRequestDto, ResponseDto.class);
        return chatGPTResponse.getChoices().get(0).getMessage().getContent();
    }

}
