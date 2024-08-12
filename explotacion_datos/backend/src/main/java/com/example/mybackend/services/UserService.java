package com.example.mybackend.services;

import com.example.mybackend.model.Group;
import com.example.mybackend.model.User;
import com.example.mybackend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public User createUser(User user) {
        // Aquí deberías agregar la lógica para guardar el usuario en Neo4j
        // return userRepository.save(user);
        return user; // Modifica esto para que funcione con tu repositorio
    }
    
    public User saveUser(User user) {
        return userRepository.save(user);
    }

    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public void addUserToGroup(User user, Group group) {
        user.addGroup(group);
        userRepository.save(user);
    }
}