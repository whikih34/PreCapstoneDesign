package com.springboot.apiserver.openai.openaiapidto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class MessageDto {
    private String role;
    private String content;
}
