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
@RequestMapping("/api/groups")
public class GroupController {

    @Autowired
    private GroupService groupService;

    @PostMapping("/create")
    public ResponseEntity<String> createGroup(@RequestBody Group group) {
        try {
            groupService.createGroup(group.getName(), group.getUsers());
            return ResponseEntity.ok("Group created successfully: " + group.getName());
        } catch (Exception e) {
            e.printStackTrace(); 
            return ResponseEntity.badRequest().body("Error creating group: " + e.getMessage());
        }
    }

    @GetMapping("/groups/{name}")
    public ResponseEntity<Group> getGroupByName(@PathVariable String name) {
        Group group = groupService.getGroupByName(name);
        return group != null ? ResponseEntity.ok(group) : ResponseEntity.notFound().build();
    }
}