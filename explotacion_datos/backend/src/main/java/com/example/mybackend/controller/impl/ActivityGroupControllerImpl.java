package com.example.mybackend.controller.impl;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.mybackend.model.ActivityGroup;
import com.example.mybackend.model.ActivityGroupDTO;
import com.example.mybackend.model.JoinActivityRequest;
import com.example.mybackend.model.SimpleUserDTO;
import com.example.mybackend.model.User;
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
    public List<ActivityGroupDTO> getAll() {
        return service.getAll();
    }

    @GetMapping("/user")
    public List<ActivityGroup> getByUserEmail(@RequestParam String email) {
        return service.getActivitiesForUser(email);
    }

    @PostMapping("/join/{id}")
    public ResponseEntity<String> joinActivity(@PathVariable Long id, @RequestBody JoinActivityRequest request) {
        boolean success = service.joinActivity(id, request.getEmail());
        if (success) {
            return ResponseEntity.ok("Inscripción exitosa");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("No se pudo completar la inscripción");
        }
    }

    @GetMapping("/participants/{id}")
    public ResponseEntity<List<SimpleUserDTO>> getParticipants(@PathVariable Long id) {
        List<SimpleUserDTO> participants = service.getParticipants(id);
        return ResponseEntity.ok(participants);
    }

}
