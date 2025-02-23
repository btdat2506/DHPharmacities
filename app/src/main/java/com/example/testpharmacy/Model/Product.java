package com.example.testpharmacy.Model;

public class Product {
    private long productId;        // Maps to COLUMN_PRODUCT_ID
    private String name;           // Maps to COLUMN_PRODUCT_NAME
    private String description;    // Maps to COLUMN_PRODUCT_DESCRIPTION
    private String category;       // Maps to COLUMN_PRODUCT_CATEGORY
    private double price;          // Maps to COLUMN_PRODUCT_PRICE
    private String imageUrl;       // Maps to COLUMN_PRODUCT_IMAGE_URL
    private int stockQuantity;     // Maps to COLUMN_PRODUCT_STOCK_QUANTITY

    public Product() {
        // Default constructor
    }

    public Product(String name, String description, String category, double price, String imageUrl, int stockQuantity) {
        this.name = name;
        this.description = description;
        this.category = category;
        this.price = price;
        this.imageUrl = imageUrl;
        this.stockQuantity = stockQuantity;
    }

    // Getters and Setters for all fields

    public long getProductId() {
        return productId;
    }

    public void setProductId(long productId) {
        this.productId = productId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public int getStockQuantity() {
        return stockQuantity;
    }

    public void setStockQuantity(int stockQuantity) {
        this.stockQuantity = stockQuantity;
    }
}
