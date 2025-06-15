package com.example.mybackend.controller.impl;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.mybackend.model.ActivityGroup;
import com.example.mybackend.services.ActivityGroupService;

@RestController
@RequestMapping("/api/activities")
public class ActivityGroupControllerImpl {

    private final ActivityGroupService service;

    public ActivityGroupControllerImpl(ActivityGroupService service) {
        this.service = service;
    }

    @PostMapping
    public ActivityGroup create(@RequestBody ActivityGroup group) {
        return service.create(group);
    }

    @GetMapping("/all")
    public List<ActivityGroup> getAll() {
        return service.getAll();
    }
}
