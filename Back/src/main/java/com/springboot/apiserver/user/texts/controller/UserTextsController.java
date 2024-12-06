package com.springboot.apiserver.user.texts.controller;

import com.springboot.apiserver.user.texts.entity.UserTexts;
import com.springboot.apiserver.user.texts.entity.UserTextsRepository;
import com.springboot.apiserver.user.texts.textsdto.UserTextsResponseDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/user")
public class UserTextsController {
    private final UserTextsRepository userTextsRepository;
    @Autowired
    public UserTextsController(UserTextsRepository userTextsRepository){
        this.userTextsRepository=userTextsRepository;
    }
    @GetMapping("/get-texts")
    public List<UserTextsResponseDto> getUserTexts(@RequestParam("USER_ID")int userId){
        List<UserTexts> userTextsList = userTextsRepository.findByUserId(userId);
        List<UserTextsResponseDto> responseDtoList = new ArrayList<>();

        for(UserTexts userTexts : userTextsList){
            UserTextsResponseDto dto = new UserTextsResponseDto(userTexts.getUserTextsId(),userTexts.getUserId(),
                    userTexts.getMood(),userTexts.getKeyword(),userTexts.getTarget(),userTexts.getProduct(),
                    userTexts.getPrompt(),userTexts.getGptMessage(),userTexts.getCreatedAt());
            responseDtoList.add(dto);
        }
        return responseDtoList;
    }
}
