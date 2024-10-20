package com.example.mybackend.services;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.neo4j.driver.Driver;
import org.neo4j.driver.Session;
import org.neo4j.driver.Transaction;
import org.neo4j.driver.TransactionWork;
import org.neo4j.driver.Result;
import org.neo4j.driver.exceptions.Neo4jException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.mybackend.model.Group;
import com.example.mybackend.model.User;
import com.example.mybackend.repository.GroupRepository;

@Service
public class GroupService {

    private final Driver driver;

    public GroupService(Driver driver){
        this.driver = driver;
    }

    public void createGroup(String groupName, String email, String audience, String privated, boolean isClosed, String tripType) {
        try (Session session = driver.session()) {
            session.executeWrite(tx -> {
                tx.run("CREATE (g:Group {name: $groupName, email: $email, audience: $audience, privated: $privated, isClosed: false, isClosedVoting: false, tripType: $tripType})", Map.of("groupName", groupName, "email", email, "audience", audience, "privated", privated, "isClosed", isClosed, "tripType", tripType));
                tx.run("MATCH (u:User {email: $email}), (g:Group {name: $groupName}) " +
                       "CREATE (u)-[r:PERTENECE_A]->(g)" +
                       "SET r.preference = $preference",
                       Map.of("email", email, "groupName", groupName, "preference", tripType));
                return null;
            });
        } catch (Neo4jException e) {
            System.err.println("Error creando el grupo: " + e.getMessage());
            throw new RuntimeException("Error creando el grupo: " + e.getMessage(), e);
        }
    }
    
    public void closeGroup(String groupName) {
        try (Session session = driver.session()) {
            session.executeWrite(tx -> {
                tx.run("MATCH (g:Group {name: $groupName}) SET g.isClosed = true", 
                        Map.of("groupName", groupName));
                return null;
            });
        } catch (Neo4jException e) {
            throw new RuntimeException("Error cerrando el grupo", e);
        }
    }

    public void joinGroupWithPreferences(String groupName, String userEmail, String preference) {
        if (groupName == null || userEmail == null || preference == null) {
            throw new IllegalArgumentException("Group ID and email must not be null");
        }
        try (Session session = driver.session()) {
            session.executeWrite(tx -> {
                Result userResult = tx.run("MATCH (u:User {email: $email}) RETURN u",
                    Map.of("email", userEmail));

                if (!userResult.hasNext()) {
                    throw new RuntimeException("User with email " + userEmail + " not found.");
                }

                Result groupResult = tx.run("MATCH (g:Group {name: $name}) RETURN g",
                        Map.of("name", groupName));

                if (!groupResult.hasNext() && groupResult.next().get("closed").asBoolean()) {
                    throw new RuntimeException("Group with name " + groupName + " not found.");
                }


                tx.run("MATCH (u:User {email: $email}) "+
                    "MATCH (g:Group {name: $name}) "+
                    "MERGE (u)-[r:PERTENECE_A]->(g) "+
                    "SET r.preference = $preference",
                    Map.of("email", userEmail, "name", groupName, "preference", preference));
                return null;
            });
        } catch (Neo4jException e) {
            System.err.println("Neo4jException: " + e.getMessage());
            throw new RuntimeException("Neo4jException occurred while joining group with preferences: " + e.getMessage(), e);
        } catch (RuntimeException e) {
            System.err.println("RuntimeException: " + e.getMessage());
            throw new RuntimeException("RuntimeException occurred while joining group with preferences: " + e.getMessage(), e);
        }
    }
    
    public List<Map<String, Object>> getUserGroups(String email) {
        try (Session session = driver.session()) {
            return session.executeRead(tx -> {
                var result = tx.run(
                    "MATCH (u:User {email: $email})-[r:PERTENECE_A]->(g:Group) " +
                    "RETURN g.name AS groupName, g.email AS email, g.isClosed AS isClosed, r.preference AS preference, id(g) AS groupId", // También obtenemos el ID del grupo
                    Map.of("email", email)
                );

                List<Map<String, Object>> groups = new ArrayList<>();
                while (result.hasNext()) {
                    var record = result.next();
                    Map<String, Object> groupData = new HashMap<>();
                    groupData.put("groupName", record.get("groupName").asString());
                    groupData.put("preference", record.containsKey("preference") ? record.get("preference").asString() : "No preference");
                    groupData.put("groupId", record.get("groupId").asLong());
                    groupData.put("email", record.get("email").asString());
                    groupData.put("isClosed", record.get("isClosed").asBoolean());
                    groups.add(groupData);
                }

                return groups;
            });
        } catch (Neo4jException e) {
            throw new RuntimeException("Error retrieving groups for user", e);
        }
    }

    public Group getGroupByName(String name) {
        try (Session session = driver.session()) {
            return session.executeRead(tx -> {
                Result result = tx.run(
                    "MATCH (g:Group {name: $name}) " +
                    "RETURN g",
                    Map.of("name", name)
                );
                
                if (result.hasNext()) {
                    var record = result.next();
                    var groupNode = record.get("g").asNode();
    
                    Group group = new Group();
                    group.setName(groupNode.get("name").asString());
                    group.setAudience(groupNode.get("audience").asString());
                    group.setPrivated(groupNode.get("privated").asString());
    
                    return group;
                } else {
                    return null; 
                }
            });
        } catch (Neo4jException e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<Group> getPublicGroups() {
        try (Session session = driver.session()) {
            return session.executeRead(tx -> {
                var result = tx.run("MATCH (g:Group {privated: 'public', isClosed: false}) RETURN g");
                List<Group> publicGroups = new ArrayList<>();
                while (result.hasNext()) {
                    var record = result.next();
                    var groupNode = record.get("g").asNode();
                    
                    Group group = new Group();
                    group.setName(groupNode.get("name").asString());
                    group.setAudience(groupNode.get("audience").asString());
                    group.setPrivated(groupNode.get("privated").asString());
    
                    publicGroups.add(group);
                }
                return publicGroups;
            });
        } catch (Neo4jException e) {
            throw new RuntimeException("Error retrieving public groups", e);
        }
    }
    
    public void leaveGroup(String email, String groupName) {
        try (Session session = driver.session()) {
            session.executeWrite(tx -> {
                tx.run("MATCH (u:User {email: $email})-[r:PERTENECE_A]->(g:Group {name: $groupName}) " +
                       "DELETE r",
                       Map.of("email", email, "groupName", groupName));
                return null;
            });
        } catch (Neo4jException e) {
            throw new RuntimeException("Error while leaving the group", e);
        }
    }
    
}