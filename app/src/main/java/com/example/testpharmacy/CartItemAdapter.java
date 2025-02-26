package com.example.testpharmacy;

import android.content.Context;
import android.text.Editable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.text.TextWatcher;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

public class CartItemAdapter extends RecyclerView.Adapter<CartItemAdapter.CartItemViewHolder> {

    private Context context;
    private CartActivity cartActivity; // Reference to CartActivity to update summary

    public CartItemAdapter(Context context, List<CartItem> cartItemList) {
        this.context = context;
        if (context instanceof CartActivity) {
            this.cartActivity = (CartActivity) context; // Cast context to CartActivity
        }
    }

    @NonNull
    @Override
    public CartItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_cart, parent, false);
        return new CartItemViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull CartItemViewHolder holder, int position) {
        // Get the current cart items from the manager each time
        List<CartItem> cartItems = CartManager.getInstance().getCartItems();
        CartItem cartItem = cartItems.get(position);
        Medicine medicine = cartItem.getMedicine();

        holder.itemNameTextView.setText(medicine.getName());
        holder.itemPriceTextView.setText(String.format("%.3f", medicine.getPrice()) + "đ");
        Glide.with(context)
                .load(medicine.getImageUrl())
                .into(holder.itemImageView);
        holder.itemQuantityEditText.setText(String.valueOf(cartItem.getQuantity()));
        holder.itemTotalPriceTextView.setText(String.format("%.3f", cartItem.getTotalPrice()) + "đ");

        // Save the position to use in listeners
        final int itemPosition = position;

        holder.itemQuantityEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // No action needed before text change
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // No action needed during text change
            }

            @Override
            public void afterTextChanged(Editable s) {
                String quantityStr = s.toString();
                int quantity = 1; // Default quantity if input is invalid or empty
                try {
                    quantity = Integer.parseInt(quantityStr);
                    if (quantity <= 0) {
                        quantity = 1; // Ensure quantity is at least 1
                        holder.itemQuantityEditText.setText(String.valueOf(quantity)); // Reset EditText to 1
                    }
                } catch (NumberFormatException e) {
                    quantity = 1; // Default to 1 if parsing fails
                    holder.itemQuantityEditText.setText(String.valueOf(quantity)); // Reset EditText to 1
                }

                // Update quantity through CartManager
                CartManager.getInstance().updateQuantity(medicine.getProductId(), quantity);

                // Refresh total price display
                holder.itemTotalPriceTextView.setText(String.format("%.3f",
                        CartManager.getInstance().getCartItems().get(itemPosition).getTotalPrice()) + "đ");

                // Update cart summary in CartActivity
                if (cartActivity != null) {
                    cartActivity.updateCartSummary();
                }
            }
        });

        holder.increaseQuantityButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get current quantity from CartManager
                int currentQuantity = CartManager.getInstance().getCartItems().get(itemPosition).getQuantity();
                int newQuantity = currentQuantity + 1;

                // Update through CartManager
                CartManager.getInstance().updateQuantity(medicine.getProductId(), newQuantity);

                // Update UI
                holder.itemQuantityEditText.setText(String.valueOf(newQuantity));
                holder.itemTotalPriceTextView.setText(String.format("%.3f",
                        CartManager.getInstance().getCartItems().get(itemPosition).getTotalPrice()) + "đ");

                // Update cart summary in CartActivity
                if (cartActivity != null) {
                    cartActivity.updateCartSummary();
                }
            }
        });

        holder.decreaseQuantityButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get current quantity from CartManager
                int currentQuantity = CartManager.getInstance().getCartItems().get(itemPosition).getQuantity();

                if (currentQuantity > 1) {
                    int newQuantity = currentQuantity - 1;

                    // Update through CartManager
                    CartManager.getInstance().updateQuantity(medicine.getProductId(), newQuantity);

                    // Update UI
                    holder.itemQuantityEditText.setText(String.valueOf(newQuantity));
                    holder.itemTotalPriceTextView.setText(String.format("%.3f",
                            CartManager.getInstance().getCartItems().get(itemPosition).getTotalPrice()) + "đ");

                    // Update cart summary in CartActivity
                    if (cartActivity != null) {
                        cartActivity.updateCartSummary();
                    }
                } else {
                    // Remove item if quantity would become 0
                    CartManager.getInstance().removeCartItemAt(itemPosition);
                    notifyItemRemoved(itemPosition);
                    notifyItemRangeChanged(itemPosition, CartManager.getInstance().getCartItems().size());

                    // Update cart summary in CartActivity
                    if (cartActivity != null) {
                        cartActivity.updateCartSummary();
                    }
                }
            }
        });

        holder.removeItemButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Remove through CartManager
                CartManager.getInstance().removeCartItemAt(itemPosition);

                // Update UI
                notifyItemRemoved(itemPosition);
                notifyItemRangeChanged(itemPosition, CartManager.getInstance().getCartItems().size());

                // Update cart summary in CartActivity
                if (cartActivity != null) {
                    cartActivity.updateCartSummary();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        // Always get the current count from CartManager
        return CartManager.getInstance().getCartItems().size();
    }

    public static class CartItemViewHolder extends RecyclerView.ViewHolder {
        ImageView itemImageView;
        TextView itemNameTextView;
        TextView itemPriceTextView;
        EditText itemQuantityEditText;
        TextView itemTotalPriceTextView;
        Button increaseQuantityButton;
        Button decreaseQuantityButton;
        Button removeItemButton;

        public CartItemViewHolder(@NonNull View itemView) {
            super(itemView);
            itemImageView = itemView.findViewById(R.id.cart_item_image_view);
            itemNameTextView = itemView.findViewById(R.id.cart_item_name_text_view);
            itemPriceTextView = itemView.findViewById(R.id.cart_item_price_text_view);
            itemQuantityEditText = itemView.findViewById(R.id.cart_item_quantity_edit_text);
            itemTotalPriceTextView = itemView.findViewById(R.id.cart_item_total_price_text_view);
            increaseQuantityButton = itemView.findViewById(R.id.cart_item_increase_button);
            decreaseQuantityButton = itemView.findViewById(R.id.cart_item_decrease_button);
            removeItemButton = itemView.findViewById(R.id.cart_item_remove_button);
        }
    }
}