package com.springboot.apiserver.signup.controller;

import com.springboot.apiserver.signup.service.SignUpService;
import com.springboot.apiserver.signup.signupdto.SignUpDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("signup")
public class SignUpController {
    @Autowired
    private SignUpService signUpService;

    @PostMapping("/")
    public ResponseEntity<String> signUp(@RequestBody SignUpDto signUpDto) {
        signUpService.signUp(signUpDto.getId(), signUpDto.getPassword());
        return ResponseEntity.ok("sign up success");
    }

    @PostMapping("/login")
    public ResponseEntity<Integer> login(@RequestBody SignUpDto signUpDto) {
        signUpService.login(signUpDto.getId(), signUpDto.getPassword());
        int success = signUpService.login(signUpDto.getId(), signUpDto.getPassword());
        System.out.println("login success"+signUpDto.getId());
        if(success>0){
            return ResponseEntity.ok(success);
        }
        else{
            return ResponseEntity.status(401).body(-1);
        }
    }
}
