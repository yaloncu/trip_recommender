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
import com.example.mybackend.controller.impl.RecommendationControllerImpl.VoteRequest;
import com.example.mybackend.model.Group;
import com.example.mybackend.model.JoinGroupWithPreferencesRequest;
import com.example.mybackend.services.GroupService;
import com.example.mybackend.services.RecommendationService;

class RecommendationControllerImplTest {
    @Mock
    private RecommendationService recommendationService;
    @InjectMocks
    private RecommendationControllerImpl recommendationController;

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
                .tripType("Test Trip Type")
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
                .tripType("Test Trip Type")
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
    void testGetRecommendations() {
        Long groupId = 1L;
        //when(recommendationService.getRecommendations(groupId)).thenReturn(List.of("Recommendation 1", "Recommendation 2"));

        //List<String> response = recommendationController.getRecommendations(groupId);

        //assertNotNull(response);
        //assertEquals(List.of("Recommendation 1", "Recommendation 2"), response);
        //verify(recommendationService, times(1)).createGroupDestinationRecommendations(groupId);
        //verify(recommendationService, times(1)).getRecommendations(groupId);
    }

    @Test
    void testGetFinalDestination() {
        Long groupId = 1L;
        when(recommendationService.getFinalDestination(groupId)).thenReturn("Final Destination");

        String response = recommendationController.getFinalDestination(groupId);

        assertNotNull(response);
        assertEquals("Final Destination", response);
        verify(recommendationService, times(1)).getFinalDestination(groupId);
    }

    @Test
    void testVoteForCity() {
        Long groupId = 1L;
        String userId = "1"; 
        String city = "City";

        RecommendationControllerImpl.VoteRequest voteRequest =
            new RecommendationControllerImpl.VoteRequest(groupId, userId, city);

        when(recommendationService.voteForCity(userId, city, groupId)).thenReturn("Vote registered");
        String response = recommendationController.voteForCity(voteRequest);
        assertNotNull(response);
        assertEquals("Vote registered", response);
        verify(recommendationService, times(1)).voteForCity(userId, city, groupId);
    }

}
