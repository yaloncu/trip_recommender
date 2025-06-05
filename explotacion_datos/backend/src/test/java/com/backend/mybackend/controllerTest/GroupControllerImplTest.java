package com.backend.mybackend.controllerTest;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import org.assertj.core.condition.Join;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import com.example.mybackend.controller.impl.GroupControllerImpl;
import com.example.mybackend.model.Group;
import com.example.mybackend.model.JoinGroupWithPreferencesRequest;
import com.example.mybackend.services.GroupService;

class GroupControllerImplTest {
    @Mock
    private GroupService groupService;
    @InjectMocks
    private GroupControllerImpl groupController;

    private Group mockGroup;
    private Group mockGroupClosed;
    private JoinGroupWithPreferencesRequest mockJoinGroupWithPreferencesRequest;
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        mockGroup = Group.builder()
                .name("Test Group")
                .email("Test Email")
                .audience("Test Audience")
                .privated("public")
                .isClosed(false)
                .isClosedVoting(false)
                .type("Test Trip Type")
                .departureDate(LocalDate.of(2023, 10, 1))
                .returnDate(LocalDate.of(2023, 10, 10))
                .availabilityStartDates(List.of(LocalDate.of(2023, 9, 25)))
                .availabilityEndDates(List.of(LocalDate.of(2023, 9, 30)))
                .build();

        mockGroupClosed = Group.builder()
                .name("Test Group")
                .email("Test Email")
                .audience("Test Audience")
                .privated("public")
                .isClosed(true)
                .isClosedVoting(true)
                .type("Test Trip Type")
                .departureDate(LocalDate.of(2023, 10, 1))
                .returnDate(LocalDate.of(2023, 10, 10))
                .availabilityStartDates(List.of(LocalDate.of(2023, 9, 25)))
                .availabilityEndDates(List.of(LocalDate.of(2023, 9, 30)))
                .build();
        mockJoinGroupWithPreferencesRequest = JoinGroupWithPreferencesRequest.builder()
                .groupName("Test Group")
                .email("Test Email")
                .preference("Test Preference")
                .availabilityStartDates(List.of(LocalDate.of(2023, 9, 25)))
                .availabilityEndDates(List.of(LocalDate.of(2023, 9, 30)))
                .build();
    }

    @Test
    void testCreateGroup() {
        when(groupService.createGroup("Test Group", "Test Email", "Test Audience", "public", false,
            "Test Trip Type", LocalDate.of(2023, 10, 1), LocalDate.of(2023, 10, 10),
            List.of(LocalDate.of(2023, 9, 25)), List.of(LocalDate.of(2023, 9, 30)),
            "")).thenReturn(mockGroup);
        Group grupo = groupController.createGroup(mockGroup);
        assertNotNull(grupo);
        assertEquals("Test Group", grupo.getName());
        assertEquals("Test Email", grupo.getEmail());
        assertEquals("Test Audience", grupo.getAudience());
        assertEquals("public", grupo.getPrivated());
        assertFalse(grupo.isClosed());
        assertEquals("Test Trip Type", grupo.getType());
        assertEquals(LocalDate.of(2023, 10, 1), grupo.getDepartureDate());
        assertEquals(LocalDate.of(2023, 10, 10), grupo.getReturnDate());
        assertEquals(List.of(LocalDate.of(2023, 9, 25)), grupo.getAvailabilityStartDates());
        assertEquals(List.of(LocalDate.of(2023, 9, 30)), grupo.getAvailabilityEndDates());
        verify(groupService, times(1)).createGroup("Test Group", "Test Email", "Test Audience", "public", false,
            "Test Trip Type", LocalDate.of(2023, 10, 1), LocalDate.of(2023, 10, 10),
            List.of(LocalDate.of(2023, 9, 25)), List.of(LocalDate.of(2023, 9, 30)), "");
    }

    @Test
    void testCloseGroup() {
        String groupName = "Test Group";
        when(groupService.closeGroup(groupName)).thenReturn(mockGroupClosed);
        Group closedGroup = groupController.closeGroup(groupName);
        assertNotNull(closedGroup);
        assertEquals("Test Group", closedGroup.getName());
        assertTrue(closedGroup.isClosed());
        verify(groupService, times(1)).closeGroup(groupName);
    }

    @Test
    void testCloseGroupPrivate() {
        String groupName = "Test Group";
        when(groupService.closeGroup(groupName)).thenReturn(mockGroupClosed);
        Group closedGroup = groupController.closeGroupPrivate(groupName);
        assertNotNull(closedGroup);
        assertEquals("Test Group", closedGroup.getName());
        assertTrue(closedGroup.isClosed());
        verify(groupService, times(1)).closeGroup(groupName);
    }

    @Test 
    void testCloseVoting() {
        String groupName = "Test Group";
        when(groupService.closeVoting(groupName)).thenReturn(mockGroupClosed);
        Group closedGroup = groupController.closeVoting(groupName);
        assertNotNull(closedGroup);
        assertEquals("Test Group", closedGroup.getName());
        assertTrue(closedGroup.isClosedVoting());
        verify(groupService, times(1)).closeVoting(groupName);
    }

    @Test 
    void testJoinGroupWithPreferences() {
        String groupName = "Test Group";
        String userEmail = "Test Email";
        String preference = "Test Preference"; 
        List<LocalDate> availabilityStartDates = List.of(LocalDate.of(2023, 9, 25));
        List<LocalDate> availabilityEndDates = List.of(LocalDate.of(2023, 9, 30));

        when(groupService.joinPublicGroupWithPreferences(groupName, userEmail, preference))
            .thenReturn("Test preference");
        String result = groupController.joinGroupWithPreferences(mockJoinGroupWithPreferencesRequest);
        assertNotNull(result);
        assertEquals("Test preference", result);
        verify(groupService, times(1)).joinPublicGroupWithPreferences(groupName, userEmail, preference);
    }

    @Test
    void testInviteUserToGroup() {
        String groupName = "Test Group";
        String userEmail = "Test Email";
        Map<String, String> requestBody = Map.of(
            "email", userEmail,
            "groupName",  groupName            
        );
        when(groupService.inviteUserToGroup(userEmail, groupName)).thenReturn("User name");
        String userName = groupController.inviteUserToGroup(requestBody);
        assertEquals("User name", userName);
        verify(groupService, times(1)).inviteUserToGroup(userEmail, groupName);
    }

    @Test
    void testGetInvitedGroups() {
        String email = "Test Email";
        when(groupService.getInvitedGroups(email)).thenReturn(List.of(mockGroup));
        List<Group> invitedGroups = groupController.getInvitedGroups(email);
        assertNotNull(invitedGroups);
        assertEquals(1, invitedGroups.size());
        assertEquals("Test Group", invitedGroups.get(0).getName());
        verify(groupService, times(1)).getInvitedGroups(email);
    }

    @Test
    void testAcceptInvitationWithDetails() {
        String email = "test@example.com";
        String groupName = "Test Group";
        String preference = "Beach";
        List<String> startDates = List.of("2023-09-25");
        List<String> endDates = List.of("2023-09-30");

        Map<String, Object> requestBody = Map.of(
            "email", email,
            "groupName", groupName,
            "preference", preference,
            "startDates", startDates,
            "endDates", endDates
        );

        List<LocalDate> parsedStartDates = startDates.stream().map(LocalDate::parse).toList();
        List<LocalDate> parsedEndDates = endDates.stream().map(LocalDate::parse).toList();

        when(groupService.acceptInvitationWithDetails(email, groupName, preference, parsedStartDates, parsedEndDates))
            .thenReturn("Test Group");
        String result = groupController.acceptInvitationWithDetails(requestBody);
        assertNotNull(result);
        assertEquals("Test Group", result);
        verify(groupService, times(1)).acceptInvitationWithDetails(email, groupName, preference, parsedStartDates, parsedEndDates);
    }

    @Test
    void testGetPublicGroups() {
        when(groupService.getPublicGroups()).thenReturn(List.of(mockGroup));
        List<Group> publicGroups = groupController.getPublicGroups();
        assertNotNull(publicGroups);
        assertEquals(1, publicGroups.size());
        assertEquals("Test Group", publicGroups.get(0).getName());
        verify(groupService, times(1)).getPublicGroups();
    }

    @Test
    void testGetUserGroups() {
        String email = "Test Email";
        when(groupService.getUserGroups(email)).thenReturn(List.of(mockGroup));
        List<Group> userGroups = groupController.getUserGroups(email);
        assertNotNull(userGroups);
        assertEquals(1, userGroups.size());
        assertEquals("Test Group", userGroups.get(0).getName());
        verify(groupService, times(1)).getUserGroups(email);
    }

    @Test
    void testGetGroupByName() {
        String groupName = "Test Group";
        when(groupService.getGroupByName(groupName)).thenReturn(mockGroup);
        Group group = groupController.getGroupByName(groupName);
        assertNotNull(group);
        assertEquals("Test Group", group.getName());
        verify(groupService, times(1)).getGroupByName(groupName);
    }

    @Test
    void testLeaveGroup() {
        String groupName = "Test Group";
        String userEmail = "Test Email";
        Map<String, String> requestBody = Map.of(
            "email", userEmail,
            "groupName",  groupName            
        );
        when(groupService.leaveGroup(userEmail, groupName)).thenReturn(mockGroup);
        Group group = groupController.leaveGroup(requestBody);
        assertNotNull(group);
        assertEquals("Test Group", group.getName());
        verify(groupService, times(1)).leaveGroup(userEmail, groupName);
    }

    @Test
    void testGetGroupsByTheme() {
        String tripType = "Test Trip Type";
        when(groupService.getGroupsByTheme(tripType)).thenReturn(List.of(mockGroup));
        List<Group> groups = groupController.getGroupsByTheme(tripType);
        assertNotNull(groups);
        assertEquals(1, groups.size());
        assertEquals("Test Group", groups.get(0).getName());
        verify(groupService, times(1)).getGroupsByTheme(tripType);
    }

    @Test
    void testGetGroupsByAudience() {
        String audience = "Test Audience";
        when(groupService.getGroupsByAudience(audience)).thenReturn(List.of(mockGroup));
        List<Group> groups = groupController.getGroupsByAudience(audience);
        assertNotNull(groups);
        assertEquals(1, groups.size());
        assertEquals("Test Group", groups.get(0).getName());
        verify(groupService, times(1)).getGroupsByAudience(audience);
    }

}
