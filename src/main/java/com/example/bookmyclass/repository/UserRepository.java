package com.example.bookmyclass.repository;

import com.example.bookmyclass.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findById(Long id);  // Updated to return Optional<User> for handling null cases.
    User findByUsername(String username);  // Your existing method to find by username
}
