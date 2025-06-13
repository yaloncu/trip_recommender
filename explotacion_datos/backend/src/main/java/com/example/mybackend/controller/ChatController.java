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

    public void sendMessage(@DestinationVariable String groupName, @Payload Message incomingMessage);

    @GetMapping("/{groupName}")
    public List<Message> getMessagesByGroup(@PathVariable String groupName);
}
