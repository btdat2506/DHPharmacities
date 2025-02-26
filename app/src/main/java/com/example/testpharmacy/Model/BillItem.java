package com.example.testpharmacy.Model;

import com.example.testpharmacy.CartItem;

public class BillItem {
    private long id;
    private String orderNumber; // To link to parent bill
    private long productId;
    private String productName;
    private String productImage;
    private double unitPrice;
    private int quantity;
    private double totalPrice;

    public BillItem() {
    }

    // Constructor to create from cart item
    public BillItem(CartItem cartItem) {
        this.productId = cartItem.getMedicine().getProductId();
        this.productName = cartItem.getMedicine().getName();
        this.productImage = cartItem.getMedicine().getImageUrl();
        this.unitPrice = cartItem.getMedicine().getPrice();
        this.quantity = cartItem.getQuantity();
        this.totalPrice = cartItem.getTotalPrice();
    }

    // Getters and setters
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    public long getProductId() {
        return productId;
    }

    public void setProductId(long productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductImage() {
        return productImage;
    }

    public void setProductImage(String productImage) {
        this.productImage = productImage;
    }

    public double getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(double unitPrice) {
        this.unitPrice = unitPrice;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    // Helper method to calculate total price
    public void calculateTotalPrice() {
        this.totalPrice = this.unitPrice * this.quantity;
    }
}