package com.example.mybackend.services;

import com.example.mybackend.model.Group;
import com.example.mybackend.model.User;
import com.example.mybackend.repository.UserRepository;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public User createUser(User user) {
        return userRepository.save(user);
    }
    
    public User saveUser(User user) {
        return userRepository.save(user);
    }

    public User getUserByEmail(String email) {
        try (Session session = driver.session()) {
        return session.executeRead(tx -> {
            Result result = tx.run("MATCH (u:User {email: $email}) RETURN u",
                                    Map.of("email", email));
            if (result.hasNext()) {
                var record = result.next();
                return new User(record.get("u").asNode().get("name").asString(),
                                record.get("u").asNode().get("email").asString(),
                                record.get("u").asNode().get("password").asString());
            } else {
                return null;
            }
        });
    } userRepository.findByEmail(email);
    }

    public void addUserToGroup(User user, Group group) {
        user.addGroup(group);
        userRepository.save(user);
    }

    public boolean validateUser(String email, String password) {
        User user = getUserByEmail(email);
        if (user != null) {
            return password.equals(user.getPassword());
        }
        return false;
    }
}