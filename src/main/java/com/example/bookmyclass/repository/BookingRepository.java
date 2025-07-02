package com.example.bookmyclass.repository;

import com.example.bookmyclass.entity.Booking;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {

    // Find all pending bookings (approved = false)
    List<Booking> findByApproved(boolean approved);

    // Custom query to find a booking by room ID and approval status
    List<Booking> findByRoomIdAndApproved(Long roomId, boolean approved);

    // Find bookings that are ending before a specific time
    List<Booking> findByEndTimeBefore(LocalDateTime time);

    // Find bookings that have not been notified
    List<Booking> findByNotificationSentFalse();
}
