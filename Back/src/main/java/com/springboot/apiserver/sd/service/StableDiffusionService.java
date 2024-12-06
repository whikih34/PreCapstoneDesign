package com.springboot.apiserver.sd.service;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.springboot.apiserver.openai.service.OpenAIService;
import com.springboot.apiserver.sd.config.StableDiffusionItoIConfig;

import okhttp3.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import com.springboot.apiserver.util.LoggingUtil;
@Service
public class StableDiffusionService {
    private final StableDiffusionItoIConfig sdConfig;
    private final RestTemplate restTemplate;
    private final OpenAIService openAIService;
    private final OkHttpClient client = new OkHttpClient.Builder()
            .connectTimeout(30, TimeUnit.SECONDS)  // 연결 타임아웃
            .writeTimeout(30, TimeUnit.SECONDS)    // 쓰기 타임아웃
            .readTimeout(120, TimeUnit.SECONDS)    // 읽기 타임아웃(긴 작업을 대비)
            .build();
    private final ObjectMapper jacksonObjectMapper;

    public StableDiffusionService(@Qualifier("sdRestTemplate") RestTemplate restTemplate, StableDiffusionItoIConfig sdConfig, OpenAIService openAIService, ObjectMapper jacksonObjectMapper) {
        this.restTemplate = restTemplate;
        this.sdConfig = sdConfig;
        this.openAIService = openAIService;
        this.jacksonObjectMapper = jacksonObjectMapper;
    }

    public Response generateImage(String prompt, String initImage, boolean negativePrompt) {
        Response response = null;
        MediaType mediaType = MediaType.parse("application/json");
        LoggingUtil.logGeneratedNegativePrompt("URL : "+sdConfig.getSdApiURL());
        Map<String, Object> requestBodyMap = new HashMap<>();
        if (negativePrompt) {
            String generatedNegativePrompt = openAIService.generateNegativePrompt(prompt);
            requestBodyMap.put("negative_prompt", generatedNegativePrompt);
            LoggingUtil.logGeneratedNegativePrompt(generatedNegativePrompt);
        } else {
            requestBodyMap.put("negative_prompt", "bad quality");
        }
        requestBodyMap.put("key", sdConfig.getSdApiKey());
        requestBodyMap.put("model_id","Crystal Clear XL_V1");
        requestBodyMap.put("prompt", prompt);
        requestBodyMap.put("init_image", initImage);
        requestBodyMap.put("width", "512");
        requestBodyMap.put("height", "512");
        requestBodyMap.put("samples", "1");
        requestBodyMap.put("temp", false);
        requestBodyMap.put("safety_checker", false);
        requestBodyMap.put("strength", 0.73);
        requestBodyMap.put("seed", null);
        requestBodyMap.put("webhook", null);
        requestBodyMap.put("track_id", null);

        String requestBodyJson = null;
        try {
            requestBodyJson = jacksonObjectMapper.writeValueAsString(requestBodyMap);

            RequestBody body = RequestBody.create(mediaType, requestBodyJson);
            Request request = new Request.Builder()
                    .url(sdConfig.getSdApiURL())
                    .method("POST", body)
                    .addHeader("Content-Type", "application/json")
                    .build();
            response = client.newCall(request).execute();
//            System.out.println(response);
//            LoggingUtil.logResponseDetails(responseBody); // 로깅
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return response;
        }

}
