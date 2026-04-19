package com.busreservation;

import java.util.Scanner;

import com.busreservation.dao.UserDAO;
import com.busreservation.dao.BusDAO;
import com.busreservation.dao.BookingDAO;
import com.busreservation.model.User;
import com.busreservation.model.Bus;
import com.busreservation.model.Booking;

public class Main {

    static Scanner sc = new Scanner(System.in);

    static UserDAO userDAO = new UserDAO();
    static BusDAO busDAO = new BusDAO();
    static BookingDAO bookingDAO = new BookingDAO();

    public static void main(String[] args) {

        System.out.println("\tBUS RESERVATION SYSTEM\t");

        while (true) {

            System.out.println("\n1. Register User");
            System.out.println("2. Login User");
            System.out.println("3. Admin Login");
            System.out.println("4. View All Buses");
            System.out.println("5. Exit");

            System.out.print("Enter choice: ");
            int choice = sc.nextInt();
            sc.nextLine();

            switch (choice) {

                case 1:
                    registerUser();
                    break;

                case 2:
                    loginUser();
                    break;

                case 3:
                    adminLogin();
                    break;

                case 4:
                    viewBuses();
                    break;

                case 5:
                    System.out.println("Exiting system...");
                    return;

                default:
                    System.out.println("Invalid choice!");
            }
        }
    }

    // ================= REGISTER USER =================
    static void registerUser() {

        System.out.println("\n--- USER REGISTRATION ---");

        System.out.print("Enter Name: ");
        String name = sc.nextLine();

        System.out.print("Enter Email: ");
        String email = sc.nextLine();

        System.out.print("Enter Password: ");
        String password = sc.nextLine();

        User user = new User(name, email, password);

        boolean result = userDAO.registerUser(user);

        if (result) {
            System.out.println("User registered successfully!");
        } else {
            System.out.println("Registration failed!");
        }
    }

    // ================= LOGIN USER =================
    static void loginUser() {

        System.out.println("\n--- USER LOGIN ---");

        System.out.print("Enter Email: ");
        String email = sc.nextLine();

        System.out.print("Enter Password: ");
        String password = sc.nextLine();

        User user = userDAO.loginUser(email, password);

        if (user != null) {
            System.out.println("Login successful! Welcome " + user.getName());
            userDashboard(user);
        } else {
            System.out.println("Invalid credentials!");
        }
    }

    // ================= VIEW BUSES =================
    static void viewBuses() {

        System.out.println("\n================ AVAILABLE BUSES ================\n");

        var buses = busDAO.getAllBuses();

        if (buses.isEmpty()) {
            System.out.println("No buses available.");
            return;
        }

        System.out.printf("%-5s %-12s %-20s %-12s %-12s %-6s %-6s%n",
                "ID", "BUS NO", "BUS NAME", "SOURCE", "DEST", "TOTAL", "AVAIL");

        System.out.println("--------------------------------------------------------------------------");

        for (var bus : buses) {
            System.out.printf("%-5d %-12s %-20s %-12s %-12s %-6d %-6d%n",
                    bus.getId(),
                    bus.getBusNo(),
                    bus.getBusName(),
                    bus.getSource(),
                    bus.getDestination(),
                    bus.getTotalSeats(),
                    bus.getAvailableSeats());
        }
    }

    // ================= USER DASHBOARD =================
    static void userDashboard(User user) {

        while (true) {

            System.out.println("\n=================================");
            System.out.println("   USER DASHBOARD");
            System.out.println("   Welcome, " + user.getName());
            System.out.println("=================================");

            System.out.println("1. View All Buses");
            System.out.println("2. Book Ticket");
            System.out.println("3. View My Bookings");
            System.out.println("4. Cancel Booking");
            System.out.println("5. Logout");

            System.out.print("Enter choice: ");
            int choice = sc.nextInt();
            sc.nextLine();

            switch (choice) {

                case 1:
                    viewBuses();
                    break;

                case 2:
                    bookTicket(user);
                    break;

                case 3:
                    viewMyBookings(user);
                    break;

                case 4:
                    cancelBooking(user);
                    break;

                case 5:
                    System.out.println("Logging out...");
                    return;

                default:
                    System.out.println("Invalid choice!");
            }
        }
    }

    // ================= BOOK TICKET =================
    static void bookTicket(User user) {

        System.out.println("\n--- BOOK TICKET ---");

        var buses = busDAO.getAllBuses();

        if (buses.isEmpty()) {
            System.out.println("No buses available.");
            return;
        }

        for (var bus : buses) {
            System.out.println(bus);
        }

        System.out.print("Enter Bus ID: ");
        int busId = sc.nextInt();
        sc.nextLine();

        Bus bus = busDAO.getBusById(busId);

        if (bus == null) {
            System.out.println("Invalid Bus ID!");
            return;
        }

        if (bus.getAvailableSeats() <= 0) {
            System.out.println("No seats available!");
            return;
        }

        int seatNo = busDAO.getNextAvailableSeat(busId);

        Booking booking = new Booking(user.getId(), busId, seatNo);

        boolean booked = bookingDAO.bookSeat(booking);
        boolean updated = busDAO.decreaseSeat(busId);

        if (booked && updated) {
            System.out.println("Ticket booked successfully!");
            System.out.println("Seat Number: " + seatNo);
        } else {
            System.out.println("Booking failed!");
        }
    }

    // ================= VIEW BOOKINGS =================
    static void viewMyBookings(User user) {

        System.out.println("\n--- MY BOOKINGS ---");

        var bookings = bookingDAO.getBookingsByUser(user.getId());

        if (bookings.isEmpty()) {
            System.out.println("No bookings found.");
            return;
        }

        for (var b : bookings) {
            System.out.println(b);
        }
    }

    // ================= CANCEL BOOKING =================
    static void cancelBooking(User user) {

        System.out.println("\n--- CANCEL BOOKING ---");

        var bookings = bookingDAO.getBookingsByUser(user.getId());

        if (bookings.isEmpty()) {
            System.out.println("No bookings to cancel.");
            return;
        }

        for (var b : bookings) {
            System.out.println(b);
        }

        System.out.print("Enter Booking ID to cancel: ");
        int bookingId = sc.nextInt();
        sc.nextLine();

        Booking booking = bookingDAO.getBookingById(bookingId);

        if (booking == null || booking.getUserId() != user.getId()) {
            System.out.println("Invalid booking!");
            return;
        }

        boolean cancelled = bookingDAO.cancelBooking(bookingId, user.getId());
        boolean updated = busDAO.increaseSeat(booking.getBusId());

        if (cancelled && updated) {
            System.out.println("Booking cancelled successfully!");
        } else {
            System.out.println("Cancellation failed!");
        }
    }

    // ================= ADMIN LOGIN =================
    static void adminLogin() {

        System.out.println("\n--- ADMIN LOGIN ---");

        System.out.print("Enter Admin Username: ");
        String username = sc.nextLine();

        System.out.print("Enter Admin Password: ");
        String password = sc.nextLine();

        if (username.equals("admin") && password.equals("admin123")) {
            System.out.println("Admin Login Successful!");
            adminDashboard();
        } else {
            System.out.println("Invalid Admin Credentials!");
        }
    }

    // ================= ADMIN DASHBOARD =================
    static void adminDashboard() {

        while (true) {

            System.out.println("\n===== ADMIN DASHBOARD =====");
            System.out.println("1. Add Bus");
            System.out.println("2. View All Buses");
            System.out.println("3. Logout");

            System.out.print("Enter choice: ");
            int choice = sc.nextInt();
            sc.nextLine();

            switch (choice) {

                case 1:
                    addBus();
                    break;

                case 2:
                    viewBuses();
                    break;

                case 3:
                    System.out.println("Logging out...");
                    return;

                default:
                    System.out.println("Invalid choice!");
            }
        }
    }

    // ================= ADD BUS =================
    static void addBus() {

        System.out.println("\n--- ADD BUS ---");

        System.out.print("Enter Bus No: ");
        String busNo = sc.nextLine();

        System.out.print("Enter Bus Name: ");
        String busName = sc.nextLine();

        System.out.print("Enter Source: ");
        String source = sc.nextLine();

        System.out.print("Enter Destination: ");
        String destination = sc.nextLine();

        System.out.print("Enter Total Seats: ");
        int seats = sc.nextInt();
        sc.nextLine();

        Bus bus = new Bus(busNo, busName, source, destination, seats);

        boolean result = busDAO.addBus(bus);

        if (result) {
            System.out.println("Bus added successfully!");
        } else {
            System.out.println("Failed to add bus!");
        }
    }
}