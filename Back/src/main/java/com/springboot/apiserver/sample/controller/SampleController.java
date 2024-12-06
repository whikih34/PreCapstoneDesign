package com.springboot.apiserver.sample.controller;

import com.springboot.apiserver.sample.entity.SampleImageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/get-sample")
public class SampleController {
    @Autowired
    SampleImageRepository sampleImageRepository;

    @GetMapping
    public List<String> getSample() {
        return sampleImageRepository.findAllUrl();
    }
}
