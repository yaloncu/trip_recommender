package com.example.mybackend.controller;

import com.example.mybackend.model.Group;
import com.example.mybackend.model.User;
import com.example.mybackend.services.GroupService;
import com.example.mybackend.services.UserService;

import jakarta.annotation.PreDestroy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;
    @Autowired
    private GroupService groupService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/create")
    public String createUser(@RequestBody User user) {
        userService.createUser(user.getName(), user.getEmail(), user.getPassword());
        return "User created successfully";
    }
    @PreDestroy
    public void close() {
        userService.close();
    }


    

    
}
