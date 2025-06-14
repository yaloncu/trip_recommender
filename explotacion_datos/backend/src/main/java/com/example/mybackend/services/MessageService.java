package com.example.mybackend.services;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.mybackend.model.Message;
import com.example.mybackend.repository.MessageRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface MessageService {

/*************  ✨ Windsurf Command ⭐  *************/
/*******  b53f17d6-6d17-4db5-a860-eb4faf75cd16  *******/
    public Message save(Long groupId, String sender, String content);

    public List<Message> getMessagesByGroup(Long groupId);
}