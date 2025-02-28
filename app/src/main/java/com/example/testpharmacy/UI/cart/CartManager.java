package com.example.testpharmacy.UI.cart;

import com.example.testpharmacy.Model.Medicine;

import java.util.ArrayList;
import java.util.List;

public class CartManager {
    private static CartManager instance;
    private List<CartItem> cartItems;

    private CartManager() {
        cartItems = new ArrayList<>();
    }

    public static synchronized CartManager getInstance() {
        if (instance == null) {
            instance = new CartManager();
        }
        return instance;
    }

    // Thêm sản phẩm vào giỏ hàng
    public void addToCart(Medicine medicine, int quantity) {
        // Kiểm tra nếu sản phẩm đã có trong giỏ hàng, thì chỉ cập nhật số lượng
        for (CartItem item : cartItems) {
            if (item.getMedicine().getProductId() == medicine.getProductId()) {
                item.setQuantity(item.getQuantity() + quantity); // Tăng số lượng
                return;
            }
        }
        // Nếu sản phẩm chưa có trong giỏ hàng, thêm mới
        cartItems.add(new CartItem(medicine, quantity));
    }

    // Update quantity of an item by product ID
    public void updateQuantity(long productId, int quantity) {
        if (quantity <= 0) {
            // If quantity is 0 or negative, remove the item
            removeFromCart(productId);
            return;
        }

        for (CartItem item : cartItems) {
            if (item.getMedicine().getProductId() == productId) {
                item.setQuantity(quantity);
                return;
            }
        }
    }

    // Remove an item by product ID
    public void removeFromCart(long productId) {
        for (int i = 0; i < cartItems.size(); i++) {
            if (cartItems.get(i).getMedicine().getProductId() == productId) {
                cartItems.remove(i);
                return;
            }
        }
    }

    // Remove an item at a specific position
    public void removeCartItemAt(int position) {
        if (position >= 0 && position < cartItems.size()) {
            cartItems.remove(position);
        }
    }

    public List<Long> getProductIdsInCart() {
        List<Long> productIds = new ArrayList<>();
        for (CartItem cartItem : cartItems) {
            productIds.add(cartItem.getMedicine().getProductId());
        }
        return productIds;
    }

    public List<CartItem> getCartItems() {
        return cartItems;
    }

    public void clearCart() {
        cartItems.clear();
    }

    // Get total number of items in cart
    public int getItemCount() {
        return cartItems.size();
    }

    // Calculate total price of all items in cart
    public double getTotalPrice() {
        double total = 0;
        for (CartItem item : cartItems) {
            total += item.getTotalPrice();
        }
        return total;
    }
}