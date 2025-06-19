package com.backend.mybackend.controllerTest;

import com.example.mybackend.controller.impl.ChatControllerImpl;
import com.example.mybackend.model.Message;
import com.example.mybackend.services.MessageService;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ChatControllerImplTest {

    @Mock
    private SimpMessagingTemplate messagingTemplate;

    @Mock
    private MessageService messageService;

    @InjectMocks
    private ChatControllerImpl controller;

    @Test
    void testSendMessage() {
        Long groupId = 1L;
        Message incoming = new Message();
        incoming.setSender("Alice");
        incoming.setContent("Hola grupo");

        Message saved = new Message();
        saved.setSender("Alice");
        saved.setContent("Hola grupo");

        when(messageService.save(groupId, "Alice", "Hola grupo")).thenReturn(saved);

        controller.sendMessage(groupId, incoming);

        verify(messageService).save(groupId, "Alice", "Hola grupo");
        verify(messagingTemplate).convertAndSend("/topic/group/1", saved);
    }

    @Test
    void testSendMessageToActivity() {
        Long activityId = 42L;
        Message incoming = new Message();
        incoming.setSender("Bob");
        incoming.setContent("¡Vamos a la actividad!");

        Message saved = new Message();
        saved.setSender("Bob");
        saved.setContent("¡Vamos a la actividad!");

        when(messageService.saveForActivity(activityId, "Bob", "¡Vamos a la actividad!")).thenReturn(saved);

        controller.sendMessageToActivity(activityId, incoming);

        verify(messageService).saveForActivity(activityId, "Bob", "¡Vamos a la actividad!");
        verify(messagingTemplate).convertAndSend("/topic/activity/42", saved);
    }

    @Test
    void testGetMessagesByGroup() {
        Long groupId = 1L;
        Message m1 = new Message(); m1.setContent("Mensaje 1");
        Message m2 = new Message(); m2.setContent("Mensaje 2");

        when(messageService.getMessagesByGroup(groupId)).thenReturn(List.of(m1, m2));

        List<Message> result = controller.getMessagesByGroup(groupId);

        assertEquals(2, result.size());
        assertEquals("Mensaje 1", result.get(0).getContent());
    }

    @Test
    void testGetMessagesByActivity() {
        Long activityId = 5L;
        Message m1 = new Message(); m1.setContent("Actividad 1");

        when(messageService.getMessagesByActivityGroup(activityId)).thenReturn(List.of(m1));

        List<Message> result = controller.getMessagesByActivity(activityId);

        assertEquals(1, result.size());
        assertEquals("Actividad 1", result.get(0).getContent());
    }
}
