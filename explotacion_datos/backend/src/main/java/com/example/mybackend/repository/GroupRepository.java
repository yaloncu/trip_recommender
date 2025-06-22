package com.example.mybackend.repository;

import com.example.mybackend.model.Group;
import com.example.mybackend.model.SimpleUserDTO;

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

    @Query("CREATE (g:Group {name: $name, email: $email, audience: $audience, privated: $privated, isClosed: false, isClosedVoting: false, tripType: $tripType}) " +
            "WITH g " +
            "MATCH (u:User {email: $email}), (g:Group {name: $name}) " +
            "CREATE (u)-[r:PERTENECE_A]->(g) " +
            "SET r.preference = $preference " +
            "SET r.finalDestination = $finalDestination " +
            "SET r.preference = $preference, r.availabilityStartDates = $availabilityStartDates, r.availabilityEndDates = $availabilityEndDates " +
            "RETURN g")
    Group createPrivateGroup(@Param("name") String groupName, @Param("email") String email, @Param("audience") String audience, @Param("privated") String privated, @Param("isClosed") boolean isClosed, @Param("tripType") String tripType, @Param("departureDate") LocalDate departureDate, @Param("returnDate") LocalDate returnDate, @Param("availabilityStartDates") List<LocalDate> availabilityStartDates, @Param("availabilityEndDates") List<LocalDate> availabilityEndDates, @Param("preference") String preference, @Param("finalDestination") String finalDestination);

        @Query("CREATE (g:Group {name: $name, email: $email, audience: $audience, privated: $privated, isClosed: false, isClosedVoting: false, tripType: $tripType, departureDate: $departureDate, returnDate: $returnDate}) " +
        "WITH g " +
        "MATCH (u:User {email: $email}) " +
        "CREATE (u)-[r:PERTENECE_A]->(g) " +
        "SET r.preference = $preference " +
        "SET r.finalDestination = $finalDestination " +
        "RETURN g")
        Group createPublicGroup(@Param("name") String groupName,
                        @Param("email") String email,
                        @Param("audience") String audience,
                        @Param("privated") String privated,
                        @Param("isClosed") boolean isClosed,
                        @Param("tripType") String tripType,
                        @Param("departureDate") LocalDate departureDate,
                        @Param("returnDate") LocalDate returnDate,
                        @Param("availabilityStartDates") List<LocalDate> availabilityStartDates,
                        @Param("availabilityEndDates") List<LocalDate> availabilityEndDates,
                        @Param("preference") String preference,
                        @Param("finalDestination") String finalDestination);

    @Query("MATCH (g:Group {name: $name}) SET g.isClosed = true " +
        "RETURN g")
    Group closeGroup(@Param("name") String groupName);

    @Query("MATCH (g:Group) WHERE g.name = $name SET g.isClosedVoting = true " +
        "RETURN g")
    Group closeVoting(@Param("name") String groupName);

    @Query("MATCH (g:Group {name: $name}) " +
       "SET g.finalDestination = $destination")
    void setFinalDestination(@Param("name") String groupName, @Param("destination") String destination);

    @Query("MATCH (g:Group {name: $name}) RETURN g")
    Group findGroupByName(@Param("name") String name);

    @Query("MATCH (u:User {email: $email}) "+
            "MATCH (g:Group {name: $name}) "+
            "MERGE (u)-[r:PERTENECE_A]->(g) "+
            "SET r.preference = $preference, r.availabilityStartDates = $availabilityStartDates, r.availabilityEndDates = $availabilityEndDates " +
            "RETURN r.preference")
    String joinGroupWithPreferences(@Param("name") String groupName, @Param("email") String userEmail, @Param("preference") String preference, @Param("availabilityStartDates") List<LocalDate> availabilityStartDates, @Param("availabilityEndDates") List<LocalDate> availabilityEndDates);

    @Query("MATCH (u:User {email: $email}) "+
            "MATCH (g:Group {name: $name}) "+
            "MERGE (u)-[r:PERTENECE_A]->(g) "+
            "SET r.preference = $preference " +
            "RETURN r.preference")
    String joinPublicGroupWithPreferences(@Param("name") String groupName, @Param("email") String userEmail, @Param("preference") String preference);

    @Query("MATCH (u:User {email: $email})-[r:PERTENECE_A]->(g:Group) " +
            "RETURN g")
    List<Group> findUserGroups(@Param("email") String email);

    @Query("MATCH (g:Group {privated: 'public', isClosed: false}) RETURN g")
    List<Group> findPublicGroups();

    @Query("MATCH (u:User {email: $email}), (g:Group {name: $name}) " +
            "MERGE (u)-[:INVITADO_A]->(g)" +
            "RETURN u.name")
    String inviteUserToGroup(@Param("name") String groupName, @Param("email") String email);

    @Query("MATCH (g:Group {tripType: $triptype}) " +
            "RETURN g")
    List<Group> findGroupsByTripType(@Param("triptype") String triptype);

    @Query("MATCH (g:Group {audience: $audience}) " +
            "RETURN g")
    List<Group> findGroupsByAudience(@Param("audience") String audience);

    @Query("MATCH (u:User {email: $email})-[r:INVITADO_A]->(g:Group) " +
            "RETURN g")
    List<Group> findInvitedGroups(@Param("email") String email);

    @Query("MATCH (u:User {email: $email})-[r:INVITADO_A]->(g:Group {name: $name}) " +
            "DELETE r"+
            "MERGE (u)-[r:PERTENECE_A]->(g) " +
            "SET r.preference = $preference, r.availabilityStartDates = $startDates, r.availabilityEndDates = $endDates" +
            "RETURN g.name")
    String acceptInvitation(@Param("name") String groupName, @Param("email") String email, @Param("preference") String preference, @Param("startDates") List<LocalDate> startDates, @Param("endDates") List<LocalDate> endDates);

    @Query("MATCH (u:User {email: $email})-[r:PERTENECE_A]->(g:Group {name: $name}) " +
            "DELETE r" +
            "RETURN g")
    Group leaveGroup(@Param("name") String groupName, @Param("email") String email);   
    

    @Query("MATCH (u:User)-[r:PERTENECE_A]->(g:Group {name: $name}) " +
            "RETURN r.availabilityStartDates ")
    List<LocalDate> getUserAvailabilityStartDates(@Param("name") String groupName);

    @Query("MATCH (u:User)-[r:PERTENECE_A]->(g:Group {name: $name}) " +
            "RETURN r.availabilityEndDates ")
    List<LocalDate> getUserAvailabilityEndDates(@Param("name") String groupName);

    @Query("MATCH (g:Group {name: $name}) " +
            "SET g.departureDate = $departureDate, g.returnDate = $returnDate")
    void updateGroupDates(@Param("name") String groupName, @Param("departureDate") LocalDate departureDate, @Param("returnDate") LocalDate returnDate);

    @Query("""
        MATCH (u:User)-[:PERTENECE_A]->(g:Group)
        WHERE id(g) = $groupId
        RETURN u.name AS name, u.email AS email, u.aboutMe AS aboutMe, u.gender AS gender, u.age AS age
        """)
    List<SimpleUserDTO> findParticipantsByGroupId(Long groupId);

}