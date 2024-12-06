package com.springboot.apiserver.sd.sddto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class StableDiffusionRequestDto {
    private int id;
    private String prompt;
    private String initImage;
    private boolean negativePrompt;
}
