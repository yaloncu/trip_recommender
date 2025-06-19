package com.example.mybackend.services.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.mybackend.model.ActivityGroup;
import com.example.mybackend.model.ActivityGroupDTO;
import com.example.mybackend.model.SimpleUserDTO;
import com.example.mybackend.model.User;
import com.example.mybackend.repository.ActivityGroupRepository;
import com.example.mybackend.repository.UserRepository;
import com.example.mybackend.services.ActivityGroupService;

@Service
public class ActivityGroupServiceImpl implements ActivityGroupService {

    private final ActivityGroupRepository repository;
    private final UserRepository userRepository;

    @Autowired
    public ActivityGroupServiceImpl(ActivityGroupRepository repository, UserRepository userRepository) {
        this.repository = repository;
        this.userRepository = userRepository;
    }

    @Override
    public ActivityGroup create(ActivityGroup group) {
        String email = group.getAdminEmail();

        User admin = userRepository.findByEmail(email);
        if (admin == null) {
            throw new RuntimeException("No se encontró el usuario con email: " + email);
        }

        List<User> inscritos = new ArrayList<>();
        inscritos.add(admin);
        group.setInscritos(inscritos);

        return repository.save(group);
    }


    public List<ActivityGroupDTO> getAll() {
        List<ActivityGroup> groups = repository.findAll();

        List<ActivityGroupDTO> dtos = new ArrayList<>();
        for (ActivityGroup g : groups) {
            ActivityGroupDTO dto = new ActivityGroupDTO(
                g.getId(),
                g.getTitle(),
                g.getDescription(),
                g.getLocation(),
                g.getType(),
                g.getAdminEmail(),
                g.getStartDateTime(),
                g.getInscritos() != null ? g.getInscritos().size() : 0
            );
            dtos.add(dto);
        }

        return dtos;
    }

    @Override
    public List<ActivityGroup> getActivitiesForUser(String email) {
        return repository.findByInscritosEmail(email);
    }


    @Override
    public boolean joinActivity(Long id, String email) {
        User userOpt = userRepository.findByEmail(email);

        return repository.findById(id).map(group -> {
            if (group.getInscritos() == null) {
                group.setInscritos(new ArrayList<>());
            }
            if (!group.getInscritos().contains(userOpt)) {
                group.getInscritos().add(userOpt);
                repository.save(group);
                return true;
            } else {
                return false; 
            }
        }).orElse(false);
    }

    public List<SimpleUserDTO> getParticipants(Long activityGroupId) {
        return repository.findParticipantsByActivityId(activityGroupId);
    }

}

