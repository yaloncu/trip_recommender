package com.example.mybackend.controller;

import com.example.mybackend.model.Group;
import com.example.mybackend.model.JoinGroupWithPreferencesRequest;
import com.example.mybackend.model.User;
import com.example.mybackend.services.GroupService;
import com.example.mybackend.services.Neo4jUserService;

import jakarta.servlet.http.HttpServletRequest;

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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * GroupController is a REST controller that handles HTTP requests related to groups.
 * It provides endpoints to manage groups in the application.
 */
@RequestMapping("/api/groups")
public interface GroupController {

    @PostMapping("/create")
    ResponseEntity<Map<String, String>> createGroup(@RequestBody Group group);
    
    @PutMapping("/close/{name}")
    ResponseEntity<Map<String, String>> closeGroup(@PathVariable String name);

    @PostMapping("/closePrivate/{name}")
    ResponseEntity<Map<String, String>> closeGroupPrivate(@PathVariable String name);

    @PutMapping("/closeVoting/{name}")
    ResponseEntity<String> closeVoting(@PathVariable String groupName);

    @PostMapping("/joinWithPreferences")
    ResponseEntity<Map<String, String>> joinGroupWithPreferences(@RequestBody JoinGroupWithPreferencesRequest request);

    @PostMapping("/invite")
    ResponseEntity<Map<String, String>> inviteUserToGroup(@RequestBody Map<String, String> body);

    @GetMapping("/invitations/{email}")
    ResponseEntity<List<Group>> getInvitedGroups(@PathVariable String email);

    @PostMapping("/accept-invite-details")
    ResponseEntity<Map<String, String>> acceptInvitationWithDetails(@RequestBody Map<String, Object> body);

    @GetMapping("/public")
    ResponseEntity<List<Group>> getPublicGroups();

    @PostMapping("/user")
    ResponseEntity<List<Map<String, Object>>> getUserGroups(@RequestBody Map<String, String> body);

    @GetMapping("/{name}")
    ResponseEntity<Group> getGroupByName(@PathVariable String name);

    @DeleteMapping("/leave")
    ResponseEntity<Map<String, String>> leaveGroup(@RequestBody Map<String, String> body);

    @GetMapping("/triptype/{triptype}")
    ResponseEntity<List<Group>> getGroupsByTheme(@PathVariable String triptype);

    @GetMapping("/audience/{audience}")
    ResponseEntity<List<Group>> getGroupsByAudience(@PathVariable String audience);
}