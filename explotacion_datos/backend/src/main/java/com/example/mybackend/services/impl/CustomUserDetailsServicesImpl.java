package com.example.mybackend.services.impl;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.mybackend.model.User;
import com.example.mybackend.repository.UserRepository;

/**
 * CustomUserDetailsService is a service that provides custom user details.
 */
@Service
public class CustomUserDetailsServicesImpl implements UserDetailsService{
    @Autowired
    private UserRepository userRepository; 

    /**
     * Loads a user by its email.
     *
     * @param email the email of the user to load
     * @return the user details
     * @throws UsernameNotFoundException if the user is not found
     */
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email);
        if (user == null) {
            throw new UsernameNotFoundException("Usuario no encontrado con el email: " + email);
        }
        
        return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(), new ArrayList<>());
    }
}
