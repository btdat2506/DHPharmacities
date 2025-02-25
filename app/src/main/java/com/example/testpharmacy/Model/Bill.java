package com.example.testpharmacy.Model;

public class Bill {
    private long cartItemId;      // Maps to COLUMN_CART_ITEM_ID
    private long userId;          // Maps to COLUMN_CART_USER_ID (Foreign Key)
    private long productId;       // Maps to COLUMN_CART_PRODUCT_ID (Foreign Key)
    private int quantity;         // Maps to COLUMN_CART_QUANTITY

    public Bill() {
        // Default constructor
    }

    public Bill(long userId, long productId, int quantity) {
        this.userId = userId;
        this.productId = productId;
        this.quantity = quantity;
    }

    // Getters and Setters

    public long getCartItemId() {
        return cartItemId;
    }

    public void setCartItemId(long cartItemId) {
        this.cartItemId = cartItemId;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public long getProductId() {
        return productId;
    }

    public void setProductId(long productId) {
        this.productId = productId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    // Note: You might also want to add a method to fetch the associated Product object
    // if you need product details within the CartItem (e.g., to display name, price, etc.)
    // But for simplicity, in this basic model, CartItem just holds IDs and quantity.
}
