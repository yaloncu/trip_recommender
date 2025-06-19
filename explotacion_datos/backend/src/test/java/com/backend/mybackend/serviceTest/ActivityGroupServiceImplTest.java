package com.backend.mybackend.serviceTest;

import com.example.mybackend.model.*;
import com.example.mybackend.repository.ActivityGroupRepository;
import com.example.mybackend.repository.UserRepository;
import com.example.mybackend.services.impl.ActivityGroupServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class ActivityGroupServiceImplTest {

    @Mock
    private ActivityGroupRepository activityGroupRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private ActivityGroupServiceImpl activityGroupService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testCreate_AdminExists() {
        User admin = new User();
        admin.setEmail("admin@example.com");

        ActivityGroup group = new ActivityGroup();
        group.setAdminEmail("admin@example.com");

        when(userRepository.findByEmail("admin@example.com")).thenReturn(admin);
        when(activityGroupRepository.save(any(ActivityGroup.class))).thenAnswer(i -> i.getArguments()[0]);

        ActivityGroup result = activityGroupService.create(group);

        assertNotNull(result.getInscritos());
        assertEquals(1, result.getInscritos().size());
        assertEquals("admin@example.com", result.getInscritos().get(0).getEmail());
    }

    @Test
    public void testCreate_AdminNotFound() {
        ActivityGroup group = new ActivityGroup();
        group.setAdminEmail("notfound@example.com");

        when(userRepository.findByEmail("notfound@example.com")).thenReturn(null);

        assertThrows(RuntimeException.class, () -> activityGroupService.create(group));
    }

    @Test
    public void testGetAll() {
        ActivityGroup group = new ActivityGroup();
        group.setId(1L);
        group.setTitle("Yoga");
        group.setDescription("Yoga en el parque");
        group.setLocation("Madrid");
        group.setType("Relax");
        group.setAdminEmail("admin@example.com");
        group.setStartDateTime(ZonedDateTime.now());
        group.setInscritos(List.of(new User(), new User()));

        when(activityGroupRepository.findAll()).thenReturn(List.of(group));

        List<ActivityGroupDTO> result = activityGroupService.getAll();

        assertEquals(1, result.size());
        assertEquals("Yoga", result.get(0).getTitle());
        assertEquals(2, result.get(0).getNumberOfParticipants());
    }

    @Test
    public void testGetActivitiesForUser() {
        when(activityGroupRepository.findByInscritosEmail("user@example.com")).thenReturn(List.of(new ActivityGroup()));

        List<ActivityGroup> result = activityGroupService.getActivitiesForUser("user@example.com");

        assertEquals(1, result.size());
    }

    @Test
    public void testJoinActivity_NotAlreadyJoined() {
        User user = new User();
        user.setEmail("user@example.com");

        ActivityGroup group = new ActivityGroup();
        group.setId(1L);
        group.setInscritos(new ArrayList<>());

        when(userRepository.findByEmail("user@example.com")).thenReturn(user);
        when(activityGroupRepository.findById(1L)).thenReturn(Optional.of(group));
        when(activityGroupRepository.save(any(ActivityGroup.class))).thenReturn(group);

        boolean result = activityGroupService.joinActivity(1L, "user@example.com");

        assertTrue(result);
        assertEquals(1, group.getInscritos().size());
    }

    @Test
    public void testJoinActivity_AlreadyJoined() {
        User user = new User();
        user.setEmail("user@example.com");

        ActivityGroup group = new ActivityGroup();
        group.setId(1L);
        group.setInscritos(new ArrayList<>(List.of(user)));

        when(userRepository.findByEmail("user@example.com")).thenReturn(user);
        when(activityGroupRepository.findById(1L)).thenReturn(Optional.of(group));

        boolean result = activityGroupService.joinActivity(1L, "user@example.com");

        assertFalse(result);
    }

    @Test
    public void testJoinActivity_GroupNotFound() {
        when(activityGroupRepository.findById(99L)).thenReturn(Optional.empty());

        boolean result = activityGroupService.joinActivity(99L, "user@example.com");

        assertFalse(result);
    }

    @Test
    public void testGetParticipants() {
        SimpleUserDTO userDTO = new SimpleUserDTO("Name", "email@example.com", "about", "F", 30);
        when(activityGroupRepository.findParticipantsByActivityId(1L)).thenReturn(List.of(userDTO));

        List<SimpleUserDTO> result = activityGroupService.getParticipants(1L);

        assertEquals(1, result.size());
    }
}
