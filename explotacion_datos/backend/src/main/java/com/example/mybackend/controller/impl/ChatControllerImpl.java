package com.example.mybackend.controller.impl;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.mybackend.controller.ChatController;
import com.example.mybackend.model.Message;
import com.example.mybackend.services.MessageService;

@RestController
@RequestMapping("/api/messages")
public class ChatControllerImpl implements ChatController {
    private final SimpMessagingTemplate messagingTemplate;
    private final MessageService messageService;

    public ChatControllerImpl(SimpMessagingTemplate messagingTemplate, MessageService messageService) {
        this.messagingTemplate = messagingTemplate;
        this.messageService = messageService;
    }

    @MessageMapping("/chat.sendMessage/{groupId}")
    public void sendMessage(@DestinationVariable Long groupId , @Payload Message incomingMessage) {
        Message saved = messageService.save(groupId , incomingMessage.getSender(), incomingMessage.getContent());
        messagingTemplate.convertAndSend("/topic/group/" + groupId , saved);
    }

    @GetMapping("/{groupId}")
    public List<Message> getMessagesByGroup(@PathVariable Long groupId ) {
        return messageService.getMessagesByGroup(groupId);
    }
}
