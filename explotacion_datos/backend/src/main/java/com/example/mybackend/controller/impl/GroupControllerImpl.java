package com.example.mybackend.controller.impl;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.mybackend.controller.GroupController;
import com.example.mybackend.model.Group;
import com.example.mybackend.model.JoinGroupWithPreferencesRequest;
import com.example.mybackend.services.GroupService;

@RestController
@RequestMapping("/api/groups")
public class GroupControllerImpl implements GroupController {
    private static final Logger logger = LoggerFactory.getLogger(GroupController.class);

    @Autowired
    private final GroupService groupService;

    public GroupControllerImpl(GroupService groupService) {
        this.groupService = groupService;
    }
    /**
     * Create a new group.
     *
     * @param group the group to create
     * @return the created group
     */
    @PostMapping("/create")
    public ResponseEntity<Map<String, String>> createGroup(@RequestBody Group group) {
        try {
            groupService.createGroup(group.getName(), group.getEmail(), group.getAudience(), group.getPrivated(), group.isClosed(), group.getType(), group.getDepartureDate(), group.getReturnDate(), group.getAvailabilityStartDate(), group.getAvailabilityEndDate(), "");
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

    /**
     * Get all groups.
     *
     * @return the list of groups
     */
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

    /**
     * Get a group by ID.
     *
     * @param id the ID of the group
     * @return the group
     */
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

    /**
     * Update a group by ID.
     *
     * @param id the ID of the group
     * @param group the group details to update
     * @return the updated group
     */
    @PutMapping("/closeVoting/{name}")
    public ResponseEntity<String> closeVoting(@PathVariable String groupName) {
        try {
            groupService.closeVoting(groupName); 
            return ResponseEntity.ok("Votación cerrada con éxito.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al cerrar la votación.");
        }
    }
    
     /**
     * Delete a group by ID.
     *
     * @param id the ID of the group
     * @return the response status
     */
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

     /**
     * Invite a user to a group.
     *
     * @param body the request body containing email and group name
     * @return the response status and message
     */
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

    /**
     * Remove a user from a group.
     *
     * @param body the request body containing email and group name
     * @return the response status and message
     */
    @GetMapping("/invitations/{email}")
    public ResponseEntity<List<Group>> getInvitedGroups(@PathVariable String email) {
        try {
            List<Group> invitedGroups = groupService.getInvitedGroups(email);
            return ResponseEntity.ok(invitedGroups);
        } catch (Exception e) {
            logger.error("Error retrieving invited groups:", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(null);
        }
    }

    /**
     * Accept an invitation to a private group with additional details.
     *
     * @param body the request body containing email, group name, preference, start dates, and end dates
     * @return the response status and message
     */
    @PostMapping("/accept-invite-details")
    public ResponseEntity<Map<String, String>> acceptInvitationWithDetails(@RequestBody Map<String, Object> body) {
        String email = (String) body.get("email");
        String groupName = (String) body.get("groupName");
        String preference = (String) body.get("preference");
        List<String> startDates = (List<String>) body.get("startDates");
        List<String> endDates = (List<String>) body.get("endDates");

        try {
            List<LocalDate> parsedStartDates = startDates.stream()
                .map(LocalDate::parse)
                .toList();

            List<LocalDate> parsedEndDates = endDates.stream()
                .map(LocalDate::parse)
                .toList();

            groupService.acceptInvitationWithDetails(email, groupName, preference, parsedStartDates, parsedEndDates);
            return ResponseEntity.ok(Map.of("message", "Invitación aceptada con detalles correctamente"));
        } catch (Exception e) {
            logger.error("Error al aceptar la invitación con detalles:", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Error al aceptar la invitación", "message", e.getMessage()));
        }
    }

    /**
     * Get all public groups.
     *
     * @return the list of public groups
     */
    @GetMapping("/public")
    public ResponseEntity<List<Group>> getPublicGroups() {
        try {
            List<Group> publicGroups = groupService.getPublicGroups();
            return ResponseEntity.ok(publicGroups);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    /**
     * Get groups for a specific user.
     *
     * @param body the request body containing the user's email
     * @return the list of groups the user belongs to
     */
    @PostMapping("/user")
    public ResponseEntity<List<Map<String, Object>>> getUserGroups(@RequestBody Map<String, String> body) {
        String email = body.get("email");
        if (email == null || email.isEmpty()) {
            throw new IllegalArgumentException("Email is required");
        }
        List<Map<String, Object>> groups = groupService.getUserGroups(email);
        return ResponseEntity.ok(groups);
    }

    /**
     * Get a group by name.
     *
     * @param name the name of the group
     * @return the group
     */
    @GetMapping("/{name}")
    public ResponseEntity<Group> getGroupByName(@PathVariable String name) {
        Group group = groupService.getGroupByName(name);
        return group != null ? ResponseEntity.ok(group) : ResponseEntity.notFound().build();
    }

    /**
     * Leave a group.
     *
     * @param body the request body containing the user's email and group name
     * @return the response status and message
     */
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

    /**
     * Get groups by theme.
     *
     * @param triptype the theme of the groups
     * @return the list of groups with the specified theme
     */
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

    /**
     * Get groups by audience.
     *
     * @param audience the audience of the groups
     * @return the list of groups with the specified audience
     */
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
