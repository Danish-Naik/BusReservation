package com.busreservation.model;

public class Bus {
    private int id;
    private String busNo;
    private String busName;
    private String source;
    private String destination;
    private int totalSeats;
    private int availableSeats;

    // Constructors
    public Bus() {}

    public Bus(String busNo, String busName, String source, String destination, int totalSeats) {
        this.busNo = busNo;
        this.busName = busName;
        this.source = source;
        this.destination = destination;
        this.totalSeats = totalSeats;
        this.availableSeats = totalSeats;
    }

    public Bus(int id, String busNo, String busName, String source, String destination,
               int totalSeats, int availableSeats) {
        this.id = id;
        this.busNo = busNo;
        this.busName = busName;
        this.source = source;
        this.destination = destination;
        this.totalSeats = totalSeats;
        this.availableSeats = availableSeats;
    }

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getBusNo() { return busNo; }
    public void setBusNo(String busNo) { this.busNo = busNo; }

    public String getBusName() { return busName; }
    public void setBusName(String busName) { this.busName = busName; }

    public String getSource() { return source; }
    public void setSource(String source) { this.source = source; }

    public String getDestination() { return destination; }
    public void setDestination(String destination) { this.destination = destination; }

    public int getTotalSeats() { return totalSeats; }
    public void setTotalSeats(int totalSeats) { this.totalSeats = totalSeats; }

    public int getAvailableSeats() { return availableSeats; }
    public void setAvailableSeats(int availableSeats) { this.availableSeats = availableSeats; }

    @Override
    public String toString() {
        return String.format(
            "| %-4d | %-8s | %-20s | %-12s | %-12s | %-6d | %-6d |",
            id, busNo, busName, source, destination, totalSeats, availableSeats
        );
    }
}