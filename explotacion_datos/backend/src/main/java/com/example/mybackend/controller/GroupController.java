package com.example.mybackend.controller;

import com.example.mybackend.model.Group;
import com.example.mybackend.services.GroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/groups")
public class GroupController {

    @Autowired
    private GroupService groupService;

    @PostMapping
    public ResponseEntity<Group> createGroup(@RequestBody Group group) {
        Group savedGroup = groupService.saveGroup(group);
        return ResponseEntity.ok(savedGroup);
    }

    @GetMapping("/{name}")
    public ResponseEntity<Group> getGroupByName(@PathVariable String name) {
        Group group = groupService.getGroupByName(name);
        return group != null ? ResponseEntity.ok(group) : ResponseEntity.notFound().build();
    }
}