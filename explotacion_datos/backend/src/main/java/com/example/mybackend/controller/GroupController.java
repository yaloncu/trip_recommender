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
    Group createGroup(@RequestBody Group group);
    
    @PutMapping("/close/{name}")
    Group closeGroup(@PathVariable String name);

    @PostMapping("/closePrivate/{name}")
    Group closeGroupPrivate(@PathVariable String name);

    @PutMapping("/closeVoting/{name}")
    Group closeVoting(@PathVariable String groupName);

    @PostMapping("/joinWithPreferences")
    String joinGroupWithPreferences(@RequestBody JoinGroupWithPreferencesRequest request);

    @PostMapping("/invite")
    String inviteUserToGroup(@RequestBody Map<String, String> body);

    @GetMapping("/invitations/{email}")
    List<Group> getInvitedGroups(@PathVariable String email);

    @PostMapping("/accept-invite-details")
    String acceptInvitationWithDetails(@RequestBody Map<String, Object> body);

    @GetMapping("/public")
    List<Group> getPublicGroups();

    @PostMapping("/user")
    List<Group> getUserGroups(String email);

    @GetMapping("/{name}")
    Group getGroupByName(@PathVariable String name);

    @DeleteMapping("/leave")
    Group leaveGroup(@RequestBody Map<String, String> body);

    @GetMapping("/triptype/{triptype}")
    List<Group> getGroupsByTheme(@PathVariable String triptype);

    @GetMapping("/audience/{audience}")
    List<Group> getGroupsByAudience(@PathVariable String audience);
}