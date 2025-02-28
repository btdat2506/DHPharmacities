package com.example.testpharmacy.UI.admin.products;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.testpharmacy.Model.Medicine;
import com.example.testpharmacy.R;

import java.util.List;
import java.util.Locale;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {

    private List<Medicine> products;
    private OnProductClickListener listener;

    public interface OnProductClickListener {
        void onProductClick(Medicine product);
    }

    public ProductAdapter(List<Medicine> products, OnProductClickListener listener) {
        this.products = products;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_product_admin, parent, false);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        Medicine product = products.get(position);

        holder.nameTextView.setText(product.getName());
        holder.priceTextView.setText(String.format(Locale.getDefault(), "%.2f đ", product.getPrice()));
        holder.stockTextView.setText("Stock: " + product.getStockQuantity());

        // Load image using Glide
        if (product.getImageUrl() != null && !product.getImageUrl().isEmpty()) {
            Glide.with(holder.itemView.getContext())
                    .load(product.getImageUrl())
                    .into(holder.imageView);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onProductClick(product);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return products.size();
    }

    static class ProductViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView nameTextView;
        TextView priceTextView;
        TextView stockTextView;

        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.admin_product_image_view);
            nameTextView = itemView.findViewById(R.id.admin_product_name_text_view);
            priceTextView = itemView.findViewById(R.id.admin_product_price_text_view);
            stockTextView = itemView.findViewById(R.id.admin_product_stock_text_view);
        }
    }
}