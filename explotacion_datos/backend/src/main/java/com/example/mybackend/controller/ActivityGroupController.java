package com.example.mybackend.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.mybackend.model.ActivityGroup;
import com.example.mybackend.model.ActivityGroupDTO;
import com.example.mybackend.model.SimpleUserDTO;
import com.example.mybackend.model.User;

@RequestMapping("/api/activities")
public interface ActivityGroupController {
    @PostMapping
    ActivityGroup create(@RequestBody ActivityGroup group);

    @GetMapping("/all")
    List<ActivityGroupDTO> getAll();
    
    @GetMapping("/user")
    public List<ActivityGroup> getByUserEmail(@RequestParam String email);

    @GetMapping("/participants/{id}")
    public ResponseEntity<List<SimpleUserDTO>> getParticipants(@PathVariable Long id);
}
