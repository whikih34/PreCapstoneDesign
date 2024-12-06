package com.springboot.apiserver.openai.openaiapidto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@Getter
@NoArgsConstructor
@AllArgsConstructor

public class RequestAdvertiseDto {
    private List<MessageDto> messages = new ArrayList<>(); //required
    private String model; //required

//    여기 아래부터는 open ai 서버에 전달되면 500 Err
    private int id;
    private String mood; //사용자 설정 분위기
    private String target; //사용자 설정 타겟 층 연령대
    private String product;//사용자가 판매할 제품
    private String keyword; //사용자 설정 메인 키워드
    private String prompt;//사용자 설정 문장
    // OpenAI API에 전달할 프롬프트 생성자
    public void initializeMessages() {
        String formattedPrompt = String.format(
                "<%s> 와 같은 분위기로 광고 문구를 만들어줘. 광고를 듣게 될 사람은 <%s>야. 광고하는 물건은 <%s>야. " +
                        "광고 카피에 포함되어야 할 메인 키워드는 <%s>야. " +
                        "사용자 프롬프트 : <%s>. " +
                        "주의사항 1 : 절대 이모지(예: 😊, 👍)나 특수 문자를 포함하지 말 것."+
                        "주의사항 2 : 광고 문구 외에 다른 부연 설명은 절대 포함하지 말 것."
                ,
                mood,
                target,
                product,
                keyword,
                prompt
        );
        this.messages.add(new MessageDto("user", formattedPrompt));

    }

    }
