package com.springboot.apiserver.ppurio.controller;

import com.springboot.apiserver.ppurio.ppuriodto.PpurioMMSRequestDto;
import com.springboot.apiserver.ppurio.ppuriodto.PpurioSMSRequestDto;
import com.springboot.apiserver.ppurio.service.PpurioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/ppurio/message")
public class PpurioController {
    private final PpurioService ppurioService;

    @Autowired
    public PpurioController(PpurioService ppurioService) {
        this.ppurioService = ppurioService;
    }

    @GetMapping("/test")
    public ResponseEntity<String> test() {
        String token = ppurioService.requestToken();

        return ResponseEntity.ok(token);
    }

//    @GetMapping("/send-test")
//    public ResponseEntity<String> sendTest() {
//        String response = null;
//        try {
//            response = ppurioService.sendMessage();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return ResponseEntity.ok(response);
//    }

    @PostMapping("/send-SMS")
    public ResponseEntity<String> sendSMS(@RequestBody PpurioSMSRequestDto ppurioSMSRequestDto){
        String response = null;
        try{
            System.out.println(ppurioSMSRequestDto.toString());
            response= ppurioService.sendMessage(ppurioSMSRequestDto);
        }catch (Exception e){
            e.printStackTrace();
        }
        return ResponseEntity.ok(response);
    }

    @PostMapping("/send-MMS")
    public ResponseEntity<String> sendMMS(@RequestBody PpurioMMSRequestDto ppurioMMSRequestDto){
        String response = null;
        try{
//            System.out.println(ppurioMMSRequestDto.toString());
            response = ppurioService.sendMessage(ppurioMMSRequestDto);
            System.out.println("MMS res : " + response);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ResponseEntity.ok(response);
    }


}
