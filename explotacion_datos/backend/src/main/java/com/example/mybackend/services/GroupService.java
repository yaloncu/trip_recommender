package com.example.mybackend.services;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.neo4j.driver.Driver;
import org.neo4j.driver.Session;
import org.neo4j.driver.Result;
import org.neo4j.driver.exceptions.Neo4jException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.mybackend.model.Group;

@Service
public class GroupService {

    private final Driver driver;

    // Contador estático para ID de grupo
    private static long groupIdCounter = 0;

    @Autowired
    public GroupService(Driver driver) {
        this.driver = driver;
    }

    public void createGroup(String groupName, String email, String audience, String privated, boolean isClosed, String tripType) {
        try (Session session = driver.session()) {
            session.executeWrite(tx -> {
                long groupId = ++groupIdCounter; // Incrementar el contador y asignar ID
                tx.run("CREATE (g:Group {groupId: $groupId, name: $groupName, email: $email, audience: $audience, privated: $privated, isClosed: $isClosed, tripType: $tripType})",
                        Map.of("groupId", groupId, "groupName", groupName, "email", email, "audience", audience, "privated", privated, "isClosed", isClosed, "tripType", tripType));
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
            throw new IllegalArgumentException("Group name, email y preference deben ser no nulos.");
        }
        try (Session session = driver.session()) {
            session.executeWrite(tx -> {
                // Verifica que el usuario exista
                Result userResult = tx.run("MATCH (u:User {email: $email}) RETURN u",
                        Map.of("email", userEmail));

                if (!userResult.hasNext()) {
                    throw new RuntimeException("Usuario con email " + userEmail + " no encontrado.");
                }

                // Verifica que el grupo exista y que no esté cerrado
                Result groupResult = tx.run("MATCH (g:Group {name: $name}) RETURN g.isClosed AS isClosed",
                        Map.of("name", groupName));

                if (!groupResult.hasNext()) {
                    throw new RuntimeException("Grupo con nombre " + groupName + " no encontrado.");
                }

                if (groupResult.next().get("isClosed").asBoolean()) {
                    throw new RuntimeException("Grupo con nombre " + groupName + " está cerrado.");
                }

                // Relaciona el usuario con el grupo
                tx.run("MATCH (u:User {email: $email}), (g:Group {name: $name}) " +
                        "MERGE (u)-[r:PERTENECE_A]->(g) " +
                        "SET r.preference = $preference",
                        Map.of("email", userEmail, "name", groupName, "preference", preference));
                return null;
            });
        } catch (Neo4jException e) {
            System.err.println("Neo4jException: " + e.getMessage());
            throw new RuntimeException("Ocurrió un error al intentar unirse al grupo: " + e.getMessage(), e);
        }
    }

    public List<Map<String, Object>> getUserGroups(String email) {
        try (Session session = driver.session()) {
            var groups = session.executeRead(tx -> {
                var result = tx.run(
                        "MATCH (u:User {email: $email})-[r:PERTENECE_A]->(g:Group) " +
                        "RETURN g.name AS groupName, r.preference AS preference, g.groupId AS groupId",
                        Map.of("email", email));
                    return result.list();
            });

            List<Map<String, Object>> grupos = new ArrayList<>();
            groups.forEach(group -> {
                Map<String, Object> groupData = new HashMap<>();
                    groupData.put("groupName", group.get("groupName").asString());
                    groupData.put("tripType", group.get("tripType").asString());
                    groupData.put("groupId", group.get("groupId").asString());
                    grupos.add(groupData);
            });
            return grupos;
        } catch (Neo4jException e) {
            throw new RuntimeException("Error recuperando grupos del usuario", e);
        }
    }

    public Group getGroupByName(String name) {
        try (Session session = driver.session()) {
            return session.executeRead(tx -> {
                Result result = tx.run(
                        "MATCH (g:Group {name: $name}) RETURN g",
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
                var result = tx.run("MATCH (g:Group {privated: false}) RETURN g");
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
            throw new RuntimeException("Error recuperando grupos públicos", e);
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
            throw new RuntimeException("Error al intentar salir del grupo", e);
        }
    }
}
