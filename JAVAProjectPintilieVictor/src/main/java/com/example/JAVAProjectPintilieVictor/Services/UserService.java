package com.example.JAVAProjectPintilieVictor.Services;

import com.example.JAVAProjectPintilieVictor.Entities.User;
import com.example.JAVAProjectPintilieVictor.Exceptions.UserNotFoundException;
import com.example.JAVAProjectPintilieVictor.Exceptions.UsernameAlreadyExistsException;
import com.example.JAVAProjectPintilieVictor.Repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;

import java.security.Principal;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;



    public User saveUser(User user) {
        String encodedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);
        return userRepository.save(user);
    }

    public User findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public User registerUser(User user) {
        if (userRepository.findByUsername(user.getUsername()) != null) {
                throw new UsernameAlreadyExistsException("Username is already taken.");
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
        }

        public User resetUserPassword(User user, String newPassword) {
            User existingUser = userRepository.findByUsername(user.getUsername());

            if (existingUser == null) {
                throw new UserNotFoundException("User not found.");
            }

            existingUser.setPassword(passwordEncoder.encode(newPassword));
            return userRepository.save(existingUser);
        }

    public Long getTotalUsers() {
        return userRepository.count();
    }

    public void deleteUser(User user) {
        userRepository.delete(user);
    }
}


