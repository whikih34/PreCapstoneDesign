package com.springboot.apiserver.signup.service;

import com.springboot.apiserver.signup.entity.User;
import com.springboot.apiserver.signup.entity.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SignUpService {
    @Autowired
    private UserRepository userRepository;

    public void signUp(String id, String password){
        System.out.println(id+password);
        User user = new User();
        user.setId(id);
        user.setPassword(password);
        userRepository.save(user);
    }

    public int login(String id, String password) {
        User user = userRepository.findById(id);
        if(user==null) return -1;
        if(user.getPassword().equals(password)) return user.getUserId();
        return -1;
    }

}
