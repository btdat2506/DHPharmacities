package com.example.testpharmacy; // Replace with your actual package name

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class CartItemCheckoutAdapter extends RecyclerView.Adapter<CartItemCheckoutAdapter.CartItemCheckoutViewHolder> {

    private Context context;
    private List<CartItem> cartItemList;

    public CartItemCheckoutAdapter(Context context, List<CartItem> cartItemList) {
        this.context = context;
        this.cartItemList = cartItemList;
    }

    @NonNull
    @Override
    public CartItemCheckoutViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_cart_checkout, parent, false);
        return new CartItemCheckoutViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull CartItemCheckoutViewHolder holder, int position) {
        CartItem cartItem = cartItemList.get(position);
        Medicine medicine = cartItem.getMedicine();

        holder.itemNameTextView.setText(medicine.getName());
        holder.itemPriceTextView.setText(String.format("%.3f", medicine.getPrice()) + "đ");
        holder.itemImageView.setImageResource(medicine.getImageResourceId());
        holder.itemQuantityTextView.setText("x" + cartItem.getQuantity()); // Display quantity with "x"
    }

    @Override
    public int getItemCount() {
        return cartItemList.size();
    }

    public static class CartItemCheckoutViewHolder extends RecyclerView.ViewHolder {
        ImageView itemImageView;
        TextView itemNameTextView;
        TextView itemPriceTextView;
        TextView itemQuantityTextView;

        public CartItemCheckoutViewHolder(@NonNull View itemView) {
            super(itemView);
            itemImageView = itemView.findViewById(R.id.checkout_item_image_view);
            itemNameTextView = itemView.findViewById(R.id.checkout_item_name_text_view);
            itemPriceTextView = itemView.findViewById(R.id.checkout_item_price_text_view);
            itemQuantityTextView = itemView.findViewById(R.id.checkout_item_quantity_text_view);
        }
    }
}