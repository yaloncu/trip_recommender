package com.example.mybackend.controller;

import com.example.mybackend.model.Group;
import com.example.mybackend.model.User;
import com.example.mybackend.services.GroupService;
import com.example.mybackend.services.UserService;
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

    @PostMapping("/create")
    public User createUser(@RequestBody User user) {
        return userService.createUser(user);
    }

    @GetMapping("/{email}")
    public ResponseEntity<User> getUserByEmail(@PathVariable String email) {
        User user = userService.getUserByEmail(email);
        return ResponseEntity.ok(user);
    }

    @PostMapping("/{email}/groups/{groupName}")
    public ResponseEntity<Void> addUserToGroup(@PathVariable String email, @PathVariable String groupName) {
        User user = userService.getUserByEmail(email);
        Group group = groupService.getGroupByName(groupName);
        if (user != null && group != null) {
            userService.addUserToGroup(user, group);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }
}