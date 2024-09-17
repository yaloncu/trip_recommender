package com.example.mybackend.services;

import java.util.ArrayList;
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

    public void createGroup(String name, String audience, List<User> users) {
        try (Session session = driver.session()) {
            session.executeWrite(tx -> {
                tx.run("CREATE (g:Group {name: $name, audience: $audience})",
                        Map.of("name", name, "audience", audience));
                
                return null;
            });
        } catch (Neo4jException e) {
            e.printStackTrace();
            throw e;
        }
    }

    public void joinGroupWithPreferences(Long groupId, String userEmail, String preference) {
        if (groupId == null || userEmail == null || preference == null) {
            throw new IllegalArgumentException("Group ID and email must not be null");
        }
        try (Session session = driver.session()) {
            session.executeWrite(tx -> {
                Result userResult = tx.run("MATCH (u:User {email: $email}) RETURN u",
                    Map.of("email", userEmail));

                if (!userResult.hasNext()) {
                    throw new RuntimeException("User with email " + userEmail + " not found.");
                }

                // Verificar si el grupo existe
                Result groupResult = tx.run("MATCH (g:Group) WHERE id(g) = $groupId RETURN g",
                        Map.of("groupId", groupId));

                if (!groupResult.hasNext()) {
                    throw new RuntimeException("Group with ID " + groupId + " not found.");
                }


                // Unir al grupo
                tx.run("MATCH (u:User {email: $email}) "+
                    "MATCH (g:Group) WHERE id(g) = $groupId "+
                    "MERGE (u)-[r:PERTENECE_A]->(g) "+
                    "SET r.preference = $preference",
                    Map.of("email", userEmail, "groupId", groupId, "preference", preference));
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
                    "MATCH (u:User {email: $email})-[:PERTENECE_A]->(g:Group) " +
                    "RETURN g.name AS groupName, id(g) AS groupId, u.preference AS preference",
                    Map.of("email", email)
                );
                List<Map<String, Object>> groups = new ArrayList<>();
                while (result.hasNext()) {
                    groups.add(result.next().asMap());
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
                Result result = tx.run("MATCH (g:Group {name: $name})-[:PERTENECE_A]->(u:User) RETURN g, collect(u) as users",
                        Map.of("name", name));
                if (result.hasNext()) {
                    var record = result.next();
                    
                    // Obtener el nodo de grupo
                    var groupNode = record.get("g").asNode();
                    
                    // Crear el objeto Group
                    Group group = new Group();
                    group.setName(groupNode.get("name").asString());
                    
                    // Manejar el ID (si existe)
                    if (groupNode.containsKey("id")) {
                        Object idValue = groupNode.get("id");
                        if (idValue instanceof Number) {
                            group.setId(((Number) idValue).longValue());
                        } else {
                            // Manejo de error si el ID no es un número
                            System.out.println("ID no es del tipo Long. Valor: " + idValue);
                            group.setId(null);
                        }
                    }

                    // Obtener los nodos de usuarios
                    List<Object> userNodes = record.get("users").asList();
                    for (Object userNodeObj : userNodes) {
                        var userNode = (org.neo4j.driver.internal.InternalNode) userNodeObj;
                        var user = new User(
                            userNode.get("name").asString(),
                            userNode.get("email").asString(),
                            userNode.get("password").asString()
                        );
                    }

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

}