package com.example.mybackend.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.mybackend.model.Message;
import com.example.mybackend.repository.MessageRepository;
import com.example.mybackend.services.MessageService;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class MessageServiceImpl implements MessageService {
    private final MessageRepository messageRepository;

    @Autowired
    public MessageServiceImpl(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

/*************  ✨ Windsurf Command ⭐  *************/
/*******  b53f17d6-6d17-4db5-a860-eb4faf75cd16  *******/
    public Message save(String groupName, String sender, String content) {
        Message message = new Message(null, groupName, sender, content, LocalDateTime.now());
        return messageRepository.save(message);
    }

    public List<Message> getMessagesByGroup(String groupName) {
        return messageRepository.findByGroupNameOrderByTimestampAsc(groupName);
    }
}
