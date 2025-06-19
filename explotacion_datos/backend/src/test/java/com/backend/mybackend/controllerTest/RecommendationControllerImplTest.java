package com.backend.mybackend.controllerTest;

import com.example.mybackend.controller.impl.RecommendationControllerImpl;
import com.example.mybackend.model.VoteRequest;
import com.example.mybackend.services.RecommendationService;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class RecommendationControllerImplTest {

    @Mock
    private RecommendationService recommendationService;

    @InjectMocks
    private RecommendationControllerImpl controller;

    @Test
    void testGetRecommendations_success() {
        Long groupId = 1L;
        List<String> mockRecommendations = List.of("Roma", "París", "Lisboa");

        when(recommendationService.getRecommendations(groupId)).thenReturn(mockRecommendations);

        List<String> result = controller.getRecommendations(groupId);

        assertNotNull(result);
        assertEquals(3, result.size());
        assertEquals("Roma", result.get(0));
    }

    @Test
    void testGetRecommendations_exception() {
        Long groupId = 99L;

        when(recommendationService.getRecommendations(groupId)).thenThrow(new RuntimeException("DB error"));

        List<String> result = controller.getRecommendations(groupId);

        assertNull(result);
    }

    @Test
    void testVoteForCity_success() {
        VoteRequest voteRequest = new VoteRequest();
        voteRequest.setUserId("user123");
        voteRequest.setCity("Barcelona");
        voteRequest.setGroupId(42L);

        when(recommendationService.voteForCity("user123", "Barcelona", 42L)).thenReturn("Voto registrado");

        ResponseEntity<String> response = controller.voteForCity(voteRequest);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Voto registrado", response.getBody());
    }

    @Test
    void testVoteForCity_illegalArgument() {
        VoteRequest voteRequest = new VoteRequest();
        voteRequest.setUserId(null); // parámetro inválido
        voteRequest.setCity("Madrid");
        voteRequest.setGroupId(1L);

        when(recommendationService.voteForCity(null, "Madrid", 1L))
            .thenThrow(new IllegalArgumentException("userId no puede ser null"));

        ResponseEntity<String> response = controller.voteForCity(voteRequest);

        assertEquals(400, response.getStatusCodeValue());
        assertTrue(response.getBody().contains("Parámetros inválidos"));
    }

    @Test
    void testVoteForCity_exception() {
        VoteRequest voteRequest = new VoteRequest(2L, "user123", "Roma");

        when(recommendationService.voteForCity("user123", "Roma", 2L))
            .thenThrow(new RuntimeException("Error inesperado"));

        ResponseEntity<String> response = controller.voteForCity(voteRequest);

        assertEquals(500, response.getStatusCodeValue());
        assertTrue(response.getBody().contains("Error interno"));
    }
}
