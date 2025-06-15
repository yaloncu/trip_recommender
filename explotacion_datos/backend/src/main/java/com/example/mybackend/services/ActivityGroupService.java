package com.example.mybackend.services;

import java.util.List;

import com.example.mybackend.model.ActivityGroup;

public interface ActivityGroupService {
    ActivityGroup create(ActivityGroup group);
    List<ActivityGroup> getAll();
}
