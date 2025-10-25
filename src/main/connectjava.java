package main;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class connectjava {

    static Connection con = null;

    public static void main(String[] args) {
        // Step 1: Connect to the database
        connect();

        // Step 2: Create tables (auto if missing)
        createTables();

        // Step 3: Register a new user (only once for testing)
        registerUser("Juan Dela Cruz", "juan@gmail.com", "Manila", "password123", "09123456789", "admin");

        // Step 4: Try logging in
        loginUser("juan@gmail.com", "password123");  // ✅ Correct
        loginUser("juan@gmail.com", "wrongpass");    // ❌ Incorrect password
        loginUser("wrong@gmail.com", "password123"); // ❌ No such user

        // Step 5: Close connection
        closeConnection();
    }

    // Connect to SQLite
    public static void connect() {
        try {
            String url = "jdbc:sqlite:inventory.db";
            con = DriverManager.getConnection(url);
            System.out.println("✅ Connected to SQLite database!");
        } catch (SQLException e) {
            System.out.println("❌ Connection failed: " + e.getMessage());
        }
    }

    // Create tables
    public static void createTables() {
        try (Statement st = con.createStatement()) {
            st.execute("CREATE TABLE IF NOT EXISTS users (" +
                    "user_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "fullname TEXT NOT NULL, " +
                    "email TEXT NOT NULL UNIQUE, " +
                    "address TEXT, " +
                    "password TEXT NOT NULL, " +
                    "contact_info TEXT, " +
                    "user_type TEXT)");
            st.execute("CREATE TABLE IF NOT EXISTS supplier (" +
                    "supplier_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "name TEXT NOT NULL, " +
                    "contact TEXT, " +
                    "address TEXT)");
            st.execute("CREATE TABLE IF NOT EXISTS items (" +
                    "item_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "name TEXT NOT NULL, " +
                    "description TEXT, " +
                    "price REAL)");
            st.execute("CREATE TABLE IF NOT EXISTS orders (" +
                    "order_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "customer_name TEXT, " +
                    "item_id INTEGER, " +
                    "quantity INTEGER, " +
                    "total REAL, " +
                    "FOREIGN KEY (item_id) REFERENCES items(item_id))");
            System.out.println("✅ Tables verified or created successfully!");
        } catch (SQLException e) {
            System.out.println("❌ Error creating tables: " + e.getMessage());
        }
    }

    // Register user
    public static void registerUser(String fullname, String email, String address, String password, String contact, String userType) {
        String sql = "INSERT INTO users(fullname, email, address, password, contact_info, user_type) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, fullname);
            ps.setString(2, email);
            ps.setString(3, address);
            ps.setString(4, password);
            ps.setString(5, contact);
            ps.setString(6, userType);
            ps.executeUpdate();
            System.out.println("✅ User registered successfully!");
        } catch (SQLException e) {
            if (e.getMessage().contains("UNIQUE constraint failed")) {
                System.out.println("⚠️ User already exists: " + email);
            } else {
                System.out.println("❌ Error registering user: " + e.getMessage());
            }
        }
    }

    // Login user
    public static void loginUser(String email, String password) {
        String sql = "SELECT * FROM users WHERE email = ?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, email);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                String dbPass = rs.getString("password");
                if (dbPass.equals(password)) {
                    System.out.println("✅ Login successful! Welcome " + rs.getString("fullname") +
                            " (" + rs.getString("user_type") + ")");
                } else {
                    System.out.println("❌ Incorrect password for " + email);
                }
            } else {
                System.out.println("❌ No account found for: " + email);
            }
        } catch (SQLException e) {
            System.out.println("❌ Error logging in: " + e.getMessage());
        }
    }

    // Close DB connection
    public static void closeConnection() {
        try {
            if (con != null && !con.isClosed()) {
                con.close();
                System.out.println("🔒 Database connection closed.");
            }
        } catch (SQLException e) {
            System.out.println("⚠️ Error closing connection: " + e.getMessage());
        }
    }
}