package com.busreservation.dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import com.busreservation.DBConnection;
import com.busreservation.model.Bus;

public class BusDAO {

    public boolean addBus(Bus bus) {
        String sql = "INSERT INTO buses (bus_no, bus_name, source, destination, total_seats, available_seats) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, bus.getBusNo());
            ps.setString(2, bus.getBusName());
            ps.setString(3, bus.getSource());
            ps.setString(4, bus.getDestination());
            ps.setInt(5, bus.getTotalSeats());
            ps.setInt(6, bus.getTotalSeats());

            return ps.executeUpdate() > 0;

        } catch (SQLIntegrityConstraintViolationException e) {
            System.out.println("Bus already exists.");
        } catch (SQLException e) {
            System.out.println("Add bus error: " + e.getMessage());
        }
        return false;
    }

    public List<Bus> getAllBuses() {
        List<Bus> buses = new ArrayList<>();
        String sql = "SELECT * FROM buses ORDER BY id";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                buses.add(mapBus(rs));
            }

        } catch (SQLException e) {
            System.out.println("Fetch error: " + e.getMessage());
        }
        return buses;
    }

    public List<Bus> searchBuses(String source, String destination) {
        List<Bus> buses = new ArrayList<>();
        String sql = "SELECT * FROM buses WHERE LOWER(source)=LOWER(?) AND LOWER(destination)=LOWER(?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, source);
            ps.setString(2, destination);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                buses.add(mapBus(rs));
            }

        } catch (SQLException e) {
            System.out.println("Search error: " + e.getMessage());
        }
        return buses;
    }

    public Bus getBusById(int busId) {
        String sql = "SELECT * FROM buses WHERE id=?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, busId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return mapBus(rs);
            }

        } catch (SQLException e) {
            System.out.println("Get bus error: " + e.getMessage());
        }

        return null;
    }

    public boolean decreaseSeat(int busId) {
        String sql = "UPDATE buses SET available_seats = available_seats - 1 WHERE id=? AND available_seats>0";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, busId);
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Decrease seat error: " + e.getMessage());
        }
        return false;
    }

    public boolean increaseSeat(int busId) {
        String sql = "UPDATE buses SET available_seats = available_seats + 1 WHERE id=? AND available_seats < total_seats";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, busId);
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Increase seat error: " + e.getMessage());
        }
        return false;
    }

    public int getNextAvailableSeat(int busId) {
        String sql = "SELECT COALESCE(MAX(seat_no),0)+1 AS next_seat FROM bookings WHERE bus_id=?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, busId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return rs.getInt("next_seat");
            }

        } catch (SQLException e) {
            System.out.println("Seat error: " + e.getMessage());
        }

        return 1;
    }

    private Bus mapBus(ResultSet rs) throws SQLException {
        return new Bus(
            rs.getInt("id"),
            rs.getString("bus_no"),
            rs.getString("bus_name"),
            rs.getString("source"),
            rs.getString("destination"),
            rs.getInt("total_seats"),
            rs.getInt("available_seats")
        );
    }
}