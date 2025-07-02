package com.example.bookmyclass.views;

import com.example.bookmyclass.entity.Booking;
import com.example.bookmyclass.entity.Room;
import com.example.bookmyclass.entity.User;
import com.example.bookmyclass.repository.BookingRepository;
import com.example.bookmyclass.repository.RoomRepository;
import com.example.bookmyclass.repository.UserRepository;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.component.notification.Notification;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Route("approver-dashboard")
public class ApproverDashboardView extends VerticalLayout {

    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;
    private final RoomRepository roomRepository; 

    public ApproverDashboardView(BookingRepository bookingRepository, UserRepository userRepository, RoomRepository roomRepository) {
        this.bookingRepository = bookingRepository;
        this.userRepository = userRepository;
        this.roomRepository = roomRepository;
        
        setSizeFull();
        getStyle().set("background-image", "url('images/background5.jpeg')");
        getStyle().set("background-size", "cover");
        getStyle().set("background-position", "center");

        // Title and layout
        H1 title = new H1("PENDING REQUESTS");
        title.getStyle().set("text-align", "center")
        				.set("color","white");
        add(title);
        setAlignItems(Alignment.CENTER);

        // Add the bookings dynamically
        addBookings();
    }

    Map<Long, VerticalLayout> bookingLayouts = new HashMap<>();

    private void addBookings() {
        // Fetch pending bookings
        List<Booking> bookings = bookingRepository.findByApproved(false);

        // Loop through each booking and create a simple column-based display
        for (Booking booking : bookings) {
            // Default to "Student1" if userId is null or user is not found
            String requesterName = "Student1";
            if (booking.getUserId() != null) {
                User requester = userRepository.findById(booking.getUserId()).orElse(null);
                if (requester != null) {
                    requesterName = requester.getUsername();
                }
            }

            String roomId = (booking.getRoomId() != null) ? booking.getRoomId().toString() : "Unknown";

            // Create a VerticalLayout for each booking's details (single column view)
            VerticalLayout bookingLayout = new VerticalLayout();
            bookingLayout.setWidthFull();
            bookingLayout.getStyle().set("padding", "10px");
            bookingLayout.getStyle().set("border-bottom", "1px solid #ccc");
            bookingLayout.getStyle().set("background-color", "rgba(255, 255, 255, 0.8)");

            // Store the layout for later use in rejection
            bookingLayouts.put(booking.getId(), bookingLayout);

            // Add booking details
            Text studentNameText = new Text("Student Name: " + requesterName);
            Text roomIdText = new Text("Room ID: " + roomId);
            bookingLayout.add(studentNameText, roomIdText);

            // Create Reject button
            Button rejectButton = new Button("Reject");
            rejectButton.getStyle().set("background-color", "red");  // Set the reject button to red
            rejectButton.getStyle().set("color", "white");  // Set text color to white for better visibility
            rejectButton.addClickListener(e -> {
                rejectBooking(booking);
                bookingLayout.getStyle().set("background-color", "green");
                Notification.show("Rejected", 3000, Notification.Position.MIDDLE);
            });

            // Create Accept button
            Button acceptButton = new Button("Accept");
            acceptButton.getStyle().set("background-color", "green");  // Set the accept button to green
            acceptButton.getStyle().set("color", "white");  // Set text color to white for better visibility
            acceptButton.addClickListener(e -> {
                approveBooking(booking);
                bookingLayout.getStyle().set("background-color", "lightblue");
                Notification.show("Accepted", 3000, Notification.Position.MIDDLE);
            });

            // Add both buttons to the layout
            bookingLayout.add(rejectButton, acceptButton);

            // Add the booking layout to the main layout
            add(bookingLayout);
        }
    }

    private void approveBooking(Booking booking) {
        // Calculate the end time based on the booking date and duration
        LocalDateTime bookingDateTime = booking.getBookingDateTime();
        int durationInHours = booking.getDurationInHours(); // Assuming you have a field for duration in Booking entity
        LocalDateTime endTime = bookingDateTime.plus(durationInHours, ChronoUnit.HOURS);

        // Set the end time and mark the booking as approved
        booking.setEndTime(endTime);
        booking.setApproved(true);
        bookingRepository.save(booking);

        // Fetch the room associated with the booking
        Room room = roomRepository.findById(booking.getRoomId()).orElseThrow(() -> new IllegalArgumentException("Room not found"));

        // Mark the room as unavailable (availability set to false)
        room.setAvailable(false);  // Room is no longer available after approval
        roomRepository.save(room);  // Save the updated room status

        // Find the associated booking layout for visual updates
        VerticalLayout bookingLayout = bookingLayouts.get(booking.getId());
        if (bookingLayout != null) {
            // Change the background color in the approver's view
            bookingLayout.getStyle().set("background-color", "lightblue");  // Mark it as approved with a visual change
            
            // Remove the booking layout from the main view immediately
            remove(bookingLayout);  // Remove the booking layout from the main layout
        }

        // Show a notification to the approver
        Notification.show("Booking Approved", 3000, Notification.Position.MIDDLE);
    }

    private void rejectBooking(Booking booking) {
        // Fetch the room associated with the booking
        Room room = roomRepository.findById(booking.getRoomId()).orElseThrow(() -> new IllegalArgumentException("Room not found"));

        // Mark the room as available again
        room.setAvailable(true);
        roomRepository.save(room); // Save the updated room state

        // Remove the booking from the database
        bookingRepository.delete(booking);

        // Find the associated booking layout for visual updates
        VerticalLayout bookingLayout = bookingLayouts.get(booking.getId());

        if (bookingLayout != null) {
            // Remove the booking layout from the main view immediately
            remove(bookingLayout);  // Remove the booking layout from the main layout
        }

        // Display a notification indicating the booking rejection and room availability
        Notification.show("Booking Rejected, Room is now Available", 3000, Notification.Position.MIDDLE);
    }

}
