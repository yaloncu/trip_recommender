package com.example.mybackend.services;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.neo4j.driver.Driver;
import org.neo4j.driver.Session;
import org.neo4j.driver.Transaction;
import org.neo4j.driver.TransactionWork;
import org.neo4j.driver.Result;
import org.neo4j.driver.exceptions.Neo4jException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.core.Local;
import org.springframework.stereotype.Service;

import com.example.mybackend.model.Group;
import com.example.mybackend.model.SimpleUserDTO;
import com.example.mybackend.model.User;
import com.example.mybackend.repository.GroupRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * GroupService is a service that provides operations for managing groups in the application.
 */
public interface GroupService {

    /**
     * Creates a new group in the Neo4j database.
     * 
     * @param groupName The name of the group.
     * @param email The email of the user creating the group.
     * @param audience The audience of the group.
     * @param privated The privacy setting of the group.
     * @param isClosed Whether the group is closed or not.
     * @param tripType The type of trip for the group.
     * @param departureDate The departure date for the group.
     * @param returnDate The return date for the group.
     * @param availabilityStartDates The availability start dates for the group.
     * @param availabilityEndDates The availability end dates for the group.
     */
    Group createGroup(String groupName, String email, String audience, String privated, boolean isClosed,
                    String tripType, LocalDate departureDate, LocalDate returnDate,
                    List<LocalDate> availabilityStartDates, List<LocalDate> availabilityEndDates,
                    String preference);   
    /**
     * Closes a group in the Neo4j database.
     *
     * @param groupName The name of the group to close.
     */
    Group closeGroup(String groupName);

    /**
     * Closes the voting for a group in the Neo4j database.
     *
     * @param groupName The name of the group to close the voting for.
     */
    Group closeVoting(String groupName);

    /**
     * Joins a group with preferences in the Neo4j database.
     *
     * @param groupName The name of the group to join.
     * @param userEmail The email of the user joining the group.
     * @param preference The preference of the user joining the group.
     * @param availabilityStartDates The availability start dates of the user joining the group.
     * @param availabilityEndDates The availability end dates of the user joining the group.
     */
    String joinGroupWithPreferences(String groupName, String userEmail, String preference, List<LocalDate> availabilityStartDates, List<LocalDate> availabilityEndDates);
    
    /**
     * Joins a public group with preferences in the Neo4j database.
     *
     * @param groupName The name of the group to join.
     * @param userEmail The email of the user joining the group.
     * @param preference The preference of the user joining the group.
     */
    String joinPublicGroupWithPreferences(String groupName, String userEmail, String preference);
    
    /**
     * Gets the groups that a user belongs to.
     *
     * @param email The email of the user.
     * @return A list of maps containing the group data.
     */
    List<Group> getUserGroups(String email);
    
    /**
     * Gets the groups that a user belongs to.
     *
     * @param email The email of the user.
     * @return A list of maps containing the group data.
     */
    Group getGroupByName(String name);

    /**
     * Gets the public groups.
     *
     * @return A list of public groups.
     */
    List<Group> getPublicGroups();

    /**
     * Invites a user to a private group.
     *
     * @param email The email of the user to invite.
     * @param groupName The name of the group to invite the user to.
     */
    String inviteUserToGroup(String email, String groupName);
    
    /**
     * Gets the groups by theme.
     * @param triptype The type of trip.
     * @return A list of groups with the specified theme.
     */
    List<Group> getGroupsByTheme(String triptype);
    
    /**
     * Gets the groups by audience.
     * @param audience The audience of the groups.
     * @return A list of groups with the specified audience.
     */
    List<Group> getGroupsByAudience(String audience);
    
    /**
     * Gets the groups that a user has been invited to.
     * @param email The email of the user.
     * @return A list of maps containing the group data.
     */
    List<Group> getInvitedGroups(String email);

    /**
     * Accepts an invitation to a group.
     * @param email The email of the user accepting the invitation.
     * @param groupName The name of the group to accept the invitation to.
     * @param preference The preference of the user accepting the invitation.
     * @param startDates The availability start dates of the user accepting the invitation.
     * @param endDates The availability end dates of the user accepting the invitation.
     */
    String acceptInvitationWithDetails(String email, String groupName, String preference, List<LocalDate> startDates, List<LocalDate> endDates);
    
    /**
     * Leaves a group.
     * @param email The email of the user leaving the group.
     * @param groupName The name of the group to leave.
     */
    Group leaveGroup(String email, String groupName);
    
    /**
     * Recommends a date for a group using a sliding window algorithm.
     * @param groupName The name of the group to recommend a date for.
     */
    void recommendDateUsingSlidingWindow(String groupName);

    List<SimpleUserDTO> getParticipantsByGroupId(Long groupId);
}