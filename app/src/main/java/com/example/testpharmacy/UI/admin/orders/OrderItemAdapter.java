package com.example.testpharmacy.UI.admin.orders;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.testpharmacy.Model.BillItem;
import com.example.testpharmacy.R;
import com.example.testpharmacy.Utils;

import java.util.List;
import java.util.Locale;

public class OrderItemAdapter extends RecyclerView.Adapter<OrderItemAdapter.OrderItemViewHolder> {

    private List<BillItem> billItems;

    public OrderItemAdapter(List<BillItem> billItems) {
        this.billItems = billItems;
    }

    @NonNull
    @Override
    public OrderItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_order_detail, parent, false);
        return new OrderItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderItemViewHolder holder, int position) {
        BillItem billItem = billItems.get(position);

        holder.nameTextView.setText(billItem.getProductName());
        holder.priceTextView.setText(Utils.formatVND(billItem.getUnitPrice()));
        holder.quantityTextView.setText("x" + billItem.getQuantity());
        holder.totalTextView.setText(Utils.formatVND(billItem.getTotalPrice()));

        // Load image if available
        if (billItem.getProductImage() != null && !billItem.getProductImage().isEmpty()) {
            Glide.with(holder.itemView.getContext())
                    .load(billItem.getProductImage())
                    .into(holder.imageView);
        }
    }

    @Override
    public int getItemCount() {
        return billItems.size();
    }

    static class OrderItemViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView nameTextView;
        TextView priceTextView;
        TextView quantityTextView;
        TextView totalTextView;

        public OrderItemViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.order_item_image_view);
            nameTextView = itemView.findViewById(R.id.order_item_name_text_view);
            priceTextView = itemView.findViewById(R.id.order_item_price_text_view);
            quantityTextView = itemView.findViewById(R.id.order_item_quantity_text_view);
            totalTextView = itemView.findViewById(R.id.order_item_total_text_view);
        }
    }
}