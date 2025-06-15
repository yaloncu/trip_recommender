package com.example.mybackend.controller;

import java.util.List;

import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.mybackend.model.Message;

@RequestMapping("/api/messages")
public interface ChatController {

    void sendMessage(@DestinationVariable Long groupId , @Payload Message incomingMessage);

    @GetMapping("/{groupId}")
    List<Message> getMessagesByGroup(@PathVariable Long groupId );
}
