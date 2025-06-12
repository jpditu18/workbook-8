package com.pluralsight;

import java.sql.*;
import java.util.Scanner;

public class App {
    private static final String DB_URL = "jdbc:mysql://localhost:3306/northwind";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "yearup";

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        boolean running = true;

        while (running) {
            System.out.println("What do you want to do?");
            System.out.println("1) Display all products");
            System.out.println("2) Display all customers");
            System.out.println("3) Display all categories");
            System.out.println("0) Exit");
            System.out.print("Select an option: ");

            String input = scanner.nextLine();
            switch (input) {
                case "1":
                    displayAllProducts();
                    break;
                case "2":
                    displayAllCustomers();
                    break;
                case "3":
                    displayAllCategories(scanner);
                    break;
                case "0":
                    running = false;
                    break;
                default:
                    System.out.println("Invalid option. Try again.");
            }
        }
    }

    private static void displayAllProducts() {
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT ProductID, ProductName, UnitPrice, UnitsInStock FROM Products")) {

            System.out.printf("%-5s %-30s %-10s %-10s\n", "ID", "Name", "Price", "Stock");
            System.out.println("-----------------------------------------------------------");
            while (rs.next()) {
                System.out.printf("%-5d %-30s %-10.2f %-10d\n",
                        rs.getInt("ProductID"),
                        rs.getString("ProductName"),
                        rs.getDouble("UnitPrice"),
                        rs.getInt("UnitsInStock"));
            }
        } catch (SQLException e) {
            System.out.println("Error retrieving products.");
            e.printStackTrace();
        }
    }

    private static void displayAllCustomers() {
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        try {
            conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
            stmt = conn.createStatement();
            rs = stmt.executeQuery("SELECT ContactName, CompanyName, City, Country, Phone FROM Customers ORDER BY Country");

            System.out.printf("%-25s %-30s %-20s %-20s %-15s\n", "Contact Name", "Company", "City", "Country", "Phone");
            System.out.println("---------------------------------------------------------------------------------------------");
            while (rs.next()) {
                System.out.printf("%-25s %-30s %-20s %-20s %-15s\n",
                        rs.getString("ContactName"),
                        rs.getString("CompanyName"),
                        rs.getString("City"),
                        rs.getString("Country"),
                        rs.getString("Phone"));
            }
        } catch (SQLException e) {
            System.out.println("Error retrieving customers.");
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) rs.close();
                if (stmt != null) stmt.close();
                if (conn != null) conn.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }

    private static void displayAllCategories(Scanner scanner) {
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT CategoryID, CategoryName FROM Categories ORDER BY CategoryID")) {

            System.out.println("Categories:");
            while (rs.next()) {
                System.out.printf("%d: %s\n", rs.getInt("CategoryID"), rs.getString("CategoryName"));
            }

            System.out.print("Enter Category ID to view products: ");
            int categoryId = Integer.parseInt(scanner.nextLine());
            displayProductsByCategory(conn, categoryId);

        } catch (SQLException e) {
            System.out.println("Error retrieving categories.");
            e.printStackTrace();
        }
    }

    private static void displayProductsByCategory(Connection conn, int categoryId) {
        String query = "SELECT ProductID, ProductName, UnitPrice, UnitsInStock FROM Products WHERE CategoryID = ?";
        try (PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, categoryId);
            try (ResultSet rs = ps.executeQuery()) {
                System.out.printf("%-5s %-30s %-10s %-10s\n", "ID", "Name", "Price", "Stock");
                System.out.println("-----------------------------------------------------------");
                while (rs.next()) {
                    System.out.printf("%-5d %-30s %-10.2f %-10d\n",
                            rs.getInt("ProductID"),
                            rs.getString("ProductName"),
                            rs.getDouble("UnitPrice"),
                            rs.getInt("UnitsInStock"));
                }
            }
        } catch (SQLException e) {
            System.out.println("Error retrieving products for category.");
            e.printStackTrace();
        }
    }
}
