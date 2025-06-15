package com.example.mybackend.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.mybackend.model.ActivityGroup;

@RequestMapping("/api/activities")
public interface ActivityGroupController {
    @PostMapping
    ActivityGroup create(@RequestBody ActivityGroup group);

/*************  ✨ Windsurf Command ⭐  *************/
/**
 * Retrieve a list of all activity groups.
 *
 * @return a list of ActivityGroup objects representing all activity groups
 */

/*******  84030db0-f643-47dc-b377-57ffdd3f1936  *******/
    @GetMapping("/all")
    List<ActivityGroup> getAll();
}
