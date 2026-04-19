package com.busreservation.dao;

import java.sql.*;
import com.busreservation.DBConnection;
import com.busreservation.model.User;

public class UserDAO {

    // Register a new user
    public boolean registerUser(User user) {
        String sql = "INSERT INTO users (name, email, password) VALUES (?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, user.getName());
            ps.setString(2, user.getEmail());
            ps.setString(3, user.getPassword());

            int rows = ps.executeUpdate();
            return rows > 0;

        } catch (SQLIntegrityConstraintViolationException e) {
            System.out.println("  [Error] Email already registered. Please use a different email.");
        } catch (SQLException e) {
            System.out.println("  [Error] Registration failed: " + e.getMessage());
        }
        return false;
    }

    // Login: validate email and password, return User if valid
    public User loginUser(String email, String password) {
        String sql = "SELECT * FROM users WHERE email = ? AND password = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, email);
            ps.setString(2, password);

            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return new User(
                    rs.getInt("id"),
                    rs.getString("name"),
                    rs.getString("email"),
                    rs.getString("password")
                );
            }

        } catch (SQLException e) {
            System.out.println("  [Error] Login failed: " + e.getMessage());
        }
        return null; // Return null if login fails
    }

    // Check if email already exists
    public boolean emailExists(String email) {
        String sql = "SELECT id FROM users WHERE email = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, email);
            ResultSet rs = ps.executeQuery();
            return rs.next();

        } catch (SQLException e) {
            System.out.println("  [Error] " + e.getMessage());
        }
        return false;
    }

    // Get user by ID
    public User getUserById(int userId) {
        String sql = "SELECT * FROM users WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return new User(
                    rs.getInt("id"),
                    rs.getString("name"),
                    rs.getString("email"),
                    rs.getString("password")
                );
            }

        } catch (SQLException e) {
            System.out.println("  [Error] " + e.getMessage());
        }
        return null;
    }
}

