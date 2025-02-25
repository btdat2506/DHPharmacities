package com.example.testpharmacy; // Replace with your actual package name

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
    private List<CartItem> cartItemList;
    private CartActivity cartActivity; // Reference to CartActivity to update summary

    public CartItemAdapter(Context context, List<CartItem> cartItemList) {
        this.context = context;
        this.cartItemList = cartItemList;
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
        CartItem cartItem = cartItemList.get(position);
        Medicine medicine = cartItem.getMedicine();

        holder.itemNameTextView.setText(medicine.getName());
        holder.itemPriceTextView.setText(String.format("%.3f", medicine.getPrice()) + "đ");
        Glide.with(context)
                .load(medicine.getImageUrl())
                .into(holder.itemImageView);
        holder.itemQuantityEditText.setText(String.valueOf(cartItem.getQuantity()));
        holder.itemTotalPriceTextView.setText(String.format("%.3f", cartItem.getTotalPrice()) + "đ");

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

                cartItem.setQuantity(quantity); // Update CartItem quantity
                holder.itemTotalPriceTextView.setText(String.format("%.3f", cartItem.getTotalPrice()) + "đ"); // Update item total
                if (cartActivity != null) {
                    cartActivity.updateCartSummary(); // Update cart summary in CartActivity
                }
            }
        });

        holder.increaseQuantityButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int currentQuantity = cartItem.getQuantity();
                cartItem.setQuantity(currentQuantity + 1);
                holder.itemQuantityEditText.setText(String.valueOf(cartItem.getQuantity()));
                holder.itemTotalPriceTextView.setText(String.format("%.3f", cartItem.getTotalPrice()) + "đ");
                if (cartActivity != null) {
                    cartActivity.updateCartSummary(); // Update cart summary in CartActivity
                }
            }
        });

        holder.decreaseQuantityButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int currentQuantity = cartItem.getQuantity();
                if (currentQuantity > 1) {
                    cartItem.setQuantity(currentQuantity - 1);
                    holder.itemQuantityEditText.setText(String.valueOf(cartItem.getQuantity()));
                    holder.itemTotalPriceTextView.setText(String.format("%.3f", cartItem.getTotalPrice()) + "đ");
                    if (cartActivity != null) {
                        cartActivity.updateCartSummary(); // Update cart summary in CartActivity
                    }
                } else {
                    // Remove item from cart if quantity becomes 0 (or 1 and user decreases)
                    cartItemList.remove(position);
                    notifyItemRemoved(position);
                    if (cartActivity != null) {
                        cartActivity.updateCartSummary(); // Update cart summary in CartActivity
                    }
                    notifyItemRangeChanged(position, cartItemList.size()); // Update positions after removal
                }
            }
        });

        holder.removeItemButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cartItemList.remove(position);
                notifyItemRemoved(position);
                if (cartActivity != null) {
                    cartActivity.updateCartSummary(); // Update cart summary in CartActivity
                }
                notifyItemRangeChanged(position, cartItemList.size()); // Update positions after removal
            }
        });
    }

    @Override
    public int getItemCount() {
        return cartItemList.size();
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