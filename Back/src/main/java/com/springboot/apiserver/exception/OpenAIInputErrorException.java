package com.springboot.apiserver.exception;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public class OpenAIInputErrorException extends RuntimeException{
    private final String errorCode;
    private final HttpStatus status;


    public OpenAIInputErrorException(String message, String errorCode, HttpStatus status) {
        super(message);
        this.errorCode = errorCode;
        this.status = status;
    }

}
