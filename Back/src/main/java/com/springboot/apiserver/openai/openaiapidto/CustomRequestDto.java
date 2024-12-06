package com.springboot.apiserver.openai.openaiapidto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@Data
@NoArgsConstructor
@AllArgsConstructor

public class CustomRequestDto {
    private String model;
    private List<MessageDto> messages;
}
