package com.example.bookmyclass.repository;

import com.example.bookmyclass.entity.Room;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.List;
public interface RoomRepository extends JpaRepository<Room, Long> {

    // Find a room by name (optional if names are unique)
    Optional<Room> findByName(String name);

    // Find all rooms that are currently available
    List<Room> findByIsAvailableTrue();

    // Custom query to check availability for a specific room
    Optional<Room> findByIdAndIsAvailableTrue(Long id);
}
