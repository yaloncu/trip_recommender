package com.backend.mybackend.controllerTest;

import com.example.mybackend.controller.impl.GroupControllerImpl;
import com.example.mybackend.model.Group;
import com.example.mybackend.model.JoinGroupWithPreferencesRequest;
import com.example.mybackend.model.SimpleUserDTO;
import com.example.mybackend.services.GroupService;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GroupControllerImplTest {

    @Mock
    private GroupService groupService;

    @InjectMocks
    private GroupControllerImpl controller;

    @Test
    void testCreateGroup() {
        Group group = new Group();
        group.setName("TestGroup");
        group.setEmail("test@example.com");
        group.setAudience("Todos");
        group.setPrivated("false");
        group.setClosed(false);
        group.setTripType("Cultural");
        group.setDepartureDate(LocalDate.of(2025, 7, 1));
        group.setReturnDate(LocalDate.of(2025, 7, 10));
        group.setAvailabilityStartDates(List.of(LocalDate.of(2025, 6, 25)));
        group.setAvailabilityEndDates(List.of(LocalDate.of(2025, 6, 30)));
        group.setFinalDestination("Roma"); // <-- AÑADIDO: debe existir si el servicio lo usa

        when(groupService.createGroup(
            eq("TestGroup"),
            eq("test@example.com"),
            eq("Todos"),
            eq("false"),
            eq(false),
            eq("Cultural"),
            eq(LocalDate.of(2025, 7, 1)),
            eq(LocalDate.of(2025, 7, 10)),
            eq(List.of(LocalDate.of(2025, 6, 25))),
            eq(List.of(LocalDate.of(2025, 6, 30))),
            eq(""),
            eq("Roma")
        )).thenReturn(group);

        Group result = controller.createGroup(group);

        assertNotNull(result);
        assertEquals("TestGroup", result.getName());
        assertEquals("Roma", result.getFinalDestination()); // <-- validación opcional
    }

    @Test
    void testJoinGroupWithPreferences() {
        JoinGroupWithPreferencesRequest request = new JoinGroupWithPreferencesRequest();
        request.setGroupName("TestGroup");
        request.setEmail("user@example.com");
        request.setpreference("Cultural");

        when(groupService.joinPublicGroupWithPreferences("TestGroup", "user@example.com", "Cultural"))
            .thenReturn("joined");

        String result = controller.joinGroupWithPreferences(request);
        assertEquals("joined", result);
    }

    @Test
    void testInviteUserToGroup() {
        Map<String, String> body = Map.of("email", "user@example.com", "groupName", "TestGroup");

        when(groupService.inviteUserToGroup("user@example.com", "TestGroup")).thenReturn("invited");

        String result = controller.inviteUserToGroup(body);
        assertEquals("invited", result);
    }

    @Test
    void testGetPublicGroups() {
        Group g1 = new Group(); g1.setName("G1");
        Group g2 = new Group(); g2.setName("G2");

        when(groupService.getPublicGroups()).thenReturn(List.of(g1, g2));

        List<Group> result = controller.getPublicGroups();
        assertEquals(2, result.size());
        assertEquals("G1", result.get(0).getName());
    }

    @Test
    void testGetParticipants() {
        SimpleUserDTO user1 = new SimpleUserDTO("Alice", "alice@example.com", "Hola", "F", 30);
        SimpleUserDTO user2 = new SimpleUserDTO("Bob", "bob@example.com", "Hi", "M", 25);

        when(groupService.getParticipantsByGroupId(1L)).thenReturn(List.of(user1, user2));

        var response = controller.getParticipants(1L);
        assertEquals(2, response.getBody().size());
        assertEquals("Alice", response.getBody().get(0).name());
    }

    @Test
    void testCloseGroup() {
        Group expectedGroup = new Group();
        expectedGroup.setName("Cerrado");

        when(groupService.closeGroup("Cerrado")).thenReturn(expectedGroup);

        Group result = controller.closeGroup("Cerrado");

        assertNotNull(result);
        assertEquals("Cerrado", result.getName());
    }

    @Test
    void testCloseGroupPrivate() {
        Group group = new Group();
        group.setName("Privado");

        when(groupService.closeGroup("Privado")).thenReturn(group);
        doNothing().when(groupService).recommendDateUsingSlidingWindow("Privado");

        Group result = controller.closeGroupPrivate("Privado");

        assertNotNull(result);
        assertEquals("Privado", result.getName());
        verify(groupService).recommendDateUsingSlidingWindow("Privado");
    }

    @Test
    void testCloseVoting() {
        Group group = new Group();
        group.setName("Votación");

        when(groupService.closeVoting("Votación")).thenReturn(group);

        Group result = controller.closeVoting("Votación");

        assertNotNull(result);
        assertEquals("Votación", result.getName());
    }

    @Test
    void testGetInvitedGroups() {
        Group group = new Group();
        group.setName("Invitado");

        when(groupService.getInvitedGroups("user@example.com")).thenReturn(List.of(group));

        List<Group> result = controller.getInvitedGroups("user@example.com");

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Invitado", result.get(0).getName());
    }

    @Test
    void testAcceptInvitationWithDetails() {
        Map<String, Object> body = Map.of(
            "email", "user@example.com",
            "groupName", "TestGroup",
            "preference", "Playa",
            "startDates", List.of("2025-08-01"),
            "endDates", List.of("2025-08-05")
        );

        when(groupService.acceptInvitationWithDetails(
            eq("user@example.com"),
            eq("TestGroup"),
            eq("Playa"),
            anyList(),
            anyList()
        )).thenReturn("accepted");

        String result = controller.acceptInvitationWithDetails(body);

        assertEquals("accepted", result);
    }

    @Test
    void testGetUserGroups() {
        Group group = new Group();
        group.setName("Mi Grupo");

        when(groupService.getUserGroups("user@example.com")).thenReturn(List.of(group));

        List<Group> result = controller.getUserGroups("user@example.com");

        assertNotNull(result);
        assertEquals("Mi Grupo", result.get(0).getName());
    }

    @Test
    void testGetGroupByName() {
        Group group = new Group();
        group.setName("Viaje");

        when(groupService.getGroupByName("Viaje")).thenReturn(group);

        Group result = controller.getGroupByName("Viaje");

        assertNotNull(result);
        assertEquals("Viaje", result.getName());
    }

    @Test
    void testLeaveGroup() {
        Map<String, String> body = Map.of("email", "user@example.com", "groupName", "SalirGrupo");

        Group group = new Group();
        group.setName("SalirGrupo");

        when(groupService.leaveGroup("user@example.com", "SalirGrupo")).thenReturn(group);

        Group result = controller.leaveGroup(body);

        assertNotNull(result);
        assertEquals("SalirGrupo", result.getName());
    }

    @Test
    void testGetGroupsByTheme() {
        Group group = new Group();
        group.setTripType("Aventura");

        when(groupService.getGroupsByTheme("Aventura")).thenReturn(List.of(group));

        List<Group> result = controller.getGroupsByTheme("Aventura");

        assertNotNull(result);
        assertEquals("Aventura", result.get(0).getTripType());
    }

    @Test
    void testGetGroupsByAudience() {
        Group group = new Group();
        group.setAudience("Familias");

        when(groupService.getGroupsByAudience("Familias")).thenReturn(List.of(group));

        List<Group> result = controller.getGroupsByAudience("Familias");

        assertNotNull(result);
        assertEquals("Familias", result.get(0).getAudience());
    }

    @Test
    void testCreateGroup_failure() {
        Group group = new Group();
        group.setName("TestError");

        when(groupService.createGroup(
            any(), any(), any(), any(), anyBoolean(),
            any(), any(), any(), anyList(), anyList(),
            any(), any()
        ))
            .thenThrow(new RuntimeException("DB error"));

        Group result = controller.createGroup(group);
        assertNull(result);
    }

    @Test
    void testJoinGroupWithPreferences_failure() {
        JoinGroupWithPreferencesRequest request = new JoinGroupWithPreferencesRequest();
        request.setGroupName("FailGroup");
        request.setEmail("fail@example.com");
        request.setpreference("Montaña");

        when(groupService.joinPublicGroupWithPreferences(any(), any(), any()))
            .thenThrow(new RuntimeException("Join error"));

        String result = controller.joinGroupWithPreferences(request);
        assertTrue(result.contains("Error al unirse"));
    }

    @Test
    void testInviteUserToGroup_invalidInput() {
        Map<String, String> body = Map.of("email", "user@example.com");

        String result = controller.inviteUserToGroup(body);
        assertEquals("not valid", result);
    }

    @Test
    void testInviteUserToGroup_exception_2() {
        Map<String, String> body = Map.of("email", "user@example.com", "groupName", "Test");

        when(groupService.inviteUserToGroup(any(), any()))
            .thenThrow(new RuntimeException("Service down"));

        String result = controller.inviteUserToGroup(body);
        assertEquals("error", result);
    }

    @Test
    void testAcceptInvitationWithDetails_invalidDates() {
        Map<String, Object> body = Map.of(
            "email", "user@example.com",
            "groupName", "TestGroup",
            "preference", "Playa",
            "startDates", List.of("fecha-mal"),
            "endDates", List.of("2025-08-05")
        );

        String result = controller.acceptInvitationWithDetails(body);
        assertEquals("", result);
    }

    @Test
    void testGetPublicGroups_exception_2() {
        when(groupService.getPublicGroups()).thenThrow(new RuntimeException("DB error"));

        List<Group> result = controller.getPublicGroups();
        assertNull(result);
    }

    @Test
    void testGetUserGroups_invalidEmail_2() {
        assertThrows(IllegalArgumentException.class, () -> controller.getUserGroups(""));
    }

    @Test
    void testGetGroupsByTheme_emptyList_2() {
        when(groupService.getGroupsByTheme("Vacío")).thenReturn(List.of());

        List<Group> result = controller.getGroupsByTheme("Vacío");
        assertNull(result);
    }

    @Test
    void testGetGroupsByAudience_emptyList_2() {
        when(groupService.getGroupsByAudience("Nadie")).thenReturn(List.of());

        List<Group> result = controller.getGroupsByAudience("Nadie");
        assertNull(result);
    }

    @Test
    void testCreateGroup_exception() {
        when(groupService.createGroup(any(), any(), any(), any(), anyBoolean(), any(), any(), any(), any(), any(), anyString(), any()))
            .thenThrow(new RuntimeException("DB error"));
        Group group = new Group();
        Group result = controller.createGroup(group);
        assertNull(result);
    }

    @Test
    void testCloseGroup_exception() {
        when(groupService.closeGroup(anyString())).thenThrow(new RuntimeException("Error"));
        Group result = controller.closeGroup("ErrorGroup");
        assertNull(result);
    }

    @Test
    void testCloseGroupPrivate_exception() {
        when(groupService.closeGroup(anyString())).thenThrow(new RuntimeException("Error"));
        Group result = controller.closeGroupPrivate("ErrorGroup");
        assertNull(result);
    }

    @Test
    void testCloseVoting_exception() {
        when(groupService.closeVoting(anyString())).thenThrow(new RuntimeException("Error"));
        Group result = controller.closeVoting("Group");
        assertNull(result);
    }

    @Test
    void testJoinGroupWithPreferences_exception() {
        JoinGroupWithPreferencesRequest request = new JoinGroupWithPreferencesRequest();
        request.setGroupName("G");
        request.setEmail("email");
        request.setpreference("P");
        when(groupService.joinPublicGroupWithPreferences(any(), any(), any())).thenThrow(new RuntimeException("Error"));
        String result = controller.joinGroupWithPreferences(request);
        assertTrue(result.contains("Error al unirse al grupo"));
    }

    @Test
    void testInviteUserToGroup_missingParams() {
        Map<String, String> body = Map.of("email", "user@example.com");
        String result = controller.inviteUserToGroup(body);
        assertEquals("not valid", result);
    }

    @Test
    void testInviteUserToGroup_exception() {
        Map<String, String> body = Map.of("email", "user@example.com", "groupName", "Group");
        when(groupService.inviteUserToGroup(any(), any())).thenThrow(new RuntimeException("Error"));
        String result = controller.inviteUserToGroup(body);
        assertEquals("error", result);
    }

    @Test
    void testGetInvitedGroups_exception() {
        when(groupService.getInvitedGroups(anyString())).thenThrow(new RuntimeException("Error"));
        List<Group> result = controller.getInvitedGroups("user@example.com");
        assertNull(result);
    }

    @Test
    void testAcceptInvitationWithDetails_exception() {
        Map<String, Object> body = Map.of("email", "e", "groupName", "g", "preference", "p", "startDates", List.of("badDate"));
        String result = controller.acceptInvitationWithDetails(body);
        assertEquals("", result);
    }

    @Test
    void testGetPublicGroups_exception() {
        when(groupService.getPublicGroups()).thenThrow(new RuntimeException("Error"));
        List<Group> result = controller.getPublicGroups();
        assertNull(result);
    }

    @Test
    void testGetUserGroups_invalidEmail() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            controller.getUserGroups("");
        });
        assertEquals("Email is required", exception.getMessage());
    }

    @Test
    void testGetGroupsByTheme_emptyList() {
        when(groupService.getGroupsByTheme("Empty")).thenReturn(List.of());
        List<Group> result = controller.getGroupsByTheme("Empty");
        assertNull(result);
    }

    @Test
    void testGetGroupsByTheme_exception() {
        when(groupService.getGroupsByTheme(anyString())).thenThrow(new RuntimeException("Error"));
        List<Group> result = controller.getGroupsByTheme("Theme");
        assertNull(result);
    }

    @Test
    void testGetGroupsByAudience_emptyList() {
        when(groupService.getGroupsByAudience("Empty")).thenReturn(List.of());
        List<Group> result = controller.getGroupsByAudience("Empty");
        assertNull(result);
    }

    @Test
    void testGetGroupsByAudience_exception() {
        when(groupService.getGroupsByAudience(anyString())).thenThrow(new RuntimeException("Error"));
        List<Group> result = controller.getGroupsByAudience("Audience");
        assertNull(result);
    }


}
