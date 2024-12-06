package com.springboot.apiserver.openai.openaiapidto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Data
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ExtractRequestDto {
    private List<MessageDto> messages = new ArrayList<>(); //required
    private String model; //required

    private String message;
    public void initializeMessages() {
        String formattedPrompt = String.format(
                "다음 문장에서 핵심 키워드와 주요 요소를 추출해줘. "
                        + "핵심 키워드는 명사 또는 짧은 구문으로 구성되며, "
                        + "광고의 주제를 나타내는 단어를 의미해. "
                        + "결과는 쉼표로 구분된 단어의 목록으로 제공해줘."
                        + "하지만 너무 길면 안돼. 가장 중요한 단어 딱 세가지만 골라줘."
                        + "추출의 대상이 되는 문장: \"%s\"",
                message
        );
        this.messages.add(new MessageDto("user", formattedPrompt));
    }
    public List<String> extractKeywordsFromResponse(String gptResponse) {
        if (gptResponse == null || gptResponse.isEmpty()) {
            return new ArrayList<>();
        }
        return Arrays.asList(gptResponse.split("\\s*,\\s*"));
    }


}
