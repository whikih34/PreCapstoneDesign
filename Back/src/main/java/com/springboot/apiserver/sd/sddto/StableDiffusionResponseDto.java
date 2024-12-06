package com.springboot.apiserver.sd.sddto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

//status, tip 등 이상한 필드가 너무 많아서 추가한 어노테이션
//@JsonIgnoerProperties 추가시 명시된 필드 외의 것은 무시함
//없으면 에러 ㅠ_ㅠ
@JsonIgnoreProperties(ignoreUnknown = true)
public class StableDiffusionResponseDto {
    private String imageUrl;
    private String base64EncodedUrl;
    private double time;
    private String uploadedImageUrl;
}
