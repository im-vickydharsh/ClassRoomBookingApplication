package com.example.bookmyclass.service;

import com.example.bookmyclass.entity.Booking;
import com.example.bookmyclass.repository.BookingRepository;
import com.example.bookmyclass.entity.Room;
import com.example.bookmyclass.repository.RoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
public class BookingService {

    private final BookingRepository bookingRepository;
    private final RoomRepository roomRepository;

    @Autowired
    public BookingService(BookingRepository bookingRepository, RoomRepository roomRepository) {
        this.bookingRepository = bookingRepository;
        this.roomRepository = roomRepository;
    }

    // Check if a room is booked (either pending or approved)
    public boolean isRoomBooked(Long roomId) {
        List<Booking> bookings = bookingRepository.findByRoomIdAndApproved(roomId, false);
        return !bookings.isEmpty(); // If there are any pending bookings
    }

    // Handle the room booking process with duration feature
    public void bookRoom(Long roomId, LocalDateTime bookingDateTime, int durationInHours) {
        Room room = roomRepository.findById(roomId).orElseThrow(() -> new IllegalArgumentException("Room not found"));

        // Check if the room is available
        if (!room.isAvailable()) {
            throw new IllegalStateException("Room is already booked or unavailable");
        }

        // Calculate the end time based on the duration (in hours)
        LocalDateTime endTime = bookingDateTime.plusHours(durationInHours);

        // Create a booking entry
        Booking booking = new Booking();
        booking.setRoomId(roomId);
        booking.setBookingDateTime(bookingDateTime); // Set the booking start time
        booking.setApproved(false); // Booking is pending approval
        booking.setEndTime(endTime); // Set the end time of the booking
        booking.setDurationInHours(durationInHours); // Store the duration
        bookingRepository.save(booking);

        // Mark the room as unavailable
        room.setAvailable(false);
        roomRepository.save(room);
    }

    // Handle canceling the room booking
    public void cancelBooking(Long roomId) {
        List<Booking> bookings = bookingRepository.findByRoomIdAndApproved(roomId, false);
        if (!bookings.isEmpty()) {
            Booking booking = bookings.get(0); // Assume one pending booking per room
            bookingRepository.delete(booking);

            // Mark the room as available again
            Room room = roomRepository.findById(roomId).orElseThrow(() -> new IllegalArgumentException("Room not found"));
            room.setAvailable(true);
            roomRepository.save(room);
        }
    }

    // Check if a booking exists for the user in the room
    public boolean isBookingByUser(Long roomId, Long userId) {
        // Find bookings by room ID and check if they are approved
        List<Booking> bookings = bookingRepository.findByRoomIdAndApproved(roomId, true);  // Approved bookings only

        // Iterate through the bookings to check if any booking belongs to the given user
        for (Booking booking : bookings) {
            if (booking.getUserId().equals(userId)) { // Compare with userId
                return true;  // Booking exists and belongs to the user
            }
        }

        // If no matching booking is found, return false
        return false;
    }

    // Check if the booking has expired and release the room
    public void checkAndReleaseRoom(Long roomId) {
        List<Booking> bookings = bookingRepository.findByRoomIdAndApproved(roomId, true); // Approved bookings
        for (Booking booking : bookings) {
            // Check if the current time has passed the end time
            if (LocalDateTime.now().isAfter(booking.getEndTime())) {
                // Release the room if booking time is over
                Room room = roomRepository.findById(roomId).orElseThrow(() -> new IllegalArgumentException("Room not found"));
                room.setAvailable(true);
                roomRepository.save(room);

                // Optionally, delete or archive the booking if needed
                bookingRepository.delete(booking);
            }
        }
    }
}
