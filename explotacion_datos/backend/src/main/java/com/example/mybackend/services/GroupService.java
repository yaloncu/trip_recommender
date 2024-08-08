package com.example.mybackend.services;

import com.example.mybackend.model.Group;
import com.example.mybackend.model.User;
import com.example.mybackend.repository.GroupRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GroupService {

    @Autowired
    private GroupRepository groupRepository;

    public Group saveGroup(Group group) {
        return groupRepository.save(group);
    }

    public Group getGroupByName(String name) {
        return groupRepository.findByName(name);
    }

    public void addUserToGroup(Group group, User user) {
        group.addUser(user);
        groupRepository.save(group);
    }
}