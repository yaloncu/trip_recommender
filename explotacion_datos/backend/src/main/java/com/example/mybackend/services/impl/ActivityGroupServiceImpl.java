package com.example.mybackend.services.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.mybackend.model.ActivityGroup;
import com.example.mybackend.repository.ActivityGroupRepository;
import com.example.mybackend.services.ActivityGroupService;

@Service
public class ActivityGroupServiceImpl implements ActivityGroupService {

    private final ActivityGroupRepository repository;

    @Autowired
    public ActivityGroupServiceImpl(ActivityGroupRepository repository) {
        this.repository = repository;
    }

    public ActivityGroup create(ActivityGroup group) {
        return repository.save(group);
    }

    public List<ActivityGroup> getAll() {
        return repository.findAll();
    }
}

