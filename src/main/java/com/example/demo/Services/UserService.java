package com.example.demo.Services;

import java.util.List;

import com.example.demo.Repository.UserRepository;
import com.example.demo.model.User;

public class UserService {
    private final UserRepository userRepository;  

    public UserService(UserRepository userRepository) {  
        this.userRepository = userRepository;  
    }  

    public List<User> getAllUser() {  
        return userRepository.findAll();  
    } 
     
}
