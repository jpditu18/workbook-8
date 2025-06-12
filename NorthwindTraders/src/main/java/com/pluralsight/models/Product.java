package com.pluralsight.models;

public class Product {
        private int id;
        private String name;
        private double price;
        private int categoryId;
        private String categoryName;

        // Constructor for getAllProducts (no category)
        public Product(int id, String name, double price) {
            this.id = id;
            this.name = name;
            this.price = price;
        }

        // Constructor for getProductsByCategoryId (with category)
        public Product(int id, String name, double price, int categoryId, String categoryName) {
            this.id = id;
            this.name = name;
            this.price = price;
            this.categoryId = categoryId;
            this.categoryName = categoryName;
        }

        // Getters (you can add setters if needed)
        public int getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        public double getPrice() {
            return price;
        }

        public int getCategoryId() {
            return categoryId;
        }

        public String getCategoryName() {
            return categoryName;
        }
    }
