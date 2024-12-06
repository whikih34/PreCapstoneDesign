package com.springboot.apiserver.user.texts.textsdto;

import lombok.Getter;
import java.time.LocalDateTime;

@Getter
public class UserTextsResponseDto {
    private final int userTextsId;
    private final int userId;
    private final String mood;
    private final String keyword;
    private final String target;
    private final String product;
    private final String prompt;
    private final String gptMessage;
    private final LocalDateTime createdAt;
    public UserTextsResponseDto(int userTextsId, int userId, String mood, String keyword, String target, String product, String prompt, String gptMessage, LocalDateTime createdAt) {
        this.userTextsId = userTextsId;
        this.userId = userId;
        this.mood = mood;
        this.keyword = keyword;
        this.target = target;
        this.product = product;
        this.prompt = prompt;
        this.gptMessage = gptMessage;
        this.createdAt = createdAt;
    }
}
