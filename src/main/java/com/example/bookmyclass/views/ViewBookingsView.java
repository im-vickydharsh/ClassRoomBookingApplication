package com.example.bookmyclass.views;

import com.example.bookmyclass.entity.Booking;
import com.example.bookmyclass.entity.Room;
import com.example.bookmyclass.repository.BookingRepository;
import com.example.bookmyclass.repository.RoomRepository;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Route("view-bookings")
public class ViewBookingsView extends VerticalLayout {

    private final BookingRepository bookingRepository;
    private final RoomRepository roomRepository;

    @Autowired
    public ViewBookingsView(BookingRepository bookingRepository, RoomRepository roomRepository) {
        this.bookingRepository = bookingRepository;
        this.roomRepository = roomRepository;

        setSizeFull();
        setDefaultHorizontalComponentAlignment(Alignment.CENTER);
        
        // Set background style without CSS
        getStyle().set("background-image", "url('images/background33.jpg')")
                  .set("background-size", "cover")
                  .set("background-position", "center");

        // Add a title label with styles (bold, dark colors, and uppercase)
        Label titleLabel = new Label("BOOKINGS DONE ");
        titleLabel.getStyle()
                  .set("font-size", "36px")
                  .set("color", "#212121") // Dark color (dark gray)
                  .set("font-weight", "bold")
                  .set("text-transform", "uppercase") // Make text uppercase
                  .set("padding-bottom", "20px");

        // Add a back button to navigate back to the dashboard
        Button backButton = new Button("BACK TO DASHBOARD", e -> getUI().ifPresent(ui -> ui.navigate("student-dashboard")));
        backButton.getStyle()
                  .set("background-color", "#1D3557") // Dark blue background
                  .set("color", "white")
                  .set("border", "none")
                  .set("padding", "12px 24px")
                  .set("border-radius", "8px")
                  .set("font-size", "16px")
                  .set("cursor", "pointer")
                  .set("text-transform", "uppercase"); // Make button text uppercase

        // Display bookings with each booking in its own layout
        VerticalLayout bookingListLayout = new VerticalLayout();
        bookingListLayout.setSpacing(true);
        bookingListLayout.setPadding(true);

        loadBookings(bookingListLayout);

        // Add components to the layout
        add(titleLabel, backButton, bookingListLayout);
    }

    private void loadBookings(VerticalLayout bookingListLayout) {
        List<Booking> bookings = bookingRepository.findAll(); // Retrieve all bookings (or filter for the logged-in user)
        bookingListLayout.removeAll();

        for (Booking booking : bookings) {
            Room room = roomRepository.findById(booking.getRoomId()).orElse(null);

            if (room != null) {
                // Display booking details
                VerticalLayout bookingLayout = new VerticalLayout();
                bookingLayout.setSpacing(true);

                // Display booking information with light colors, bold text, and uppercase
                Label bookingInfoLabel = new Label("ROOM " + room.getId() + " - " + room.getCapacity() + " CAPACITY");
                bookingInfoLabel.getStyle()
                                .set("font-size", "18px")
                                .set("color", "#FFFFFF") // Light color
                                .set("font-weight", "bold")
                                .set("text-transform", "uppercase");

                Label bookingDateLabel = new Label("BOOKING DATE: " + booking.getBookingDateTime());
                bookingDateLabel.getStyle()
                                .set("font-size", "16px")
                                .set("color", "#F5F5F5") // Light color
                                .set("font-weight", "bold")
                                .set("text-transform", "uppercase");

                Label bookingEndTimeLabel = new Label("END TIME: " + booking.getEndTime());
                bookingEndTimeLabel.getStyle()
                                   .set("font-size", "16px")
                                   .set("color", "#F5F5F5") // Light color
                                   .set("font-weight", "bold")
                                   .set("text-transform", "uppercase");

                Label bookingDurationLabel = new Label("DURATION: " + booking.getDurationInHours() + " HOURS");
                bookingDurationLabel.getStyle()
                                    .set("font-size", "16px")
                                    .set("color", "#F5F5F5") // Light color
                                    .set("font-weight", "bold")
                                    .set("text-transform", "uppercase");

                bookingLayout.add(bookingInfoLabel, bookingDateLabel, bookingEndTimeLabel, bookingDurationLabel);
                bookingListLayout.add(bookingLayout);
            }

        }
    }
}
