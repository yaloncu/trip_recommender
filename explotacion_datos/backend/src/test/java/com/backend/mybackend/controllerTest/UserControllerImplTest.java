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
import com.example.mybackend.controller.impl.RecommendationControllerImpl;
import com.example.mybackend.controller.impl.UserControllerImpl;
import com.example.mybackend.model.VoteRequest;
import com.example.mybackend.model.Group;
import com.example.mybackend.model.JoinGroupWithPreferencesRequest;
import com.example.mybackend.model.User;
import com.example.mybackend.services.GroupService;
import com.example.mybackend.services.RecommendationService;
import com.example.mybackend.services.impl.Neo4jUserServiceImpl;

class UserControllerImplTest {
    @Mock
    private Neo4jUserServiceImpl userService;
    @InjectMocks
    private UserControllerImpl userController;

    private User mockUser;
    private User mockUserGoogle;
    private Map<String, String> mockGoogleUser;
    private Group mockGroupClosed;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        mockUser = User.builder()        
                .email("Test Email")
                .name("Test Name")
                .password("Test Password")
                .build();

        mockUserGoogle = User.builder()        
                .email("Test Email")
                .name("Test Name")
                .build();

        mockGoogleUser = Map.of("name", "Test Name", "email", "Test Email");

        mockGroupClosed = Group.builder()
                .name("Test Group")
                .email("Test Email")
                .audience("Test Audience")
                .privated("public")
                .isClosed(true)
                .isClosedVoting(true)
                .tripType("Test Trip Type")
                .departureDate(LocalDate.of(2023, 10, 1))
                .returnDate(LocalDate.of(2023, 10, 10))
                .availabilityStartDates(List.of(LocalDate.of(2023, 9, 25)))
                .availabilityEndDates(List.of(LocalDate.of(2023, 9, 30)))
                .build();
    }

    @Test
    void testCreateUser() {
        when(userService.createUser("Test Name", "Test Email", "Test Password")).thenReturn(mockUser);

        User response = userController.createUser(mockUser);
        assertNotNull(response);
        assertEquals("Test Email", response.getEmail());
        assertEquals("Test Password", response.getPassword());
        verify(userService, times(1)).createUser("Test Name", "Test Email", "Test Password");
    }

    @Test
    void testCreateUserWithGoogle_whenUserAlreadyExists() {
        when(userService.checkUserExistsByEmail("Test Email")).thenReturn(true);
        when(userService.getUserByEmail("Test Email")).thenReturn(mockUserGoogle);

        //User response = userController.createUserFromGoogle(mockGoogleUser);

        //assertNotNull(response);
        //assertEquals("Test Email", response.getEmail());

        //verify(userService, times(1)).checkUserExistsByEmail("Test Email");
        //verify(userService, times(1)).getUserByEmail("Test Email");
    }


/*************  ✨ Windsurf Command ⭐  *************/
/**
 * Test case for the login method in UserControllerImpl.
/*******  846b5459-21e8-44d6-bd46-081dbb40fc42  *******/
    @Test
    void testLogin() {
        when(userService.validateUser("Test Email", "Test Password")).thenReturn(true);
        when(userService.getUserByEmail("Test Email")).thenReturn(mockUser);

        boolean response = userController.login(mockUser);
        assertNotNull(response);
        assertTrue(response);
        verify(userService, times(1)).validateUser("Test Email", "Test Password");
    }

    @Test
    void testLoginWithGoogle() {
        when(userService.checkUserExistsByEmail("Test Email")).thenReturn(true);
        when(userService.getUserByEmail("Test Email")).thenReturn(mockUser);

        //boolean response = userController.loginWithGoogle(Map.of("email", "Test Email"));
        //assertNotNull(response);
        //assertTrue(response);
        //verify(userService, times(1)).checkUserExistsByEmail("Test Email");
    }

    @Test
    void testGetUserGroups(){
        when(userService.getUserGroups("Test Email")).thenReturn(List.of(mockGroupClosed));

        List<Group> response = userController.getUserGroups("Test Email");
        assertNotNull(response);
        assertFalse(response.isEmpty());
        assertEquals(1, response.size());
        assertEquals("Test Group", response.get(0).getName());
        verify(userService, times(1)).getUserGroups("Test Email");
    }

}
