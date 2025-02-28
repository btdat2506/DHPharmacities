package com.example.testpharmacy.admin;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.testpharmacy.Model.Bill;
import com.example.testpharmacy.R;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.OrderViewHolder> {

    private List<Bill> orders;
    private OnOrderClickListener listener;
    private SimpleDateFormat dateFormat;

    public interface OnOrderClickListener {
        void onOrderClick(Bill order);
    }

    public OrderAdapter(List<Bill> orders, OnOrderClickListener listener) {
        this.orders = orders;
        this.listener = listener;
        this.dateFormat = new SimpleDateFormat("MMM dd, yyyy HH:mm", Locale.getDefault());
    }

    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_order, parent, false);
        return new OrderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderViewHolder holder, int position) {
        Bill order = orders.get(position);
        
        holder.orderNumberTextView.setText(order.getOrderNumber());
        holder.customerNameTextView.setText(order.getShippingName());
        holder.orderDateTextView.setText(dateFormat.format(order.getOrderDate()));
        holder.orderTotalTextView.setText(String.format(Locale.getDefault(), 
                "%.2f đ", order.getTotalAmount()));
//        holder.orderStatusTextView.setText(getFormattedStatus(order.getStatus()));
        holder.orderStatusTextView.setText(order.getStatus());
        
        // Set status text color
        int statusColor = getStatusColor(order.getStatus());
        holder.orderStatusTextView.setTextColor(statusColor);
        
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onOrderClick(order);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return orders.size();
    }

    private String getFormattedStatus(String status) {
        if (status == null) return "Dog";
        
        switch (status.toLowerCase()) {
            case "pending":
                return "Pending";
            case "processing":
                return "Processing";
            case "shipping":
                return "Shipping";
            case "delivered":
                return "Delivered";
            case "cancelled":
                return "Cancelled";
            default:
                return "Pending";
        }
    }

    private int getStatusColor(String status) {
        if (status == null) return 0xFF808080; // Gray for unknown
        
        switch (status.toLowerCase()) {
            case "pending":
                return 0xFF808080; // Gray
            case "processing":
                return 0xFF2196F3; // Blue
            case "shipping":
                return 0xFFFF9800; // Orange
            case "delivered":
                return 0xFF4CAF50; // Green
            case "cancelled":
                return 0xFFF44336; // Red
            default:
                return 0xFF808080; // Gray
        }
    }

    static class OrderViewHolder extends RecyclerView.ViewHolder {
        TextView orderNumberTextView;
        TextView customerNameTextView;
        TextView orderDateTextView;
        TextView orderTotalTextView;
        TextView orderStatusTextView;

        public OrderViewHolder(@NonNull View itemView) {
            super(itemView);
            orderNumberTextView = itemView.findViewById(R.id.order_number_text_view);
            customerNameTextView = itemView.findViewById(R.id.order_customer_name_text_view);
            orderDateTextView = itemView.findViewById(R.id.order_date_text_view);
            orderTotalTextView = itemView.findViewById(R.id.order_total_text_view);
            orderStatusTextView = itemView.findViewById(R.id.order_status_text_view);
        }
    }
}
