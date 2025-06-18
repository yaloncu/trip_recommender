package com.example.mybackend.services;

import java.util.List;

import com.example.mybackend.model.ActivityGroup;
import com.example.mybackend.model.ActivityGroupDTO;
import com.example.mybackend.model.SimpleUserDTO;
import com.example.mybackend.model.User;

public interface ActivityGroupService {
    ActivityGroup create(ActivityGroup group);
    List<ActivityGroupDTO> getAll();
    boolean joinActivity(Long id, String email);
    List<ActivityGroup> getActivitiesForUser(String email);
    List<SimpleUserDTO> getParticipants(Long activityGroupId);
}
