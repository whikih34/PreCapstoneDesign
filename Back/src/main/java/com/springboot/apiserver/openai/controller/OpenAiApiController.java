package com.springboot.apiserver.openai.controller;

import com.springboot.apiserver.openai.openaiapidto.*;
import com.springboot.apiserver.openai.exception.OpenAIException;
import com.springboot.apiserver.user.texts.entity.UserTexts;
import com.springboot.apiserver.user.texts.entity.UserTextsRepository;
import com.springboot.apiserver.util.LoggingUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/GPT/api")
public class OpenAiApiController {

    @Value("${openai.model}")
    private String model;

    @Value("${openai.api.url}")
    private String apiUrl;

    private final OpenAIException openAIException;
    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private UserTextsRepository userTextsRepository;

    public OpenAiApiController(OpenAIException openAIException) {
        this.openAIException = openAIException;
    }

    @PostMapping("/create-message")
    public String createMessage(@RequestBody RequestAdvertiseDto requestAdvertiseDto) {
//        System.out.println(requestAdvertiseDto.getMessages().toString());
        openAIException.checkRequest(requestAdvertiseDto);
        requestAdvertiseDto.initializeMessages();

        List<MessageDto> messages = requestAdvertiseDto.getMessages();
        CustomRequestDto customRequestDto = new CustomRequestDto(model,messages);
        ResponseDto chatGPTResponse = restTemplate.postForObject(apiUrl, customRequestDto, ResponseDto.class);

        String gptCreatedMessage = chatGPTResponse.getChoices().get(0).getMessage().getContent();

        int userId = requestAdvertiseDto.getId();
        UserTexts userTexts = new UserTexts();
        userTexts.setUserId(userId);
        userTexts.setMood(requestAdvertiseDto.getMood());
        userTexts.setKeyword(requestAdvertiseDto.getKeyword());
        userTexts.setTarget(requestAdvertiseDto.getTarget());
        userTexts.setProduct(requestAdvertiseDto.getProduct());
        userTexts.setPrompt(requestAdvertiseDto.getPrompt());
        userTexts.setGptMessage(gptCreatedMessage);
        userTexts.setCreatedAt(LocalDateTime.now());
        userTextsRepository.save(userTexts);
        LoggingUtil.logGPTResponse(gptCreatedMessage);
        return gptCreatedMessage;
    }

    @PostMapping("/create-sd-prompt")
    public String createSdPrompt(@RequestBody RequestSDDto requestSDDto) {
        requestSDDto.initializeMessages();
        List<MessageDto> messages = requestSDDto.getMessages();
        CustomRequestDto customRequestDto = new CustomRequestDto(model,messages);
        ResponseDto chatGPTResponse = restTemplate.postForObject(apiUrl, customRequestDto, ResponseDto.class);
        return chatGPTResponse.getChoices().get(0).getMessage().getContent();
    }

    @PostMapping("/extract-keyword")
    public List<String> extarctKeyword(@RequestBody ExtractRequestDto extractRequestDto){
        openAIException.checkExtract(extractRequestDto);
        extractRequestDto.initializeMessages();
        List<MessageDto> messages = extractRequestDto.getMessages();
        CustomRequestDto customRequestDto = new CustomRequestDto(model,messages);
        ResponseDto chatGPTResponse = restTemplate.postForObject(apiUrl, customRequestDto, ResponseDto.class);
        String gptResponse = chatGPTResponse.getChoices().get(0).getMessage().getContent();
        return extractRequestDto.extractKeywordsFromResponse(gptResponse);
    }
}
