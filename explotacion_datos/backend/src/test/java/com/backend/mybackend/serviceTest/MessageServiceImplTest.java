package com.backend.mybackend.serviceTest;

import com.example.mybackend.model.Message;
import com.example.mybackend.repository.MessageRepository;
import com.example.mybackend.services.impl.MessageServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class MessageServiceImplTest {

    private MessageRepository messageRepository;
    private MessageServiceImpl messageService;

    @BeforeEach
    void setUp() {
        messageRepository = mock(MessageRepository.class);
        messageService = new MessageServiceImpl(messageRepository);
    }

    @Test
    void testSave() {
        Long groupId = 1L;
        String sender = "user@example.com";
        String content = "Hola grupo";

        Message savedMessage = new Message();
        savedMessage.setGroupId(groupId);
        savedMessage.setSender(sender);
        savedMessage.setContent(content);
        savedMessage.setTimestamp(LocalDateTime.now());

        when(messageRepository.save(any(Message.class))).thenReturn(savedMessage);

        Message result = messageService.save(groupId, sender, content);

        assertNotNull(result);
        assertEquals(groupId, result.getGroupId());
        assertEquals(sender, result.getSender());
        assertEquals(content, result.getContent());
        verify(messageRepository, times(1)).save(any(Message.class));
    }

    @Test
    void testGetMessagesByGroup() {
        Long groupId = 2L;
        Message m1 = new Message();
        Message m2 = new Message();
        List<Message> messages = Arrays.asList(m1, m2);

        when(messageRepository.findByGroupIdOrderByTimestampAsc(groupId)).thenReturn(messages);

        List<Message> result = messageService.getMessagesByGroup(groupId);

        assertEquals(2, result.size());
        verify(messageRepository).findByGroupIdOrderByTimestampAsc(groupId);
    }

    @Test
    void testSaveForActivity() {
        Long activityId = 3L;
        String sender = "admin@domain.com";
        String content = "Bienvenidos a la actividad";

        Message savedMessage = new Message();
        savedMessage.setActivityGroupId(activityId);
        savedMessage.setSender(sender);
        savedMessage.setContent(content);
        savedMessage.setTimestamp(LocalDateTime.now());

        when(messageRepository.save(any(Message.class))).thenReturn(savedMessage);

        Message result = messageService.saveForActivity(activityId, sender, content);

        assertNotNull(result);
        assertEquals(activityId, result.getActivityGroupId());
        assertEquals(sender, result.getSender());
        assertEquals(content, result.getContent());
        verify(messageRepository, times(1)).save(any(Message.class));
    }

    @Test
    void testGetMessagesByActivityGroup() {
        Long activityId = 4L;
        Message m1 = new Message();
        Message m2 = new Message();
        when(messageRepository.findByActivityGroupIdOrderByTimestampAsc(activityId)).thenReturn(List.of(m1, m2));

        List<Message> result = messageService.getMessagesByActivityGroup(activityId);

        assertEquals(2, result.size());
        verify(messageRepository).findByActivityGroupIdOrderByTimestampAsc(activityId);
    }
}
