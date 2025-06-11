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
        when(groupRepository.createPrivateGroup(any(), any(), any(), any(), anyBoolean(), any(), any(), any(), any(), any()))
            .thenReturn(mockGroup);

        Group result = groupService.createGroup("Test Group", "test@example.com", "Adults", "private", false,
                "Adventure", LocalDate.now(), LocalDate.now().plusDays(5),
                List.of(LocalDate.now()), List.of(LocalDate.now().plusDays(5)), "Nature");

        assertNotNull(result);
        assertEquals("Test Group", result.getName());
        verify(groupRepository).createPrivateGroup(any(), any(), any(), any(), anyBoolean(), any(), any(), any(), any(), any());
    }

    @Test
    void testCreatePublicGroup() {
        when(groupRepository.createPublicGroup(any(), any(), any(), any(), anyBoolean(), any(), any(), any(), any(), any(), any()))
            .thenReturn(mockGroup);
        Group result = groupService.createGroup("Test Group", "test@example.com", "Adults", "public", false,
                "Adventure", LocalDate.now(), LocalDate.now().plusDays(5),
                List.of(LocalDate.now()), List.of(LocalDate.now().plusDays(5)), "Culture");

        assertNotNull(result);
        assertEquals("Test Group", result.getName());
        verify(groupRepository).createPublicGroup(any(), any(), any(), any(), anyBoolean(), any(), any(), any(), any(), any(), any());
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

}
