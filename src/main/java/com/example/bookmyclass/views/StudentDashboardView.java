package com.example.bookmyclass.views;

import com.example.bookmyclass.entity.Room;
import com.example.bookmyclass.repository.RoomRepository;
import com.example.bookmyclass.service.BookingService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

@Route("student-dashboard")
public class StudentDashboardView extends VerticalLayout {

    private final RoomRepository roomRepository;
    private final BookingService bookingService;

    private final Map<Long, Button> cancelButtons = new HashMap<>();

    public StudentDashboardView(RoomRepository roomRepository, BookingService bookingService) {
        this.roomRepository = roomRepository;
        this.bookingService = bookingService;

        // Set the view to take the full height of the viewport
        setSizeFull();
        getStyle()
                .set("background-image", "url('images/background3.jpg')")
                .set("background-size", "cover")
                .set("background-position", "center")
                .set("background-repeat", "no-repeat")
                .set("color", "white");

        Label dashboardLabel = new Label("CLASSROOMS");
        dashboardLabel.getStyle()
                .set("font-size", "35px")
                .set("font-weight", "bold")
                .set("text-align", "center")
                .set("margin-bottom", "20px");

        VerticalLayout roomGrid = new VerticalLayout();
        roomGrid.setSpacing(true);
        roomGrid.setPadding(true);
        roomGrid.setWidthFull();

        loadRooms(roomGrid);

        Label bookHereLabel = new Label("Book Here!");
        bookHereLabel.getStyle()
                .set("font-size", "30px")
                .set("color", "darkblue")
                .set("font-weight", "bold")
                .set("text-align", "center");

        TextField roomIdField = new TextField("Room ID");
        DatePicker dateField = new DatePicker("Date");
        TextField durationField = new TextField("Duration (hours)");
        
        // Create a formatter for the current date and time
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEEE, MMM dd, yyyy HH:mm:ss");

        // Fetch the current date and time
        LocalDateTime now = LocalDateTime.now();
        String formattedDateTime = now.format(formatter);

        // Replace "XXX" with the user's name if available
        String userName = "STUDENT"; // Replace this with dynamic user retrieval if available

        // Welcome message with current date and time
        Label welcomeLabel = new Label("Welcome, " + userName + "! Current Date and Time: " + formattedDateTime);
        welcomeLabel.getStyle()
                .set("font-size", "24px")
                .set("font-weight", "bold")
                .set("text-align", "center")
                .set("margin-bottom", "20px");

        // Add the welcome label at the top of the layout
        add(welcomeLabel);

        Button bookRoomButton = new Button("Book Room", e -> bookRoom(roomIdField, dateField, durationField, roomGrid));
        bookRoomButton.getStyle()
                .set("cursor", "pointer")
                .set("background-color", "#4CAF50")
                .set("color", "white")
                .set("border", "none")
                .set("padding", "10px 20px")
                .set("border-radius", "15px")
                .set("font-size", "16px");

        Button viewBookingsButton = new Button("View Bookings", e -> {
            // Navigate to the bookings view
            getUI().ifPresent(ui -> ui.navigate("view-bookings"));
        });
        viewBookingsButton.getStyle()
                .set("cursor", "pointer")
                .set("background-color", "#2196F3")
                .set("color", "white")
                .set("border", "none")
                .set("padding", "10px 20px")
                .set("border-radius", "15px")
                .set("font-size", "16px");

        VerticalLayout bookingFormLayout = new VerticalLayout(
                bookHereLabel, roomIdField, dateField, durationField, bookRoomButton, viewBookingsButton
        );
        bookingFormLayout.setAlignItems(Alignment.CENTER);
        bookingFormLayout.setSpacing(true);

        VerticalLayout centerLayout = new VerticalLayout(bookingFormLayout);
        centerLayout.setSizeFull();
        centerLayout.setAlignItems(Alignment.CENTER);
        centerLayout.setJustifyContentMode(JustifyContentMode.CENTER);

        VerticalLayout roomDetailsLayout = new VerticalLayout(dashboardLabel, roomGrid);
        roomDetailsLayout.setSizeFull();

        HorizontalLayout mainLayout = new HorizontalLayout(roomDetailsLayout, centerLayout);
        mainLayout.setSizeFull();
        mainLayout.setAlignItems(Alignment.START);
        mainLayout.setJustifyContentMode(JustifyContentMode.START);

        // Enable scrolling for overflow
        getStyle().set("overflow", "auto");

        add(mainLayout);
    }

    private void loadRooms(VerticalLayout roomGrid) {
        List<Room> rooms = roomRepository.findAll();
        roomGrid.removeAll();

        for (Room room : rooms) {
            HorizontalLayout roomLayout = new HorizontalLayout();
            roomLayout.setWidth("100%");
            roomLayout.setSpacing(true);

            Label roomLabel = new Label("Room " + room.getId() + " - Capacity: " + room.getCapacity());
            setRoomStatusColor(room, roomLabel);

            roomLabel.getStyle()
                    .set("padding", "20px")
                    .set("margin", "10px")
                    .set("border-radius", "10px")
                    .set("background-color", "lightyellow")
                    .set("box-shadow", "2px 2px 10px rgba(0, 0, 0, 0.1)")
                    .set("flex-grow", "1")
                    .set("text-align", "center")
                    .set("font-weight", "bold")
                    .set("text-transform", "uppercase")
                    .set("color", "black");

            Button cancelButton = null;
            if (bookingService.isRoomBooked(room.getId())) {
                cancelButton = new Button("Cancel", e -> cancelBooking(room.getId(), roomGrid));
                cancelButton.getStyle()
                        .set("background-color", "red")
                        .set("color", "white")
                        .set("border-radius", "50px")
                        .set("padding", "10px 20px")
                        .set("font-size", "14px");
                cancelButtons.put(room.getId(), cancelButton);
            }

            if (cancelButton != null) {
                roomLayout.add(roomLabel, cancelButton);
            } else {
                roomLayout.add(roomLabel);
            }

            roomGrid.add(roomLayout);
        }
    }

    private void setRoomStatusColor(Room room, Label roomLabel) {
        if (room.isAvailable()) {
            roomLabel.getStyle().set("border", "3px solid green");
        } else if (bookingService.isRoomBooked(room.getId())) {
            roomLabel.getStyle().set("border", "3px solid yellow");
        } else {
            roomLabel.getStyle().set("border", "3px solid red");
        }
    }

    private void bookRoom(TextField roomIdField, DatePicker dateField, TextField durationField, VerticalLayout roomGrid) {
        try {
            Long roomId = Long.parseLong(roomIdField.getValue());
            LocalDateTime bookingDateTime = dateField.getValue().atStartOfDay();
            int durationInHours = Integer.parseInt(durationField.getValue());

            Room room = roomRepository.findById(roomId)
                    .orElseThrow(() -> new IllegalArgumentException("Invalid Room ID"));

            if (bookingService.isRoomBooked(roomId)) {
                roomIdField.setErrorMessage("Room is already booked or waiting approval!");
                roomIdField.setInvalid(true);
                return;
            }

            bookingService.bookRoom(roomId, bookingDateTime, durationInHours);

            updateRoomInGrid(roomId, roomGrid);
        } catch (Exception ex) {
            roomIdField.setErrorMessage("Error: " + ex.getMessage());
            roomIdField.setInvalid(true);
        }
    }

    private void updateRoomInGrid(Long roomId, VerticalLayout roomGrid) {
        for (HorizontalLayout roomLayout : roomGrid.getChildren().filter(HorizontalLayout.class::isInstance)
                .map(HorizontalLayout.class::cast).toList()) {
            Label roomLabel = (Label) roomLayout.getComponentAt(0);

            if (roomLabel.getText().startsWith("Room " + roomId)) {
                Room room = roomRepository.findById(roomId).orElse(null);
                if (room != null) {
                    setRoomStatusColor(room, roomLabel);

                    if (!cancelButtons.containsKey(roomId)) {
                        Button cancelButton = new Button("Cancel", e -> cancelBooking(roomId, roomGrid));
                        cancelButton.getStyle()
                                .set("background-color", "red")
                                .set("color", "white")
                                .set("border-radius", "50px")
                                .set("padding", "10px 20px")
                                .set("font-size", "14px");
                        cancelButtons.put(roomId, cancelButton);

                        roomLayout.add(cancelButton);
                    }
                }
                break;
            }
        }
    }

    private void cancelBooking(Long roomId, VerticalLayout roomGrid) {
        try {
            bookingService.cancelBooking(roomId);
            updateRoomInGrid(roomId, roomGrid);
            cancelButtons.remove(roomId);
            loadRooms(roomGrid);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}