package com.example.testpharmacy;

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

    public List<Long> getProductIdsInCart() {
        List<Long> productIds = new ArrayList<>();
        for (CartItem cartItem : CartManager.getInstance().getCartItems()) {
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
}
