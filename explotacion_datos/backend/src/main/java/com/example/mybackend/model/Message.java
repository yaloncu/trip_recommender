package com.example.mybackend.model;

import java.time.LocalDateTime;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Relationship;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Node
@Builder
@Data 
@NoArgsConstructor
@AllArgsConstructor
public class Message {
  @Id 
  @GeneratedValue
  private Long id;

  private String groupName;
  private String sender;
  private String content;
  private LocalDateTime timestamp;
}
