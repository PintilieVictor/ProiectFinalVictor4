package com.example.JAVAProjectPintilieVictor.Repositories;

import com.example.JAVAProjectPintilieVictor.Entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String username);
}
