package com.backend.mybackend.controllerTest;

import com.example.mybackend.controller.impl.ActivityGroupControllerImpl;
import com.example.mybackend.model.*;
import com.example.mybackend.services.ActivityGroupService;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.springframework.http.ResponseEntity;

import java.time.ZonedDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ActivityGroupControllerImplTest {

    @Mock
    private ActivityGroupService service;

    @InjectMocks
    private ActivityGroupControllerImpl controller;

    @Test
    void testCreate() {
        ActivityGroup group = new ActivityGroup();
        group.setTitle("Yoga en el parque");

        when(service.create(group)).thenReturn(group);

        ActivityGroup result = controller.create(group);

        assertNotNull(result);
        assertEquals("Yoga en el parque", result.getTitle());
    }

    @Test
    void testGetAll() {
        ActivityGroupDTO dto1 = new ActivityGroupDTO(
            1L,
            "Caminata",
            "Excursión por el parque",
            "Madrid",
            "Aventura",
            "admin1@example.com",
            ZonedDateTime.parse("2025-07-01T10:00:00+01:00[Europe/Madrid]"),
            5
        );

        ActivityGroupDTO dto2 = new ActivityGroupDTO(
            2L,
            "Yoga",
            "Clase al aire libre",
            "Valencia",
            "Bienestar",
            "admin2@example.com",
            ZonedDateTime.parse("2025-08-15T08:00:00+01:00[Europe/Madrid]"),
            12
        );

        when(service.getAll()).thenReturn(List.of(dto1, dto2));

        List<ActivityGroupDTO> result = controller.getAll();

        assertEquals(2, result.size());
        assertEquals("Caminata", result.get(0).getTitle());
        assertEquals("Yoga", result.get(1).getTitle());
    }


    @Test
    void testGetByUserEmail() {
        ActivityGroup group = new ActivityGroup();
        group.setTitle("Paseo cultural");

        when(service.getActivitiesForUser("ana@example.com")).thenReturn(List.of(group));

        List<ActivityGroup> result = controller.getByUserEmail("ana@example.com");

        assertEquals(1, result.size());
        assertEquals("Paseo cultural", result.get(0).getTitle());
    }

    @Test
    void testJoinActivitySuccess() {
        JoinActivityRequest request = new JoinActivityRequest("pepe@example.com");

        when(service.joinActivity(10L, "pepe@example.com")).thenReturn(true);

        ResponseEntity<String> response = controller.joinActivity(10L, request);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Inscripción exitosa", response.getBody());
    }

    @Test
    void testJoinActivityFailure() {
        JoinActivityRequest request = new JoinActivityRequest("fallo@example.com");

        when(service.joinActivity(5L, "fallo@example.com")).thenReturn(false);

        ResponseEntity<String> response = controller.joinActivity(5L, request);

        assertEquals(400, response.getStatusCodeValue());
        assertEquals("No se pudo completar la inscripción", response.getBody());
    }

    @Test
    void testGetParticipants() {
        SimpleUserDTO user = new SimpleUserDTO("Ana", "ana@example.com", "Me gusta viajar", "F", 28);

        when(service.getParticipants(3L)).thenReturn(List.of(user));

        ResponseEntity<List<SimpleUserDTO>> response = controller.getParticipants(3L);

        assertEquals(1, response.getBody().size());
        assertEquals("Ana", response.getBody().get(0).name());
    }
}
