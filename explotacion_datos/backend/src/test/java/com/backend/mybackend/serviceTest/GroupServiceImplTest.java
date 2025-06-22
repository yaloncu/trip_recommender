package com.backend.mybackend.serviceTest;

import com.example.mybackend.model.Group;
import com.example.mybackend.model.User;
import com.example.mybackend.repository.GroupRepository;
import com.example.mybackend.repository.RecommendationRepository;
import com.example.mybackend.repository.UserRepository;
import com.example.mybackend.services.impl.GroupServiceImpl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class GroupServiceImplTest {

    @Mock
    private GroupRepository groupRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private RecommendationRepository recommendationRepository;

    @InjectMocks
    private GroupServiceImpl groupService;
    

    private Group mockGroup;

    @BeforeEach
    void setUp() {
        mockGroup = Group.builder()
                .name("Test Group")
                .email("test@example.com")
                .departureDate(LocalDate.of(2025, 6, 1))
                .returnDate(LocalDate.of(2025, 6, 10))
                .build();
    }

    @Test
    void testCreatePrivateGroup() {
        when(groupRepository.createPrivateGroup(any(), any(), any(), any(), anyBoolean(), any(), any(), any(), any(), any(), any(), any()))
            .thenReturn(mockGroup);

        Group result = groupService.createGroup("Test Group", "test@example.com", "Adults", "private", false,
                "Adventure", LocalDate.now(), LocalDate.now().plusDays(5),
                List.of(LocalDate.now()), List.of(LocalDate.now().plusDays(5)), "Nature", "");

        assertNotNull(result);
        assertEquals("Test Group", result.getName());
        verify(groupRepository).createPrivateGroup(any(), any(), any(), any(), anyBoolean(), any(), any(), any(), any(), any(), any(), any());
    }

    @Test
    void testCreatePublicGroup() {
        when(groupRepository.createPublicGroup(any(), any(), any(), any(), anyBoolean(), any(), any(), any(), any(), any(), any(), any()))
            .thenReturn(mockGroup);
        Group result = groupService.createGroup("Test Group", "test@example.com", "Adults", "public", false,
                "Adventure", LocalDate.now(), LocalDate.now().plusDays(5),
                List.of(LocalDate.now()), List.of(LocalDate.now().plusDays(5)), "Culture", "");

        assertNotNull(result);
        assertEquals("Test Group", result.getName());
        verify(groupRepository).createPublicGroup(any(), any(), any(), any(), anyBoolean(), any(), any(), any(), any(), any(), any(), any());
    }

    @Test
    void testCloseGroup() {
        when(groupRepository.closeGroup("Test Group")).thenReturn(mockGroup);

        Group result = groupService.closeGroup("Test Group");

        assertEquals("Test Group", result.getName());
        verify(groupRepository).closeGroup("Test Group");
    }

    @Test
    void testJoinPublicGroupWithPreferences_success() {
        when(userRepository.findByEmail("test@example.com")).thenReturn(new User());
        when(groupRepository.findGroupByName("Test Group")).thenReturn(mockGroup);
        when(groupRepository.joinPublicGroupWithPreferences("Test Group", "test@example.com", "Nature"))
            .thenReturn("Joined");

        String result = groupService.joinPublicGroupWithPreferences("Test Group", "test@example.com", "Nature");

        assertEquals("Joined", result);
    }

    @Test
    void testJoinPublicGroupWithPreferences_userNotFound() {
        when(userRepository.findByEmail("nonexistent@example.com")).thenReturn(null);

        Exception exception = assertThrows(RuntimeException.class, () -> {
            groupService.joinPublicGroupWithPreferences("Test Group", "nonexistent@example.com", "Nature");
        });

        assertTrue(exception.getMessage().contains("not found"));
    }

    @Test
    void testJoinPublicGroupWithPreferences_groupClosed() {
        Group closedGroup = Group.builder().name("Test Group").isClosed(true).build();
        when(userRepository.findByEmail("test@example.com")).thenReturn(new User());
        when(groupRepository.findGroupByName("Test Group")).thenReturn(closedGroup);

        Exception exception = assertThrows(RuntimeException.class, () -> {
            groupService.joinPublicGroupWithPreferences("Test Group", "test@example.com", "Nature");
        });

        assertTrue(exception.getMessage().contains("not found or is closed"));
    }

    @Test
    void testCloseVoting() {
        when(recommendationRepository.getFinalDestination("Test Group")).thenReturn("Paris");
        when(groupRepository.closeVoting("Test Group")).thenReturn(mockGroup);

        Group result = groupService.closeVoting("Test Group");

        assertNotNull(result);
        verify(groupRepository).setFinalDestination("Test Group", "Paris");
        verify(groupRepository).closeVoting("Test Group");
    }

    @Test
    void testJoinGroupWithPreferences_allValid() {
        when(groupRepository.joinGroupWithPreferences(any(), any(), any(), any(), any())).thenReturn("joined");

        String result = groupService.joinGroupWithPreferences("Test Group", "test@example.com", "Nature",
                List.of(LocalDate.now()), List.of(LocalDate.now().plusDays(2)));

        assertEquals("joined", result);
    }

    @Test
    void testJoinGroupWithPreferences_withNulls() {
        String result = groupService.joinGroupWithPreferences(null, null, null, null, null);
        assertEquals("", result);
    }

    @Test
    void testGetUserGroups() {
        when(groupRepository.findUserGroups("test@example.com")).thenReturn(List.of(mockGroup));

        List<Group> result = groupService.getUserGroups("test@example.com");

        assertEquals(1, result.size());
    }

    @Test
    void testGetGroupByName() {
        when(groupRepository.findGroupByName("Test Group")).thenReturn(mockGroup);

        Group result = groupService.getGroupByName("Test Group");

        assertNotNull(result);
    }

    @Test
    void testGetPublicGroups() {
        when(groupRepository.findPublicGroups()).thenReturn(List.of(mockGroup));

        List<Group> result = groupService.getPublicGroups();

        assertFalse(result.isEmpty());
    }

    @Test
    void testInviteUserToGroup() {
        when(groupRepository.inviteUserToGroup("Test Group", "test@example.com")).thenReturn("invited");

        String result = groupService.inviteUserToGroup("test@example.com", "Test Group");

        assertEquals("invited", result);
    }

    @Test
    void testGetGroupsByTheme() {
        when(groupRepository.findGroupsByTripType("Adventure")).thenReturn(List.of(mockGroup));

        List<Group> result = groupService.getGroupsByTheme("Adventure");

        assertEquals(1, result.size());
    }

    @Test
    void testGetGroupsByAudience() {
        when(groupRepository.findGroupsByAudience("Adults")).thenReturn(List.of(mockGroup));

        List<Group> result = groupService.getGroupsByAudience("Adults");

        assertEquals(1, result.size());
    }

    @Test
    void testGetInvitedGroups() {
        when(groupRepository.findInvitedGroups("test@example.com")).thenReturn(List.of(mockGroup));

        List<Group> result = groupService.getInvitedGroups("test@example.com");

        assertEquals(1, result.size());
    }

    @Test
    void testAcceptInvitationWithDetails_valid() {
        when(groupRepository.acceptInvitation(any(), any(), any(), any(), any())).thenReturn("accepted");

        String result = groupService.acceptInvitationWithDetails("test@example.com", "Test Group", "Nature",
                List.of(LocalDate.now()), List.of(LocalDate.now().plusDays(2)));

        assertEquals("accepted", result);
    }

    @Test
    void testAcceptInvitationWithDetails_mismatchedDates() {
        Exception ex = assertThrows(IllegalArgumentException.class, () -> {
            groupService.acceptInvitationWithDetails("test@example.com", "Test Group", "Nature",
                    List.of(LocalDate.now()), List.of());
        });

        assertTrue(ex.getMessage().contains("número de fechas"));
    }

    @Test
    void testAcceptInvitationWithDetails_nullParams() {
        Exception ex = assertThrows(IllegalArgumentException.class, () -> {
            groupService.acceptInvitationWithDetails(null, null, null, null, null);
        });

        assertTrue(ex.getMessage().contains("obligatorios"));
    }

    @Test
    void testLeaveGroup() {
        when(groupRepository.leaveGroup("Test Group", "test@example.com")).thenReturn(mockGroup);

        Group result = groupService.leaveGroup("test@example.com", "Test Group");

        assertNotNull(result);
    }

    @Test
    void testGetParticipantsByGroupId() {
        when(groupRepository.findParticipantsByGroupId(1L)).thenReturn(List.of());

        List<?> result = groupService.getParticipantsByGroupId(1L);

        assertNotNull(result);
    }

    @Test
    void testCreateGroupWithUnknownPrivacyType_defaultsToPublic() {
        when(groupRepository.createPublicGroup(any(), any(), any(), any(), anyBoolean(), any(), any(), any(), any(), any(), any(), any()))
            .thenReturn(mockGroup);

        Group result = groupService.createGroup("Test Group", "test@example.com", "Adults", "unknown", false,
                "Adventure", LocalDate.now(), LocalDate.now().plusDays(5),
                List.of(LocalDate.now()), List.of(LocalDate.now().plusDays(5)), "Nature", "");

        assertNotNull(result);
        verify(groupRepository).createPublicGroup(any(), any(), any(), any(), anyBoolean(), any(), any(), any(), any(), any(), any(), any());
    }

@Test
void testRecommendDateUsingSlidingWindow_validData() {
    List<LocalDate> startDates = List.of(
        LocalDate.of(2025, 6, 1),
        LocalDate.of(2025, 6, 2),
        LocalDate.of(2025, 6, 3)
    );

    List<LocalDate> endDates = List.of(
        LocalDate.of(2025, 6, 5),
        LocalDate.of(2025, 6, 6),
        LocalDate.of(2025, 6, 4)
    );

    when(groupRepository.getUserAvailabilityStartDates("Test Group")).thenReturn(startDates);
    when(groupRepository.getUserAvailabilityEndDates("Test Group")).thenReturn(endDates);
    doNothing().when(groupRepository).updateGroupDates(any(), any(), any());

    groupService.recommendDateUsingSlidingWindow("Test Group");

    verify(groupRepository).updateGroupDates(eq("Test Group"), any(LocalDate.class), any(LocalDate.class));
}




}
