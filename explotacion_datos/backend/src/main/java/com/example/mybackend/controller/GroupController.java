package com.example.mybackend.controller;

import com.example.mybackend.model.Group;
import com.example.mybackend.model.JoinGroupWithPreferencesRequest;
import com.example.mybackend.model.User;
import com.example.mybackend.services.GroupService;
import com.example.mybackend.services.Neo4jUserService;
import com.example.mybackend.services.UserService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
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
            groupService.createGroup(group.getName(), group.getAudience(), null); // Llamada sin esperar ID
            Map<String, String> response = new HashMap<>();
            response.put("message", "Group created successfully");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            e.printStackTrace();
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "Failed to create group");
            errorResponse.put("message", e.getMessage()); // Añadir el mensaje de la excepción para más contexto
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    
    @PostMapping("/joinWithPreferences")
    public ResponseEntity<Map<String, String>> joinGroupWithPreferences(@RequestBody JoinGroupWithPreferencesRequest request) {
        try {
            groupService.joinGroupWithPreferences(request.getGroupId(), request.getEmail(), request.getpreference());
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



   /*@PostMapping("/join")
    public ResponseEntity<Map<String, String>> joinGroup(@RequestBody JoinGroupRequest request) {
        try {
            if (request.getId() == null || request.getEmail() == null) {
                Map<String, String> errorResponse = new HashMap<>();
                errorResponse.put("error", "Invalid request");
                errorResponse.put("message", "Group ID and email must not be null");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
            }
            String userEmail = request.getEmail();
            Long groupId = request.getId();

            groupService.joinGroup(groupId, userEmail); 

            Map<String, String> response = new HashMap<>();
            response.put("message", "User successfully joined the group");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            e.printStackTrace(); // Logs detallados para depuración
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "Failed to join group");
            errorResponse.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }*/

    /*@PostMapping("/vote")
    public ResponseEntity<Map<String, String>> submitVotes(@RequestBody VoteRequest voteRequest) {
        try {
            groupService.submitVotes(voteRequest.getGroupId(), voteRequest.getEmail(), voteRequest.getVotes());
            Map<String, String> response = new HashMap<>();
            response.put("message", "Votes submitted successfully");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            e.printStackTrace();
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "Failed to submit votes");
            errorResponse.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }*/

    @GetMapping("/{name}")
    public ResponseEntity<Group> getGroupByName(@PathVariable String name) {
        Group group = groupService.getGroupByName(name);
        return group != null ? ResponseEntity.ok(group) : ResponseEntity.notFound().build();
    }

}