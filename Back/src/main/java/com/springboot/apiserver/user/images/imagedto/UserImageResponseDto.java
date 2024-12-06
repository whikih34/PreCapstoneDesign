package com.springboot.apiserver.user.images.imagedto;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class UserImageResponseDto {
    private final int userImageId;
    private final String userImage;
    private final String sampleImage;
    private final String prompt;
    private final LocalDateTime createdAt;

    public UserImageResponseDto(int userImageId, String userImage, String sampleImage, String prompt, LocalDateTime createdAt) {
        this.userImageId = userImageId;
        this.userImage = userImage;
        this.sampleImage = sampleImage;
        this.prompt = prompt;
        this.createdAt = createdAt;
    }

}
