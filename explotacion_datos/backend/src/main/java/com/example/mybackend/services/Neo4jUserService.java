package com.example.mybackend.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.neo4j.driver.Driver;
import org.neo4j.driver.Session;
import org.neo4j.driver.Result;
import org.neo4j.driver.exceptions.Neo4jException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.mybackend.model.User;
import com.example.mybackend.model.Group;

@Service
public class Neo4jUserService {

    private final Driver driver;

    // Contador estático para ID de usuario
    private static long userIdCounter = 0;

    @Autowired
    public Neo4jUserService(Driver driver) {
        this.driver = driver;
    }

    public void createUser(String name, String email, String password) {
        try (Session session = driver.session()) {
            session.executeWrite(tx -> {
                long userId = ++userIdCounter; // Incrementar el contador y asignar ID
                tx.run("CREATE (u:User {userId: $userId, name: $name, email: $email, password: $password})",
                        Map.of("userId", userId, "name", name, "email", email, "password", password));
                return null;
            });
        } catch (Neo4jException e) {
            e.printStackTrace();
            throw new RuntimeException("Error al crear el usuario", e);
        }
    }

    public void addUserToGroup(String email, String groupName) {
        try (Session session = driver.session()) {
            session.executeWrite(tx -> {
                tx.run("MATCH (u:User {email: $email}), (g:Group {name: $groupName}) " +
                       "MERGE (u)-[:PERTENECE_A]->(g)",
                       Map.of("email", email, "groupName", groupName));
                return null;
            });
        } catch (Neo4jException e) {
            e.printStackTrace();
            throw new RuntimeException("Error al agregar usuario al grupo", e);
        }
    }

    public boolean validateUser(String email, String password) {
        try (Session session = driver.session()) {
            return session.executeRead(tx -> {
                Result result = tx.run("MATCH (u:User {email: $email, password: $password}) RETURN COUNT(u) > 0 AS userExists",
                        Map.of("email", email, "password", password));
                return result.single().get("userExists").asBoolean();
            });
        } catch (Neo4jException e) {
            e.printStackTrace();
            return false;
        }
    }

    public User getUserByEmail(String email) {
        try (Session session = driver.session()) {
            return session.executeRead(tx -> {
                Result result = tx.run("MATCH (u:User {email: $email}) RETURN u",
                        Map.of("email", email));
                if (result.hasNext()) {
                    var record = result.next();
                    var node = record.get("u").asNode();
                    return new User(
                        node.get("name").asString(),
                        node.get("email").asString(),
                        node.get("password").asString()
                    );
                } else {
                    return null;
                }
            });
        } catch (Neo4jException e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<Group> getUserGroups(String email) {
        try (Session session = driver.session()) {
            return session.executeRead(tx -> {
                Result result = tx.run("MATCH (u:User {email: $email})-[:PERTENECE_A]->(g:Group) RETURN g",
                        Map.of("email", email));

                List<Group> groups = new ArrayList<>();
                while (result.hasNext()) {
                    var record = result.next();
                    var node = record.get("g").asNode();
                    Group group = new Group(
                        node.get("name").asString() // Asegúrate de que el nodo `Group` tenga la propiedad `name`
                    );
                    groups.add(group);
                }
                return groups;
            });
        } catch (Neo4jException e) {
            e.printStackTrace();
            return new ArrayList<>(); // Retorna una lista vacía en caso de error
        }
    }
}
