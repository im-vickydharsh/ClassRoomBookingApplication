Classroom Booking Application using JAVA
# Classroom Booking Management System

A web-based application designed to streamline classroom booking in educational institutions. Developed by a team of five, this project emphasizes role-based access, real-time approval workflows, and admin-friendly dashboards.  

## 🔧 Technologies Used

- **Backend**: Spring Boot  
- **Frontend**: Vaadin  
- **Database**: H2 (in-memory)  
- **Scheduling**: Spring Task Scheduler  

## 👥 Roles and Access Control

The system supports three user roles:

- **Admin**: 
  - View and manage all bookings
  - Approve or reject booking requests
  - Access detailed booking reports and dashboards  
- **Faculty**: 
  - Request classroom bookings
  - View booking status and history  
- **Student**: 
  - Limited access; view approved bookings  

## ✅ Key Features

- **Authentication and Role-Based Access**:  
  Secured login with dynamic access restrictions based on user roles.

- **Booking and Approval Workflow**:  
  Faculty can request classroom bookings which require Admin approval. Each request includes details like room, date, and time.

- **Automated Booking Reset**:  
  Scheduled task resets bookings on a weekly/monthly basis to prevent stale data and ensure availability.

- **Admin Dashboard**:  
  Interactive dashboard with booking statistics, real-time requests, and system reports.

- **Conflict Management**:  
  Prevents double-booking with built-in conflict resolution logic.
