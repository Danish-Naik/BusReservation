package com.busreservation.dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import com.busreservation.DBConnection;
import com.busreservation.model.Booking;

public class BookingDAO {

    public boolean bookSeat(Booking booking) {
        String sql = "INSERT INTO bookings (user_id, bus_id, seat_no, booking_date) VALUES (?, ?, ?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, booking.getUserId());
            ps.setInt(2, booking.getBusId());
            ps.setInt(3, booking.getSeatNo());
            ps.setDate(4, Date.valueOf(booking.getBookingDate()));

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Booking failed: " + e.getMessage());
        }
        return false;
    }

    public boolean cancelBooking(int bookingId, int userId) {
        String sql = "DELETE FROM bookings WHERE id=? AND user_id=?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, bookingId);
            ps.setInt(2, userId);

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Cancel failed: " + e.getMessage());
        }
        return false;
    }

    public Booking getBookingById(int bookingId) {
        String sql = "SELECT * FROM bookings WHERE id=?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, bookingId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return new Booking(
                    rs.getInt("id"),
                    rs.getInt("user_id"),
                    rs.getInt("bus_id"),
                    rs.getInt("seat_no"),
                    rs.getDate("booking_date").toLocalDate()
                );
            }

        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
        return null;
    }

    public List<Booking> getBookingsByUser(int userId) {
        List<Booking> list = new ArrayList<>();

        String sql = "SELECT b.*, bu.bus_no, bu.bus_name, bu.source, bu.destination " +
                     "FROM bookings b JOIN buses bu ON b.bus_id = bu.id WHERE b.user_id=?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Booking b = new Booking(
                    rs.getInt("id"),
                    rs.getInt("user_id"),
                    rs.getInt("bus_id"),
                    rs.getInt("seat_no"),
                    rs.getDate("booking_date").toLocalDate()
                );

                b.setBusNo(rs.getString("bus_no"));
                b.setBusName(rs.getString("bus_name"));
                b.setSource(rs.getString("source"));
                b.setDestination(rs.getString("destination"));

                list.add(b);
            }

        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }

        return list;
    }

    public List<Booking> getAllBookings() {
        List<Booking> list = new ArrayList<>();

        String sql = "SELECT b.*, u.name AS user_name, bu.bus_no, bu.bus_name, bu.source, bu.destination " +
                     "FROM bookings b " +
                     "JOIN users u ON b.user_id=u.id " +
                     "JOIN buses bu ON b.bus_id=bu.id";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Booking b = new Booking(
                    rs.getInt("id"),
                    rs.getInt("user_id"),
                    rs.getInt("bus_id"),
                    rs.getInt("seat_no"),
                    rs.getDate("booking_date").toLocalDate()
                );

                b.setUserName(rs.getString("user_name"));
                b.setBusNo(rs.getString("bus_no"));
                b.setBusName(rs.getString("bus_name"));
                b.setSource(rs.getString("source"));
                b.setDestination(rs.getString("destination"));

                list.add(b);
            }

        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }

        return list;
    }

    public boolean isSeatBooked(int busId, int seatNo) {
        String sql = "SELECT id FROM bookings WHERE bus_id=? AND seat_no=?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, busId);
            ps.setInt(2, seatNo);

            return ps.executeQuery().next();

        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }

        return false;
    }
}