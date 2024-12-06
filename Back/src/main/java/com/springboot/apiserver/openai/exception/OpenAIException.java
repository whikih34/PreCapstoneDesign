package com.springboot.apiserver.openai.exception;

import com.springboot.apiserver.exception.OpenAIInputErrorException;
import com.springboot.apiserver.openai.openaiapidto.ExtractRequestDto;
import com.springboot.apiserver.openai.openaiapidto.RequestAdvertiseDto;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

//메세지 유효성 검사 클래스
@NoArgsConstructor
@Service
public class OpenAIException {
    public void checkRequest(RequestAdvertiseDto requestAdvertiseDto) {
        if (requestAdvertiseDto.getPrompt() == null || requestAdvertiseDto.getPrompt().isEmpty()) {
            throw new OpenAIInputErrorException("prompt가 설정되지 않았습니다.", "EMPTY_PROMPT", HttpStatus.BAD_REQUEST);
        }
        if(requestAdvertiseDto.getTarget() == null || requestAdvertiseDto.getTarget().isEmpty()){
            throw new OpenAIInputErrorException("Target이 설정되지 않았습니다.", "EMPTY_TARGET",HttpStatus.BAD_REQUEST);
        }
        if (requestAdvertiseDto.getMood() == null) {
            throw new OpenAIInputErrorException("mood가 설정되지 않았습니다.", "EMPTY_MOOD",HttpStatus.BAD_REQUEST);
        }
        if(requestAdvertiseDto.getKeyword()==null || requestAdvertiseDto.getKeyword().isEmpty()){
            throw new OpenAIInputErrorException("Keyword가 설정되지 않았습니다.", "EMPTY_KEYWORD",HttpStatus.BAD_REQUEST);
        }
        if(requestAdvertiseDto.getProduct()==null || requestAdvertiseDto.getProduct().isEmpty()){
            throw new OpenAIInputErrorException("Product가 설정되지 않았습니다.","EMPTY_PRODUCT",HttpStatus.BAD_REQUEST);
        }
    }
    public void checkExtract(ExtractRequestDto extractRequestDto){
        if(extractRequestDto.getMessage().isEmpty() || extractRequestDto.getMessage()==null){
            throw new OpenAIInputErrorException("message가 비었습니다","EMPTY_MESSAGE",HttpStatus.BAD_REQUEST);
        }
    }
    public String generateMessage(RequestAdvertiseDto requestAdvertiseDto) {
        return "성공적으로 생성되었습니다.";
    }

}
