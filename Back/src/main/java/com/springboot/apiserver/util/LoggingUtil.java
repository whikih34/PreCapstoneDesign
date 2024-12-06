package com.springboot.apiserver.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LoggingUtil {
    private static final Logger logger = LoggerFactory.getLogger(LoggingUtil.class);

    //Singleton
    private LoggingUtil() {}

    public static void logGeneratedNegativePrompt(String prompt) {
        logger.info("Generated Negative Prompt: {}", prompt);
    }

    public static void logResponseDetails(String responseBody) {
        logger.info("Response Body: {}", responseBody);
    }

    public static void logError(String message, Exception e) {
        logger.error(message, e);
    }
    public static void logGPTResponse(String message){
        logger.info("gpt message : {} ", message);
    }

    public static void logSDResponse(String message){
        logger.info("SD message : {} ", message);
    }
}
