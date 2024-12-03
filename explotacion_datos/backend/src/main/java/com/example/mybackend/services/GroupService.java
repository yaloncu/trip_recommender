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
import com.example.mybackend.model.User;
import com.example.mybackend.repository.GroupRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class GroupService {

    private static final Logger logger = LoggerFactory.getLogger(GroupService.class);

    private final Driver driver;

    public GroupService(Driver driver){
        this.driver = driver;
    }

    public void createGroup(String groupName, String email, String audience, String privated, boolean isClosed, String tripType, LocalDate departureDate, LocalDate returnDate, List<LocalDate> availabilityStartDates, List<LocalDate> availabilityEndDates) {
        try (Session session = driver.session()) {
            session.executeWrite(tx -> {
                if ("private".equals(privated)) {
                    tx.run("CREATE (g:Group {name: $groupName, email: $email, audience: $audience, privated: $privated, isClosed: false, isClosedVoting: false, tripType: $tripType})", Map.of("groupName", groupName, "email", email, "audience", audience, "privated", privated, "isClosed", isClosed, "tripType", tripType));
                    tx.run("MATCH (u:User {email: $email}), (g:Group {name: $groupName}) " +
                           "CREATE (u)-[r:PERTENECE_A]->(g) " +
                           "SET r.preference = $preference, r.availabilityStartDates = $availabilityStartDates, r.availabilityEndDates = $availabilityEndDates",
                           Map.of("email", email, "groupName", groupName, "preference", tripType,
                                  "availabilityStartDates", availabilityStartDates, "availabilityEndDates", availabilityEndDates));
                } else {
                    tx.run("CREATE (g:Group {name: $groupName, email: $email, audience: $audience, privated: $privated, isClosed: false, isClosedVoting: false, tripType: $tripType, departureDate: $departureDate, returnDate: $returnDate})", Map.of("groupName", groupName, "email", email, "audience", audience, "privated", privated, "isClosed", isClosed, "tripType", tripType, "departureDate", departureDate, "returnDate", returnDate));
                    tx.run("MATCH (u:User {email: $email}), (g:Group {name: $groupName}) " +
                           "CREATE (u)-[r:PERTENECE_A]->(g) " +
                           "SET r.preference = $preference",
                           Map.of("email", email, "groupName", groupName, "preference", tripType));
                }
                return null;
            });
        } catch (Neo4jException e) {
            System.err.println("Error creando el grupo: " + e.getMessage());
            throw new RuntimeException("Error creando el grupo: " + e.getMessage(), e);
        }catch (Exception e) {
            System.err.println("Error general creando el grupo: " + e.getMessage());
            throw new RuntimeException("Error general creando el grupo: " + e.getMessage(), e);
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

    public void closeVoting(String groupName) {
        try (Session session = driver.session()) {
            session.executeWrite(tx -> {
                tx.run(
                    "MATCH (g:Group {name: $groupName}) SET g.isVotingClosed = true", 
                    Map.of("groupName", groupName)
                );
                return null;
            });
        } catch (Neo4jException e) {
            throw new RuntimeException("Error cerrando la votación", e);
        }
    }

    public void joinGroupWithPreferences(String groupName, String userEmail, String preference, List<LocalDate> availabilityStartDates, List<LocalDate> availabilityEndDates) {
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
                    "SET r.preference = $preference, r.availabilityStartDates = $availabilityStartDates, r.availabilityEndDates = $availabilityEndDates",
                    Map.of("email", userEmail, "name", groupName, "preference", preference, "availabilityStartDates", availabilityStartDates, "availabilityEndDates", availabilityEndDates ));
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
                    "RETURN g.name AS groupName, g.departureDate AS departureDate, " +
                    "g.returnDate AS returnDate, r.preference AS preference, " +
                    "g.isClosed AS isClosed, g.email AS email, id(g) AS groupId, g.privated AS privated",
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
                    groupData.put("privated", record.get("privated").asString());

                    // Manejo seguro de fechas
                    groupData.put(
                        "departureDate", 
                        record.get("departureDate").isNull() ? null : record.get("departureDate").asLocalDate()
                    );
                    groupData.put(
                        "returnDate", 
                        record.get("returnDate").isNull() ? null : record.get("returnDate").asLocalDate()
                    );
    
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
                    LocalDate departureDate = groupNode.get("departureDate").asLocalDate();
                    LocalDate arrivalDate = groupNode.get("returnDate").asLocalDate();
                    
                    group.setDepartureDate(departureDate);
                    group.setReturnDate(arrivalDate);
            
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
                    if (groupNode.containsKey("departureDate")) {
                        group.setDepartureDate(groupNode.get("departureDate").asLocalDate());
                    }
                    if (groupNode.containsKey("returnDate")) {
                        group.setReturnDate(groupNode.get("returnDate").asLocalDate());
                    }
                    publicGroups.add(group);
                }
                return publicGroups;
            });
        } catch (Neo4jException e) {
            throw new RuntimeException("Error retrieving public groups", e);
        }
    }

    public List<Group> getGroupsByTheme(String triptype) {
        try (Session session = driver.session()) {
            return session.executeRead(tx -> {
                var result = tx.run(
                    "MATCH (g:Group {tripType: $triptype}) " +
                    "RETURN g",
                    Map.of("triptype", triptype)
                );
    
                List<Group> groups = new ArrayList<>();
                while (result.hasNext()) {
                    var record = result.next();
                    var groupNode = record.get("g").asNode();
    
                    Group group = new Group();
                    group.setName(groupNode.get("name").asString());
                    group.setAudience(groupNode.get("audience").asString());
                    group.setPrivated(groupNode.get("privated").asString());
                    if (groupNode.containsKey("departureDate")) {
                        group.setDepartureDate(groupNode.get("departureDate").asLocalDate());
                    }
                    if (groupNode.containsKey("returnDate")) {
                        group.setReturnDate(groupNode.get("returnDate").asLocalDate());
                    }
                    group.setType(groupNode.get("tripType").asString());
                    groups.add(group);
                }
                return groups;
            });
        } catch (Neo4jException e) {
            throw new RuntimeException("Error retrieving groups by theme", e);
        }
    }
    
    public List<Group> getGroupsByAudience(String audience) {
        try (Session session = driver.session()) {
            return session.executeRead(tx -> {
                var result = tx.run(
                    "MATCH (g:Group {audience: $audience}) " +
                    "RETURN g",
                    Map.of("audience", audience)
                );
    
                List<Group> groups = new ArrayList<>();
                while (result.hasNext()) {
                    var record = result.next();
                    var groupNode = record.get("g").asNode();
    
                    Group group = new Group();
                    group.setName(groupNode.get("name").asString());
                    group.setAudience(groupNode.get("audience").asString());
                    group.setPrivated(groupNode.get("privated").asString());
                    if (groupNode.containsKey("departureDate")) {
                        group.setDepartureDate(groupNode.get("departureDate").asLocalDate());
                    }
                    if (groupNode.containsKey("returnDate")) {
                        group.setReturnDate(groupNode.get("returnDate").asLocalDate());
                    }
                    group.setType(groupNode.get("tripType").asString());
                    groups.add(group);
                }
                return groups;
            });
        } catch (Neo4jException e) {
            throw new RuntimeException("Error retrieving groups by audience", e);
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
    
    public void recommendDateUsingSlidingWindow(String groupName) {
        try (Session session = driver.session()) {
            // Obtener las disponibilidades de los usuarios
            List<Map<String, Object>> userAvailabilities = session.executeRead(tx -> {
                Result result = tx.run(
                    "MATCH (u:User)-[r:PERTENECE_A]->(g:Group {name: $groupName}) " +
                    "RETURN r.availabilityStartDates AS startDates, r.availabilityEndDates AS endDates",
                    Map.of("groupName", groupName)
                );
    
                List<Map<String, Object>> availabilities = new ArrayList<>();
                while (result.hasNext()) {
                    var record = result.next();
                    List<LocalDate> startDates = record.get("startDates").asList(value -> value.asLocalDate());
                    List<LocalDate> endDates = record.get("endDates").asList(value -> value.asLocalDate());
                    logger.info("Start dates retrieved: {}", startDates);
                    logger.info("End dates retrieved: {}", endDates);
                    availabilities.add(Map.of("startDates", startDates, "endDates", endDates));
                }
                return availabilities;
            });
    
            // Obtener el rango completo de fechas
            LocalDate minStartDate = userAvailabilities.stream()
                .flatMap(avail -> ((List<LocalDate>) avail.get("startDates")).stream())
                .min(LocalDate::compareTo)
                .orElseThrow(() -> new RuntimeException("No start dates found"));
    
            LocalDate maxEndDate = userAvailabilities.stream()
                .flatMap(avail -> ((List<LocalDate>) avail.get("endDates")).stream())
                .max(LocalDate::compareTo)
                .orElseThrow(() -> new RuntimeException("No end dates found"));
    
            int totalDays = (int) ChronoUnit.DAYS.between(minStartDate, maxEndDate) + 1;
            List<int[]> userAvailabilityArrays = new ArrayList<>();
    
            for (Map<String, Object> userAvail : userAvailabilities) {
                int[] availabilityArray = new int[totalDays];
                List<LocalDate> startDates = (List<LocalDate>) userAvail.get("startDates");
                List<LocalDate> endDates = (List<LocalDate>) userAvail.get("endDates");
    
                for (int i = 0; i < startDates.size(); i++) {
                    LocalDate startDate = startDates.get(i);
                    LocalDate endDate = endDates.get(i);
    
                    int startIndex = (int) ChronoUnit.DAYS.between(minStartDate, startDate);
                    int endIndex = (int) ChronoUnit.DAYS.between(minStartDate, endDate);
    
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
            session.executeWrite(tx -> {
                tx.run("MATCH (g:Group {name: $groupName}) " +
                    "SET g.departureDate = $departureDate, g.returnDate = $returnDate",
                    Map.of("groupName", groupName,
                            "departureDate", bestStartDate,
                            "returnDate", bestEndDate));
                logger.info("Fechas actualizadas correctamente en la base de datos para el grupo: {}", groupName);
                return null;
            });
    
        } catch (Neo4jException e) {
            throw new RuntimeException("Error recommending date for group", e);
        }
    } 
}