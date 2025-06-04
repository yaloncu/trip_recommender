package com.example.mybackend.repository;

import com.example.mybackend.model.User;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * UserRepository is a repository for the User entity.
 */
@Repository
public interface UserRepository extends Neo4jRepository<User, Long> {
    User findByEmailAndPassword(String email, String password);

    @Query("CREATE (u:User {name: $name, email: $email, password: $password})")
    void createUser(@Param("name") String name, @Param("email") String email, @Param("password") String password);

    @Query("CREATE (u:User {name: $name, email: $email})")
    void createUser(@Param("name") String name, @Param("email") String email);

    @Query("MATCH (u:User {email: $email}) " +
                       "MATCH (g:Group {name: $groupName}) " +
                       "MERGE (u)-[:PERTENECE_A]->(g)")
    void addUserToGroup(@Param("email") String email, @Param("groupName") String groupName);

    @Query("MATCH (u:User {email: $email, password: $password}) RETURN COUNT(u) > 0 AS userExists")
    boolean validateUser(@Param("email") String email, @Param("password") String password);

    @Query("MATCH (u:User {email: $email}) RETURN COUNT(u) > 0 AS userExists")
    boolean checkUserExists(@Param("email") String email);

    @Query("MATCH (u:User {email: $email}) RETURN u")
    User findByEmail(@Param("email") String email);

    @Query("MATCH (u:User {email: $email})-[:PERTENECE_A]->(g:Group) RETURN g")
    User findUserGroups(@Param("email") String email);
    
}