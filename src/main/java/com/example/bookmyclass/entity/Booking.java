package com.example.bookmyclass.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import java.time.LocalDateTime;

import com.vaadin.flow.component.orderedlayout.VerticalLayout;

@Entity
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long roomId;
    private Long userId; // Changed to Long to align with method parameters
    private LocalDateTime bookingDateTime;
    private int durationInHours; // New field to store booking duration
    private LocalDateTime endTime; // New field to store calculated end time
    private boolean approved; // Initially false, later updated by the approver
    private boolean notificationSent; // New field to track if notification is sent

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getRoomId() {
        return roomId;
    }

    public void setRoomId(Long roomId) {
        this.roomId = roomId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public LocalDateTime getBookingDateTime() {
        return bookingDateTime;
    }

    public void setBookingDateTime(LocalDateTime bookingDateTime) {
        this.bookingDateTime = bookingDateTime;
    }

    public int getDurationInHours() {
        return durationInHours;
    }

    public void setDurationInHours(int durationInHours) {
        this.durationInHours = durationInHours;
        this.endTime = bookingDateTime.plusHours(durationInHours); // Automatically calculate end time
    }
    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime; // Setter for endTime
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public boolean isApproved() {
        return approved;
    }

    public void setApproved(boolean approved) {
        this.approved = approved;
    }

    public boolean isNotificationSent() {
        return notificationSent;
    }

    public void setNotificationSent(boolean notificationSent) {
        this.notificationSent = notificationSent;
    }

	public VerticalLayout getParent() {
		// TODO Auto-generated method stub
		return null;
	}
}
