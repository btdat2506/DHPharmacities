package com.example.testpharmacy; // Replace with your actual package name

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
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
        holder.itemPriceTextView.setText("₹" + String.format("%.2f", medicine.getPrice()));
        holder.itemImageView.setImageResource(medicine.getImageResourceId());
        holder.itemQuantityTextView.setText(String.valueOf(cartItem.getQuantity()));
        holder.itemTotalPriceTextView.setText("₹" + String.format("%.2f", cartItem.getTotalPrice()));

        holder.increaseQuantityButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int currentQuantity = cartItem.getQuantity();
                cartItem.setQuantity(currentQuantity + 1);
                holder.itemQuantityTextView.setText(String.valueOf(cartItem.getQuantity()));
                holder.itemTotalPriceTextView.setText("₹" + String.format("%.2f", cartItem.getTotalPrice()));
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
                    holder.itemQuantityTextView.setText(String.valueOf(cartItem.getQuantity()));
                    holder.itemTotalPriceTextView.setText("₹" + String.format("%.2f", cartItem.getTotalPrice()));
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
        TextView itemQuantityTextView;
        TextView itemTotalPriceTextView;
        Button increaseQuantityButton;
        Button decreaseQuantityButton;
        Button removeItemButton;

        public CartItemViewHolder(@NonNull View itemView) {
            super(itemView);
            itemImageView = itemView.findViewById(R.id.cart_item_image_view);
            itemNameTextView = itemView.findViewById(R.id.cart_item_name_text_view);
            itemPriceTextView = itemView.findViewById(R.id.cart_item_price_text_view);
            itemQuantityTextView = itemView.findViewById(R.id.cart_item_quantity_text_view);
            itemTotalPriceTextView = itemView.findViewById(R.id.cart_item_total_price_text_view);
            increaseQuantityButton = itemView.findViewById(R.id.cart_item_increase_button);
            decreaseQuantityButton = itemView.findViewById(R.id.cart_item_decrease_button);
            removeItemButton = itemView.findViewById(R.id.cart_item_remove_button);
        }
    }
}