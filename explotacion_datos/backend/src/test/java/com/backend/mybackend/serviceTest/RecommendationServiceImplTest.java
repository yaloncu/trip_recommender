package com.backend.mybackend.serviceTest;

import com.example.mybackend.repository.RecommendationRepository;
import com.example.mybackend.services.impl.RecommendationServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.neo4j.driver.Driver;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RecommendationServiceImplTest {

    @Mock
    private Driver driver;

    @Mock
    private RecommendationRepository recommendationRepository;

    private RecommendationServiceImpl recommendationService;

    @BeforeEach
    void setUp() {
        recommendationService = new RecommendationServiceImpl(driver);
        // Usamos reflexión para inyectar el mock manualmente, ya que el constructor no lo permite directamente
        try {
            var field = RecommendationServiceImpl.class.getDeclaredField("recommendationRepository");
            field.setAccessible(true);
            field.set(recommendationService, recommendationRepository);
        } catch (Exception e) {
            fail("No se pudo inyectar el mock del repositorio: " + e.getMessage());
        }
    }

    @Test
    void testCreateGroupDestinationRecommendations() {
        Long groupId = 1L;

        //doNothing().when(recommendationRepository).createGroupDestinationRecommendations(groupId);

        //recommendationService.createGroupDestinationRecommendations(groupId);

        //verify(recommendationRepository, times(1)).createGroupDestinationRecommendations(groupId);
    }

    @Test
    void testGetRecommendations() {
        Long groupId = 2L;
        List<String> expected = List.of("Paris", "Tokyo");

        //when(recommendationRepository.getRecommendations(groupId)).thenReturn(expected);

        //List<String> result = recommendationService.getRecommendations(groupId);

        //assertNotNull(result);
        //assertEquals(2, result.size());
        //assertEquals("Paris", result.get(0));
        //verify(recommendationRepository).getRecommendations(groupId);
    }

    @Test
    void testVoteForCity() {
        Long groupId = 3L;
        String userId = "user@example.com";
        String city = "London";

        when(recommendationRepository.voteForCity(userId, city, groupId)).thenReturn("Vote recorded");

        String result = recommendationService.voteForCity(userId, city, groupId);

        assertEquals("Vote recorded", result);
        verify(recommendationRepository).voteForCity(userId, city, groupId);
    }

    @Test
    void testGetFinalDestination() {
        Long groupId = 4L;
        when(recommendationRepository.getFinalDestination(groupId)).thenReturn("Rome");

        String result = recommendationService.getFinalDestination(groupId);

        assertEquals("Rome", result);
        verify(recommendationRepository).getFinalDestination(groupId);
    }
}
