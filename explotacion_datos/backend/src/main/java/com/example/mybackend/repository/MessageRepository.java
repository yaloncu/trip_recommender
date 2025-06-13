package com.example.mybackend.repository;
import java.util.List;

import org.springframework.data.neo4j.repository.Neo4jRepository;
import com.example.mybackend.model.Message;
public interface MessageRepository extends Neo4jRepository<Message, Long> {
    List<Message> findByGroupNameOrderByTimestampAsc(String groupName);
}
