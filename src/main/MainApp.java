package main;

import config.config;
import java.sql.*;
import java.util.Scanner;

public class MainApp {
    static Scanner sc = new Scanner(System.in);
    static Connection con = config.connectDB();

    public static void main(String[] args) {
        if (con == null) {
            System.out.println("❌ Database connection failed!");
            return;
        }

        System.out.println("\n=== 🔰 WELCOME TO SERWING INVENTORY MANAGEMENT SYSTEM ===");

        while (true) {
            System.out.println("\n1. Login");
            System.out.println("2. Register");
            System.out.println("3. Exit");
            System.out.print("Select option: ");
            int option = sc.nextInt();
            sc.nextLine();

            switch (option) {
                case 1:
                    login();
                    break;
                case 2:
                    register();
                    break;
                case 3:
                    System.out.println("👋 Goodbye!");
                    System.exit(0);
                    break;
                default:
                    System.out.println("❗ Invalid choice!");
                    break;
            }
        }
    }

    // ============ USER AUTH =============

    public static void register() {
        try {
            System.out.println("\n--- 👤 REGISTER USER ---");
            System.out.print("Full Name: ");
            String fullname = sc.nextLine();
            System.out.print("Email: ");
            String email = sc.nextLine();
            System.out.print("Address: ");
            String address = sc.nextLine();
            System.out.print("Password: ");
            String password = sc.nextLine();
            System.out.print("Contact Info: ");
            String contact = sc.nextLine();
            System.out.print("User Type (Admin/Manager/Staff): ");
            String userType = sc.nextLine();

            String sql = "INSERT INTO users(fullname, email, address, password, contact_info, user_type) VALUES (?, ?, ?, ?, ?, ?)";
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setString(1, fullname);
            pst.setString(2, email);
            pst.setString(3, address);
            pst.setString(4, password);
            pst.setString(5, contact);
            pst.setString(6, userType);
            pst.executeUpdate();

            System.out.println("✅ User registered successfully!");
        } catch (SQLException e) {
            System.out.println("❌ Error registering user: " + e.getMessage());
        }
    }

    public static void login() {
        try {
            System.out.print("\nEmail: ");
            String email = sc.nextLine();
            System.out.print("Password: ");
            String password = sc.nextLine();

            String sql = "SELECT * FROM users WHERE email = ? AND password = ?";
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setString(1, email);
            pst.setString(2, password);
            ResultSet rs = pst.executeQuery();

            if (rs.next()) {
                System.out.println("\n✅ Login successful! Welcome, " + rs.getString("fullname"));
                dashboard();
            } else {
                System.out.println("❌ Invalid credentials!");
            }
        } catch (SQLException e) {
            System.out.println("❌ Error logging in: " + e.getMessage());
        }
    }

    // ============ DASHBOARD =============

    public static void dashboard() {
        while (true) {
            System.out.println("\n=== 🧾 DASHBOARD ===");
            System.out.println("1. Manage Suppliers");
            System.out.println("2. Manage Items");
            System.out.println("3. Manage Orders");
            System.out.println("4. Logout");
            System.out.print("Select option: ");
            int choice = sc.nextInt();
            sc.nextLine();

            switch (choice) {
                case 1:
                    manageSuppliers();
                    break;
                case 2:
                    manageItems();
                    break;
                case 3:
                    manageOrders();
                    break;
                case 4:
                    System.out.println("👋 Logged out!");
                    return;
                default:
                    System.out.println("❗ Invalid option!");
                    break;
            }
        }
    }

    // ============ SUPPLIER CRUD =============

    public static void manageSuppliers() {
        while (true) {
            System.out.println("\n=== 🏢 SUPPLIER MENU ===");
            System.out.println("1. Add Supplier");
            System.out.println("2. View Suppliers");
            System.out.println("3. Update Supplier");
            System.out.println("4. Delete Supplier");
            System.out.println("5. Back");
            System.out.print("Select option: ");
            int opt = sc.nextInt();
            sc.nextLine();

            switch (opt) {
                case 1:
                    addSupplier();
                    break;
                case 2:
                    viewSuppliers();
                    break;
                case 3:
                    updateSupplier();
                    break;
                case 4:
                    deleteSupplier();
                    break;
                case 5:
                    return;
                default:
                    System.out.println("❗ Invalid option!");
                    break;
            }
        }
    }

    public static void addSupplier() {
        try {
            System.out.print("Supplier Name: ");
            String name = sc.nextLine();
            System.out.print("Contact: ");
            String contact = sc.nextLine();
            System.out.print("Address: ");
            String address = sc.nextLine();

            String sql = "INSERT INTO supplier(name, contact, address) VALUES (?, ?, ?)";
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setString(1, name);
            pst.setString(2, contact);
            pst.setString(3, address);
            pst.executeUpdate();

            System.out.println("✅ Supplier added!");
        } catch (SQLException e) {
            System.out.println("❌ Error: " + e.getMessage());
        }
    }

    public static void viewSuppliers() {
        try {
            String sql = "SELECT * FROM supplier";
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(sql);

            System.out.println("\n--- SUPPLIERS ---");
            while (rs.next()) {
                System.out.println("ID: " + rs.getInt("supplier_id")
                        + " | Name: " + rs.getString("name")
                        + " | Contact: " + rs.getString("contact")
                        + " | Address: " + rs.getString("address"));
            }
        } catch (SQLException e) {
            System.out.println("❌ Error: " + e.getMessage());
        }
    }

    public static void updateSupplier() {
        try {
            System.out.print("Enter Supplier ID to update: ");
            int id = sc.nextInt();
            sc.nextLine();
            System.out.print("New Name: ");
            String name = sc.nextLine();
            System.out.print("New Contact: ");
            String contact = sc.nextLine();
            System.out.print("New Address: ");
            String address = sc.nextLine();

            String sql = "UPDATE supplier SET name=?, contact=?, address=? WHERE supplier_id=?";
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setString(1, name);
            pst.setString(2, contact);
            pst.setString(3, address);
            pst.setInt(4, id);
            pst.executeUpdate();

            System.out.println("✅ Supplier updated!");
        } catch (SQLException e) {
            System.out.println("❌ Error: " + e.getMessage());
        }
    }

    public static void deleteSupplier() {
        try {
            System.out.print("Enter Supplier ID to delete: ");
            int id = sc.nextInt();

            String sql = "DELETE FROM supplier WHERE supplier_id=?";
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setInt(1, id);
            pst.executeUpdate();

            System.out.println("✅ Supplier deleted!");
        } catch (SQLException e) {
            System.out.println("❌ Error: " + e.getMessage());
        }
    }

    // ============ ITEMS CRUD =============

    public static void manageItems() {
        while (true) {
            System.out.println("\n=== 📦 ITEM MENU ===");
            System.out.println("1. Add Item");
            System.out.println("2. View Items");
            System.out.println("3. Update Item");
            System.out.println("4. Delete Item");
            System.out.println("5. Back");
            System.out.print("Select option: ");
            int opt = sc.nextInt();
            sc.nextLine();

            switch (opt) {
                case 1:
                    addItem();
                    break;
                case 2:
                    viewItems();
                    break;
                case 3:
                    updateItem();
                    break;
                case 4:
                    deleteItem();
                    break;
                case 5:
                    return;
                default:
                    System.out.println("❗ Invalid option!");
                    break;
            }
        }
    }

    public static void addItem() {
        try {
            System.out.print("Item Name: ");
            String name = sc.nextLine();
            System.out.print("Description: ");
            String desc = sc.nextLine();
            System.out.print("Price: ");
            double price = sc.nextDouble();

            String sql = "INSERT INTO items(name, description, price) VALUES (?, ?, ?)";
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setString(1, name);
            pst.setString(2, desc);
            pst.setDouble(3, price);
            pst.executeUpdate();

            System.out.println("✅ Item added!");
        } catch (SQLException e) {
            System.out.println("❌ Error: " + e.getMessage());
        }
    }

    public static void viewItems() {
        try {
            String sql = "SELECT * FROM items";
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(sql);

            System.out.println("\n--- ITEMS ---");
            while (rs.next()) {
                System.out.println("ID: " + rs.getInt("item_id")
                        + " | Name: " + rs.getString("name")
                        + " | Desc: " + rs.getString("description")
                        + " | Price: " + rs.getDouble("price"));
            }
        } catch (SQLException e) {
            System.out.println("❌ Error: " + e.getMessage());
        }
    }

    public static void updateItem() {
        try {
            System.out.print("Enter Item ID to update: ");
            int id = sc.nextInt();
            sc.nextLine();
            System.out.print("New Name: ");
            String name = sc.nextLine();
            System.out.print("New Description: ");
            String desc = sc.nextLine();
            System.out.print("New Price: ");
            double price = sc.nextDouble();

            String sql = "UPDATE items SET name=?, description=?, price=? WHERE item_id=?";
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setString(1, name);
            pst.setString(2, desc);
            pst.setDouble(3, price);
            pst.setInt(4, id);
            pst.executeUpdate();

            System.out.println("✅ Item updated!");
        } catch (SQLException e) {
            System.out.println("❌ Error: " + e.getMessage());
        }
    }

    public static void deleteItem() {
        try {
            System.out.print("Enter Item ID to delete: ");
            int id = sc.nextInt();

            String sql = "DELETE FROM items WHERE item_id=?";
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setInt(1, id);
            pst.executeUpdate();

            System.out.println("✅ Item deleted!");
        } catch (SQLException e) {
            System.out.println("❌ Error: " + e.getMessage());
        }
    }

    // ============ ORDERS CRUD (Simplified) =============

    public static void manageOrders() {
        while (true) {
            System.out.println("\n=== 🧾 ORDER MENU ===");
            System.out.println("1. Create Order");
            System.out.println("2. View Orders");
            System.out.println("3. Back");
            System.out.print("Select option: ");
            int opt = sc.nextInt();
            sc.nextLine();

            switch (opt) {
                case 1:
                    createOrder();
                    break;
                case 2:
                    viewOrders();
                    break;
                case 3:
                    return;
                default:
                    System.out.println("❗ Invalid option!");
                    break;
            }
        }
    }

    public static void createOrder() {
        try {
            System.out.print("Customer Name: ");
            String customer = sc.nextLine();
            System.out.print("Item ID: ");
            int itemId = sc.nextInt();
            System.out.print("Quantity: ");
            int qty = sc.nextInt();

            // Get item price
            String getPriceSQL = "SELECT price FROM items WHERE item_id=?";
            PreparedStatement pstPrice = con.prepareStatement(getPriceSQL);
            pstPrice.setInt(1, itemId);
            ResultSet rs = pstPrice.executeQuery();

            if (!rs.next()) {
                System.out.println("❌ Item not found!");
                return;
            }

            double price = rs.getDouble("price");
            double subtotal = qty * price;

            String sql = "INSERT INTO orders(customer_name, item_id, quantity, total) VALUES (?, ?, ?, ?)";
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setString(1, customer);
            pst.setInt(2, itemId);
            pst.setInt(3, qty);
            pst.setDouble(4, subtotal);
            pst.executeUpdate();

            System.out.println("✅ Order created! Total: ₱" + subtotal);
        } catch (SQLException e) {
            System.out.println("❌ Error: " + e.getMessage());
        }
    }

    public static void viewOrders() {
        try {
            String sql = "SELECT * FROM orders";
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(sql);

            System.out.println("\n--- ORDERS ---");
            while (rs.next()) {
                System.out.println("Order ID: " + rs.getInt("order_id")
                        + " | Customer: " + rs.getString("customer_name")
                        + " | Item ID: " + rs.getInt("item_id")
                        + " | Qty: " + rs.getInt("quantity")
                        + " | Total: ₱" + rs.getDouble("total"));
            }
        } catch (SQLException e) {
            System.out.println("❌ Error: " + e.getMessage());
        }
    }
}