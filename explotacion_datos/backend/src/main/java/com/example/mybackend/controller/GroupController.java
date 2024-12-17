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

@RestController
@RequestMapping("/api/groups")
public class GroupController {

    private static final Logger logger = LoggerFactory.getLogger(GroupController.class);

    @Autowired
    private GroupService groupService;

    public GroupController(GroupService groupService) {
        this.groupService = groupService;
    }

    @PostMapping("/create")
    public ResponseEntity<Map<String, String>> createGroup(@RequestBody Group group) {
        try {
            groupService.createGroup(group.getName(), group.getEmail(), group.getAudience(), group.getPrivated(), group.isClosed(), group.getType(), group.getDepartureDate(), group.getReturnDate(), group.getAvailabilityStartDate(), group.getAvailabilityEndDate());
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

    @PostMapping("/closePrivate/{name}")
    public ResponseEntity<Map<String, String>> closeGroupPrivate(@PathVariable String name) {
        try {
            logger.info("Closing group: {}", name);
            groupService.closeGroup(name);
            logger.info("Group closed successfully: {}", name);
            groupService.recommendDateUsingSlidingWindow(name);
            logger.info("Recommended dates calculated for group: {}", name);
            Map<String, String> response = new HashMap<>();
            response.put("message", "Group closed successfully and dates recommended");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            e.printStackTrace();
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "Failed to close group and recommend dates");
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
            groupService.joinPublicGroupWithPreferences(request.getGroupName(), request.getEmail(), request.getpreference());
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

    @PostMapping("/invite")
    public ResponseEntity<Map<String, String>> inviteUserToGroup(@RequestBody Map<String, String> body) {
        String email = body.get("email");
        String groupName = body.get("groupName");
        if (email == null || groupName == null) {
            return ResponseEntity.badRequest().body(Map.of("error", "Email y nombre del grupo son obligatorios"));
        }
        try {
            groupService.inviteUserToGroup(email, groupName);
            return ResponseEntity.ok(Map.of("message", "Usuario invitado exitosamente"));
        } catch (Exception e) {
            logger.error("Error al invitar al usuario al grupo:", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Error al invitar al usuario", "message", e.getMessage()));
        }
    }

    @GetMapping("/invitations/{email}")
    public ResponseEntity<List<Map<String, Object>>> getInvitedGroups(@PathVariable String email) {
        try {
            List<Map<String, Object>> invitedGroups = groupService.getInvitedGroups(email);
            return ResponseEntity.ok(invitedGroups);
        } catch (Exception e) {
            logger.error("Error retrieving invited groups:", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(null);
        }
    }

    @PostMapping("/accept-invite")
    public ResponseEntity<Map<String, String>> acceptInvitation(@RequestBody Map<String, String> body) {
        String email = body.get("email");
        String groupName = body.get("groupName");

        if (email == null || groupName == null) {
            return ResponseEntity.badRequest().body(Map.of("error", "Email y nombre del grupo son obligatorios"));
        }

        try {
            groupService.acceptInvitation(email, groupName);
            return ResponseEntity.ok(Map.of("message", "Invitación aceptada correctamente"));
        } catch (Exception e) {
            logger.error("Error al aceptar la invitación:", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Error al aceptar la invitación", "message", e.getMessage()));
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

    @GetMapping("/triptype/{triptype}")
    public ResponseEntity<List<Group>> getGroupsByTheme(@PathVariable String triptype) {
        try {
            List<Group> groups = groupService.getGroupsByTheme(triptype);
            if (groups.isEmpty()) {
                return ResponseEntity.noContent().build();
            }
            return ResponseEntity.ok(groups);
        } catch (Exception e) {
            logger.error("Error retrieving groups by theme: {}", triptype, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping("/audience/{audience}")
    public ResponseEntity<List<Group>> getGroupsByAudience(@PathVariable String audience) {
        try {
            List<Group> groups = groupService.getGroupsByAudience(audience);
            if (groups.isEmpty()) {
                return ResponseEntity.noContent().build();
            }
            return ResponseEntity.ok(groups);
        } catch (Exception e) {
            logger.error("Error retrieving groups by audience: {}", audience, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

}