package com.example.mybackend.services.impl;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.neo4j.driver.Driver;
import org.neo4j.driver.Result;
import org.neo4j.driver.Session;
import org.neo4j.driver.exceptions.Neo4jException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.core.Local;
import org.springframework.stereotype.Service;

import com.example.mybackend.model.Group;
import com.example.mybackend.model.User;
import com.example.mybackend.repository.GroupRepository;
import com.example.mybackend.repository.RecommendationRepository;
import com.example.mybackend.repository.UserRepository;
import com.example.mybackend.services.GroupService;

@Service
public class GroupServiceImpl implements GroupService {
    private static final Logger logger = LoggerFactory.getLogger(GroupService.class);

    private final Driver driver;
    private final GroupRepository groupRepository;
    private final UserRepository userRepository;
    private final RecommendationRepository recommendationRepository;

    @Autowired
    public GroupServiceImpl(Driver driver, GroupRepository groupRepository, UserRepository userRepository, RecommendationRepository recommendationRepository) {
        this.driver = driver;
        this.groupRepository = groupRepository;
        this.userRepository = userRepository; 
        this.recommendationRepository = recommendationRepository;
    }

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
    public Group createGroup(String groupName, String email, String audience, String privated, boolean isClosed,
                        String tripType, LocalDate departureDate, LocalDate returnDate,
                        List<LocalDate> availabilityStartDates, List<LocalDate> availabilityEndDates,
                        String preference) {
    if ("private".equals(privated)) {
        return groupRepository.createPrivateGroup(groupName, email, audience, privated, isClosed, tripType,
            departureDate, returnDate, availabilityStartDates, availabilityEndDates);
    } else {
        return groupRepository.createPublicGroup(groupName, email, audience, privated, isClosed, tripType,
            departureDate, returnDate, availabilityStartDates, availabilityEndDates, preference);
    }
}

    
    /**
     * Closes a group in the Neo4j database.
     *
     * @param groupName The name of the group to close.
     */
    public Group closeGroup(String groupName) {
        recommendationRepository.createGroupDestinationRecommendations(groupName);
        return groupRepository.closeGroup(groupName);
    }

    /**
     * Closes the voting for a group in the Neo4j database.
     *
     * @param groupName The name of the group to close the voting for.
     */
    public Group closeVoting(String groupName) {
        return groupRepository.closeVoting(groupName);
    }

    /**
     * Joins a group with preferences in the Neo4j database.
     *
     * @param groupName The name of the group to join.
     * @param userEmail The email of the user joining the group.
     * @param preference The preference of the user joining the group.
     * @param availabilityStartDates The availability start dates of the user joining the group.
     * @param availabilityEndDates The availability end dates of the user joining the group.
     */
    public String joinGroupWithPreferences(String groupName, String userEmail, String preference, List<LocalDate> availabilityStartDates, List<LocalDate> availabilityEndDates) {
        if (groupName == null || userEmail == null || preference == null) {
            return "";
        }
        return groupRepository.joinGroupWithPreferences(groupName, userEmail, preference, availabilityStartDates, availabilityEndDates);
    }

    /**
     * Joins a public group with preferences in the Neo4j database.
     *
     * @param groupName The name of the group to join.
     * @param userEmail The email of the user joining the group.
     * @param preference The preference of the user joining the group.
     */
    public String joinPublicGroupWithPreferences(String groupName, String userEmail, String preference) {
        if (groupName == null || userEmail == null || preference == null) {
            throw new IllegalArgumentException("Group ID and email must not be null");
        }
        User user = userRepository.findByEmail(userEmail);
        if (user == null) {
            throw new RuntimeException("User with email " + userEmail + " not found.");
        }
        Group group = groupRepository.findGroupByName(groupName);
        if (group == null || group.isClosed()) {
            throw new RuntimeException("Group with name " + groupName + " not found or is closed.");
        }
        return groupRepository.joinPublicGroupWithPreferences(groupName, userEmail, preference);
    }
    
    /**
     * Gets the groups that a user belongs to.
     *
     * @param email The email of the user.
     * @return A list of maps containing the group data.
     */
    public List<Group> getUserGroups(String email) {
        return groupRepository.findUserGroups(email);
    }
    
    /**
     * Gets the groups that a user belongs to.
     *
     * @param email The email of the user.
     * @return A list of maps containing the group data.
     */
    public Group getGroupByName(String name) {
        return groupRepository.findGroupByName(name);
    }

    /**
     * Gets the public groups.
     *
     * @return A list of public groups.
     */
    public List<Group> getPublicGroups() {
        return groupRepository.findPublicGroups();
    }

    /**
     * Invites a user to a private group.
     *
     * @param email The email of the user to invite.
     * @param groupName The name of the group to invite the user to.
     */
    public String inviteUserToGroup(String email, String groupName) {
        return groupRepository.inviteUserToGroup(groupName, email);
    }
    
    /**
     * Gets the groups by theme.
     * @param triptype The type of trip.
     * @return A list of groups with the specified theme.
     */
    public List<Group> getGroupsByTheme(String triptype) {
        return groupRepository.findGroupsByTripType(triptype);
    }
    
    /**
     * Gets the groups by audience.
     * @param audience The audience of the groups.
     * @return A list of groups with the specified audience.
     */
    public List<Group> getGroupsByAudience(String audience) {
        return groupRepository.findGroupsByAudience(audience);
    }
    
    /**
     * Gets the groups that a user has been invited to.
     * @param email The email of the user.
     * @return A list of maps containing the group data.
     */
    public List<Group> getInvitedGroups(String email) {
        return groupRepository.findInvitedGroups(email);
    }

    /**
     * Accepts an invitation to a group.
     * @param email The email of the user accepting the invitation.
     * @param groupName The name of the group to accept the invitation to.
     * @param preference The preference of the user accepting the invitation.
     * @param startDates The availability start dates of the user accepting the invitation.
     * @param endDates The availability end dates of the user accepting the invitation.
     */
    public String acceptInvitationWithDetails(String email, String groupName, String preference, List<LocalDate> startDates, List<LocalDate> endDates) {
        if (email == null || groupName == null || preference == null || startDates == null || endDates == null) {
            throw new IllegalArgumentException("Todos los parámetros son obligatorios.");
        }
        if (startDates.size() != endDates.size()) {
            throw new IllegalArgumentException("El número de fechas de inicio y fin debe coincidir.");
        }
        return groupRepository.acceptInvitation(groupName, email, preference, startDates, endDates);
    }
    
    /**
     * Leaves a group.
     * @param email The email of the user leaving the group.
     * @param groupName The name of the group to leave.
     */
    public Group leaveGroup(String email, String groupName) {
        return groupRepository.leaveGroup(groupName, email);
    }
    
    /**
     * Recommends a date for a group using a sliding window algorithm.
     * @param groupName The name of the group to recommend a date for.
     */
    public void recommendDateUsingSlidingWindow(String groupName) {
        List<LocalDate> startDates = groupRepository.getUserAvailabilityStartDates(groupName);
        List<LocalDate> endDates = groupRepository.getUserAvailabilityEndDates(groupName);
        List<Map<String, Object>> availabilities = new ArrayList<>();
        for (int i = 0; i < startDates.size(); i++) {
            availabilities.add(Map.of("startDates", startDates.get(i), "endDates", endDates.get(i)));
        }
        LocalDate minStartDate = startDates.stream()
            .min(LocalDate::compareTo)
            .orElseThrow(() -> new RuntimeException("No start dates found"));

        LocalDate maxEndDate = endDates.stream()
            .max(LocalDate::compareTo)  
            .orElseThrow(() -> new RuntimeException("No end dates found"));
    
        int totalDays = (int) ChronoUnit.DAYS.between(minStartDate, maxEndDate) + 1;
        List<int[]> userAvailabilityArrays = new ArrayList<>();

        for (Map<String, Object> userAvail : availabilities) {
            int[] availabilityArray = new int[totalDays];
            List<LocalDate> startDates2 = (List<LocalDate>) userAvail.get("startDates");
            List<LocalDate> endDates2 = (List<LocalDate>) userAvail.get("endDates");

            for (int i = 0; i < startDates.size(); i++) {
                LocalDate startDate2 = startDates.get(i);
                LocalDate endDate2 = endDates.get(i);

                int startIndex = (int) ChronoUnit.DAYS.between(minStartDate, startDate2);
                int endIndex = (int) ChronoUnit.DAYS.between(minStartDate, endDate2);

                for (int j = startIndex; j <= endIndex; j++) {
                    availabilityArray[j] = 1;
                }
            }
            userAvailabilityArrays.add(availabilityArray);
        }
    
        // Aplicar Sliding Window 
        int maxCount = 0;
        int bestStartIndex = 0;

        for (int i = 0; i < totalDays - 2; i++) {
            int count = 0;

            for (int[] userAvailability : userAvailabilityArrays) {
                if (userAvailability[i] == 1 && userAvailability[i + 1] == 1 && userAvailability[i + 2] == 1) {
                    count++;
                }
            }

            if (count > maxCount) {
                maxCount = count;
                bestStartIndex = i;
            }
        }

        LocalDate bestStartDate = minStartDate.plusDays(bestStartIndex);
        LocalDate bestEndDate = bestStartDate.plusDays(2);

        logger.info("Calculated bestStartDate: {}", bestStartDate);
        logger.info("Calculated bestEndDate: {}", bestEndDate);

        // Guardar las fechas en el nodo del grupo
        groupRepository.updateGroupDates(groupName, bestStartDate, bestEndDate);    
    } 
}
