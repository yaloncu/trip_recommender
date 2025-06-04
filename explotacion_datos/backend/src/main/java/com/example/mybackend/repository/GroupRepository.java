package com.example.mybackend.repository;

import com.example.mybackend.model.Group;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * GroupRepository is a repository for the Group entity.
 */
@Repository
public interface GroupRepository extends Neo4jRepository<Group, Long> {

    @Query("CREATE (g:Group {name: $groupName, email: $email, audience: $audience, privated: $privated, isClosed: false, isClosedVoting: false, tripType: $tripType})" +
            "WITH g " +
            "MATCH (u:User {email: $email}), (g:Group {name: $groupName}) " +
            "CREATE (u)-[r:PERTENECE_A]->(g) " +
            "SET r.preference = $preference, r.availabilityStartDates = $availabilityStartDates, r.availabilityEndDates = $availabilityEndDates")
    void createPrivateGroup(@Param("groupName") String groupName, @Param("email") String email, @Param("audience") String audience, @Param("privated") String privated, @Param("isClosed") boolean isClosed, @Param("tripType") String tripType, @Param("departureDate") LocalDate departureDate, @Param("returnDate") LocalDate returnDate, @Param("availabilityStartDates") List<LocalDate> availabilityStartDates, @Param("availabilityEndDates") List<LocalDate> availabilityEndDates);

        @Query("CREATE (g:Group {name: $groupName, email: $email, audience: $audience, privated: $privated, isClosed: false, isClosedVoting: false, tripType: $tripType, departureDate: $departureDate, returnDate: $returnDate}) " +
        "WITH g " +
        "MATCH (u:User {email: $email}) " +
        "CREATE (u)-[r:PERTENECE_A]->(g) " +
        "SET r.preference = $preference")
        void createPublicGroup(@Param("groupName") String groupName,
                        @Param("email") String email,
                        @Param("audience") String audience,
                        @Param("privated") String privated,
                        @Param("isClosed") boolean isClosed,
                        @Param("tripType") String tripType,
                        @Param("departureDate") LocalDate departureDate,
                        @Param("returnDate") LocalDate returnDate,
                        @Param("availabilityStartDates") List<LocalDate> availabilityStartDates,
                        @Param("availabilityEndDates") List<LocalDate> availabilityEndDates,
                        @Param("preference") String preference);

    @Query("MATCH (g:Group {name: $groupName}) SET g.isClosed = true")
    void closeGroup(@Param("groupName") String groupName);

    @Query("MATCH (g:Group {name: $groupName}) SET g.isClosedVoting = true")
    void closeVoting(@Param("groupName") String groupName);

    @Query("MATCH (g:Group {name: $name}) RETURN g")
    Group findGroupByName(@Param("name") String name);

    @Query("MATCH (u:User {email: $email}) "+
            "MATCH (g:Group {name: $name}) "+
            "MERGE (u)-[r:PERTENECE_A]->(g) "+
            "SET r.preference = $preference, r.availabilityStartDates = $availabilityStartDates, r.availabilityEndDates = $availabilityEndDates")
    void joinGroupWithPreferences(@Param("groupName") String groupName, @Param("userEmail") String userEmail, @Param("preference") String preference, @Param("availabilityStartDates") List<LocalDate> availabilityStartDates, @Param("availabilityEndDates") List<LocalDate> availabilityEndDates);

    @Query("MATCH (u:User {email: $email}) "+
            "MATCH (g:Group {name: $name}) "+
            "MERGE (u)-[r:PERTENECE_A]->(g) "+
            "SET r.preference = $preference")
    void joinPublicGroupWithPreferences(@Param("groupName") String groupName, @Param("userEmail") String userEmail, @Param("preference") String preference);

    @Query("MATCH (u:User {email: $email})-[r:PERTENECE_A]->(g:Group) " +
            "RETURN g")
    List<Group> findUserGroups(@Param("email") String email);

    @Query("MATCH (g:Group {privated: 'public', isClosed: false}) RETURN g")
    List<Group> findPublicGroups();

    @Query("MATCH (u:User {email: $email}), (g:Group {name: $groupName}) " +
            "MERGE (u)-[:INVITADO_A]->(g)")
    void inviteUserToGroup(@Param("groupName") String groupName, @Param("email") String email);

    @Query("MATCH (g:Group {tripType: $triptype}) " +
            "RETURN g")
    List<Group> findGroupsByTripType(@Param("triptype") String triptype);

    @Query("MATCH (g:Group {audience: $audience}) " +
            "RETURN g")
    List<Group> findGroupsByAudience(@Param("audience") String audience);

    @Query("MATCH (u:User {email: $email})-[r:INVITADO_A]->(g:Group) " +
            "RETURN g")
    List<Group> findInvitedGroups(@Param("email") String email);

    @Query("MATCH (u:User {email: $email})-[r:INVITADO_A]->(g:Group {name: $groupName}) " +
            "DELETE r"+
            "MERGE (u)-[r:PERTENECE_A]->(g) " +
            "SET r.preference = $preference, r.availabilityStartDates = $startDates, r.availabilityEndDates = $endDates")
    void acceptInvitation(@Param("groupName") String groupName, @Param("email") String email, @Param("preference") String preference, @Param("startDates") List<LocalDate> startDates, @Param("endDates") List<LocalDate> endDates);

    @Query("MATCH (u:User {email: $email})-[r:PERTENECE_A]->(g:Group {name: $groupName}) " +
            "DELETE r")
    void leaveGroup(@Param("groupName") String groupName, @Param("email") String email);   
    

    @Query("MATCH (u:User)-[r:PERTENECE_A]->(g:Group {name: $groupName}) " +
            "RETURN r.availabilityStartDates ")
    List<LocalDate> getUserAvailabilityStartDates(@Param("groupName") String groupName);

    @Query("MATCH (u:User)-[r:PERTENECE_A]->(g:Group {name: $groupName}) " +
            "RETURN r.availabilityEndDates ")
    List<LocalDate> getUserAvailabilityEndDates(@Param("groupName") String groupName);

    @Query("MATCH (g:Group {name: $groupName}) " +
            "SET g.departureDate = $departureDate, g.returnDate = $returnDate")
    void updateGroupDates(@Param("groupName") String groupName, @Param("departureDate") LocalDate departureDate, @Param("returnDate") LocalDate returnDate);
}