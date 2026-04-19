package com.busreservation.model;

import java.time.LocalDate;

public class Booking {
    private int id;
    private int userId;
    private int busId;
    private int seatNo;
    private LocalDate bookingDate;

    // Extra fields for display (joined from other tables)
    private String userName;
    private String busName;
    private String busNo;
    private String source;
    private String destination;

    // Constructors
    public Booking() {}

    public Booking(int userId, int busId, int seatNo) {
        this.userId = userId;
        this.busId = busId;
        this.seatNo = seatNo;
        this.bookingDate = LocalDate.now();
    }

    public Booking(int id, int userId, int busId, int seatNo, LocalDate bookingDate) {
        this.id = id;
        this.userId = userId;
        this.busId = busId;
        this.seatNo = seatNo;
        this.bookingDate = bookingDate;
    }

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }

    public int getBusId() { return busId; }
    public void setBusId(int busId) { this.busId = busId; }

    public int getSeatNo() { return seatNo; }
    public void setSeatNo(int seatNo) { this.seatNo = seatNo; }

    public LocalDate getBookingDate() { return bookingDate; }
    public void setBookingDate(LocalDate bookingDate) { this.bookingDate = bookingDate; }

    public String getUserName() { return userName; }
    public void setUserName(String userName) { this.userName = userName; }

    public String getBusName() { return busName; }
    public void setBusName(String busName) { this.busName = busName; }

    public String getBusNo() { return busNo; }
    public void setBusNo(String busNo) { this.busNo = busNo; }

    public String getSource() { return source; }
    public void setSource(String source) { this.source = source; }

    public String getDestination() { return destination; }
    public void setDestination(String destination) { this.destination = destination; }

    @Override
    public String toString() {
        return String.format(
            "| %-6d | %-8s | %-20s | %-12s | %-12s | %-6d | %-12s |",
            id, busNo, busName, source, destination, seatNo, bookingDate
        );
    }
}