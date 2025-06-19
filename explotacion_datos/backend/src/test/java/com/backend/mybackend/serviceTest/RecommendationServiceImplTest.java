package com.backend.mybackend.serviceTest;

import com.example.mybackend.repository.RecommendationRepository;
import com.example.mybackend.services.impl.RecommendationServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.neo4j.driver.Driver;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class RecommendationServiceImplTest {

    private RecommendationRepository recommendationRepository;
    private Driver driver;
    private RecommendationServiceImpl recommendationService;

    @BeforeEach
    void setUp() {
        recommendationRepository = mock(RecommendationRepository.class);
        driver = mock(Driver.class);
        recommendationService = new RecommendationServiceImpl(driver, recommendationRepository);
    }

    @Test
    void testCreateGroupDestinationRecommendations() {
        String groupName = "AventuraJoven";

        doNothing().when(recommendationRepository).createGroupDestinationRecommendations(groupName);

        assertDoesNotThrow(() -> recommendationService.createGroupDestinationRecommendations(groupName));
        verify(recommendationRepository, times(1)).createGroupDestinationRecommendations(groupName);
    }

    @Test
    void testGetRecommendations() {
        Long groupId = 1L;
        List<String> mockRecommendations = Arrays.asList("París", "Roma", "Lisboa");

        when(recommendationRepository.getRecommendations(groupId)).thenReturn(mockRecommendations);

        List<String> result = recommendationService.getRecommendations(groupId);

        assertNotNull(result);
        assertEquals(3, result.size());
        assertEquals("París", result.get(0));
        verify(recommendationRepository, times(1)).getRecommendations(groupId);
    }

    @Test
    void testVoteForCity() {
        String userId = "user@example.com";
        String city = "Barcelona";
        Long groupId = 42L;
        String expectedResponse = "Voto registrado";

        when(recommendationRepository.voteForCity(userId, city, groupId)).thenReturn(expectedResponse);

        String result = recommendationService.voteForCity(userId, city, groupId);

        assertEquals(expectedResponse, result);
        verify(recommendationRepository, times(1)).voteForCity(userId, city, groupId);
    }
}
