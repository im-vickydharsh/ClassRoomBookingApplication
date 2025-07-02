package com.example.bookmyclass;

import com.example.bookmyclass.entity.User;
import com.example.bookmyclass.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class BookMyClassApplication {

    public static void main(String[] args) {
        SpringApplication.run(BookMyClassApplication.class, args);
    }

    @Bean
    CommandLineRunner initData(UserRepository userRepository) {
        return args -> {
            // Create default users for testing
            userRepository.save(new User("student1", "ceg@001", "STUDENT"));
            userRepository.save(new User("approver1", "admin@123", "APPROVER"));
            userRepository.save(new User("student2", "ceg@002", "STUDENT"));
        };
    }
}
