package com.example.mybackend.controller;

import com.example.mybackend.model.Group;
import com.example.mybackend.model.User;
import com.example.mybackend.services.GroupService;
import com.example.mybackend.services.Neo4jUserService;
import com.example.mybackend.services.UserService;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class UserController {

    @Autowired
    private Neo4jUserService userService;
    @Autowired
    private GroupService groupService;

    @PostMapping("/signup")
    public ResponseEntity<String> createUser(@RequestBody User user) {
        try {
            userService.createUser(user.getName(), user.getEmail(), user.getPassword());
            return ResponseEntity.ok("User created successfully: " + user.getName());
        } catch (Exception e) {
            e.printStackTrace(); 
            return ResponseEntity.badRequest().body("Error creating user: " + e.getMessage());
        }
    }

    @RequestMapping(value = "/api/**", method = RequestMethod.OPTIONS)
    public ResponseEntity<?> handleOptionsRequest() {
        return ResponseEntity.ok().build();
    }

    @GetMapping("/email")
    public ResponseEntity<User> getUserByEmail(@PathVariable String email) {
        User user = userService.getUserByEmail(email);
        return user != null ? ResponseEntity.ok(user) : ResponseEntity.notFound().build();
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody User loginRequest) {
        String email = loginRequest.getEmail();
        String password = loginRequest.getPassword();
        boolean isValid = userService.validateUser(email, password);
        if (isValid) {
            return ResponseEntity.ok("Login successful");
        } else {
            return ResponseEntity.status(401).body("Invalid credentials");
        }
    }

/*    @PostMapping("/{email}/groups/{groupName}")
    public ResponseEntity<Void> addUserToGroup(@PathVariable String email, @PathVariable String groupName) {
        User user = userService.getUserByEmail(email);
        Group group = groupService.getGroupByName(groupName);
        if (user != null && group != null) {
            userService.addUserToGroup(user, group);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }*/
}