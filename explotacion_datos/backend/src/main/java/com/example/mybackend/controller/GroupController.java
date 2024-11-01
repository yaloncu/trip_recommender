package com.example.mybackend.controller;

import com.example.mybackend.model.Group;
import com.example.mybackend.model.JoinGroupWithPreferencesRequest;
import com.example.mybackend.model.User;
import com.example.mybackend.services.GroupService;
import com.example.mybackend.services.Neo4jUserService;
import com.example.mybackend.services.UserService;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.neo4j.driver.exceptions.Neo4jException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/groups")
public class GroupController {

    @Autowired
    private GroupService groupService;

    public GroupController(GroupService groupService) {
        this.groupService = groupService;
    }

    @PostMapping("/create")
    public ResponseEntity<Map<String, String>> createGroup(@RequestBody Group group) {
        try {
            groupService.createGroup(group.getName(), group.getEmail(), group.getAudience(), group.getPrivated(), group.isClosed(), group.getType(), group.getDepartureDate(), group.getReturnDate());
            Map<String, String> response = new HashMap<>();
            response.put("message", "Group created successfully");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            e.printStackTrace();
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "Failed to create group");
            errorResponse.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    @PutMapping("/close/{name}")
    public ResponseEntity<Map<String, String>> closeGroup(@PathVariable String name) {
        try {
            groupService.closeGroup(name);
            Map<String, String> response = new HashMap<>();
            response.put("message", "Group closed successfully");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            e.printStackTrace();
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "Failed to close group");
            errorResponse.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    @PutMapping("/closeVoting/{name}")
    public ResponseEntity<String> closeVoting(@PathVariable String groupName) {
        try {
            groupService.closeVoting(groupName); 
            return ResponseEntity.ok("Votación cerrada con éxito.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al cerrar la votación.");
        }
    }
    
    @PostMapping("/joinWithPreferences")
    public ResponseEntity<Map<String, String>> joinGroupWithPreferences(@RequestBody JoinGroupWithPreferencesRequest request) {
        try {
            groupService.joinGroupWithPreferences(request.getGroupName(), request.getEmail(), request.getpreference());
            Map<String, String> response = new HashMap<>();
            response.put("message", "User successfully joined the group with preference");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            e.printStackTrace();
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "Failed to join group with preference");
            errorResponse.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    @GetMapping("/public")
    public ResponseEntity<List<Group>> getPublicGroups() {
        try {
            List<Group> publicGroups = groupService.getPublicGroups();
            return ResponseEntity.ok(publicGroups);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }


    @PostMapping("/user")
    public ResponseEntity<List<Map<String, Object>>> getUserGroups(@RequestBody Map<String, String> body) {
        String email = body.get("email");
        if (email == null || email.isEmpty()) {
            throw new IllegalArgumentException("Email is required");
        }
        List<Map<String, Object>> groups = groupService.getUserGroups(email);
        return ResponseEntity.ok(groups);
    }



   
    @GetMapping("/{name}")
    public ResponseEntity<Group> getGroupByName(@PathVariable String name) {
        Group group = groupService.getGroupByName(name);
        return group != null ? ResponseEntity.ok(group) : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/leave")
    public ResponseEntity<Map<String, String>> leaveGroup(@RequestBody Map<String, String> body) {
        String email = body.get("email");
        String groupName = body.get("groupName");
        try {
            groupService.leaveGroup(email, groupName);
            Map<String, String> response = new HashMap<>();
            response.put("message", "Successfully left the group");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            e.printStackTrace();
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "Failed to leave the group");
            errorResponse.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

}