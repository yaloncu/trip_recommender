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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.mybackend.controller.GroupController;
import com.example.mybackend.model.Group;
import com.example.mybackend.model.JoinGroupWithPreferencesRequest;
import com.example.mybackend.model.SimpleUserDTO;
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
    public Group createGroup(@RequestBody Group group) {
        try {
             return groupService.createGroup(group.getName(), group.getEmail(), group.getAudience(), group.getPrivated(), group.isClosed(), group.getTripType(), group.getDepartureDate(), group.getReturnDate(), group.getAvailabilityStartDates(), group.getAvailabilityEndDates(), "", group.getFinalDestination());
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Get all groups.
     *
     * @return the list of groups
     */
    @PutMapping("/close/{name}")
    public Group closeGroup(@PathVariable String name) {
        try {
            return groupService.closeGroup(name);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Get a group by ID.
     *
     * @param id the ID of the group
     * @return the group
     */
    @PostMapping("/closePrivate/{name}")
    public Group closeGroupPrivate(@PathVariable String name) {
        try {
            logger.info("Closing group: {}", name);
            Group group = groupService.closeGroup(name);
            logger.info("Group closed successfully: {}", name);
            groupService.recommendDateUsingSlidingWindow(name);
            logger.info("Recommended dates calculated for group: {}", name);
            return group;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Update a group by ID.
     *
     * @param id the ID of the group
     * @param group the group details to update
     * @return the updated group
     */
    @PostMapping("/closeVoting/{groupName}")
    public Group closeVoting(@PathVariable String groupName) {
        try {
            return groupService.closeVoting(groupName); 
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
     /**
     * Delete a group by ID.
     *
     * @param id the ID of the group
     * @return the response status
     */
    @PostMapping("/joinWithPreferences")
    public String joinGroupWithPreferences(@RequestBody JoinGroupWithPreferencesRequest request) {
        try {
            return groupService.joinPublicGroupWithPreferences(request.getGroupName(), request.getEmail(), request.getpreference());
        } catch (Exception e) {
            e.printStackTrace();
            return "Error al unirse al grupo: " + e.getMessage();
        }
    }

     /**
     * Invite a user to a group.
     *
     * @param body the request body containing email and group name
     * @return the response status and message
     */
    @PostMapping("/invite")
    public String inviteUserToGroup(@RequestBody Map<String, String> body) {
        String email = body.get("email");
        String groupName = body.get("groupName");
        if (email == null || groupName == null) {
            return "not valid";
        }
        try {
            return groupService.inviteUserToGroup(email, groupName);
        } catch (Exception e) {
            logger.error("Error al invitar al usuario al grupo:", e);
            return "error";
        }
    }

    /**
     * Remove a user from a group.
     *
     * @param body the request body containing email and group name
     * @return the response status and message
     */
    @GetMapping("/invitations/{email}")
    public List<Group> getInvitedGroups(@PathVariable String email) {
        try {
            List<Group> invitedGroups = groupService.getInvitedGroups(email);
            return invitedGroups;
        } catch (Exception e) {
            logger.error("Error retrieving invited groups:", e);
            return null;
        }
    }

    /**
     * Accept an invitation to a private group with additional details.
     *
     * @param body the request body containing email, group name, preference, start dates, and end dates
     * @return the response status and message
     */
    @PostMapping("/accept-invite-details")
    public String acceptInvitationWithDetails(@RequestBody Map<String, Object> body) {
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

            return groupService.acceptInvitationWithDetails(email, groupName, preference, parsedStartDates, parsedEndDates);
        } catch (Exception e) {
            logger.error("Error al aceptar la invitación con detalles:", e);
            return "";
        }
    }

    /**
     * Get all public groups.
     *
     * @return the list of public groups
     */
    @GetMapping("/public")
    public List<Group> getPublicGroups() {
        try {
            List<Group> publicGroups = groupService.getPublicGroups();
            return publicGroups;
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Get groups for a specific user.
     *
     * @param body the request body containing the user's email
     * @return the list of groups the user belongs to
     */
    @GetMapping("/user")
    public List<Group> getUserGroups(@RequestParam String email) {
        if (email == null || email.isEmpty()) {
            throw new IllegalArgumentException("Email is required");
        }
        return groupService.getUserGroups(email);
    }

    /**
     * Get a group by name.
     *
     * @param name the name of the group
     * @return the group
     */
    @GetMapping("/{name}")
    public Group getGroupByName(@PathVariable String name) {
        Group group = groupService.getGroupByName(name);
        return group != null ? group : null;
    }

    /**
     * Leave a group.
     *
     * @param body the request body containing the user's email and group name
     * @return the response status and message
     */
    @DeleteMapping("/leave")
    public Group leaveGroup(@RequestBody Map<String, String> body) {
        String email = body.get("email");
        String groupName = body.get("groupName");
        try {
            return groupService.leaveGroup(email, groupName);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Get groups by theme.
     *
     * @param triptype the theme of the groups
     * @return the list of groups with the specified theme
     */
    @GetMapping("/triptype/{triptype}")
    public List<Group> getGroupsByTheme(@PathVariable String triptype) {
        try {
            List<Group> groups = groupService.getGroupsByTheme(triptype);
            if (groups.isEmpty()) {
                return null;
            }
            return groups;
        } catch (Exception e) {
            logger.error("Error retrieving groups by theme: {}", triptype, e);
            return null;
        }
    }

    /**
     * Get groups by audience.
     *
     * @param audience the audience of the groups
     * @return the list of groups with the specified audience
     */
    @GetMapping("/audience/{audience}")
    public List<Group> getGroupsByAudience(@PathVariable String audience) {
        try {
            List<Group> groups = groupService.getGroupsByAudience(audience);
            if (groups.isEmpty()) {
                return null;
            }
            return groups;
        } catch (Exception e) {
            logger.error("Error retrieving groups by audience: {}", audience, e);
            return null;
        }
    }

    @GetMapping("/participants/{groupId}")
    public ResponseEntity<List<SimpleUserDTO>> getParticipants(@PathVariable Long groupId) {
        List<SimpleUserDTO> participants = groupService.getParticipantsByGroupId(groupId);
        return ResponseEntity.ok(participants);
    }

}
