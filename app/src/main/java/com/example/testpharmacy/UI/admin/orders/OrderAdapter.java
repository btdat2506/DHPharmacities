package com.example.testpharmacy.UI.admin.orders;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.testpharmacy.Constants.OrderStatusConstants;
import com.example.testpharmacy.Model.Bill;
import com.example.testpharmacy.R;
import com.example.testpharmacy.Utils;

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

    public void setOrders(List<Bill> orders) {
        this.orders = orders;
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
        holder.orderTotalTextView.setText(Utils.formatVND(order.getTotalAmount()));

        // Use the OrderStatusConstants to get the localized status
        holder.orderStatusTextView.setText(OrderStatusConstants.getLocalizedStatus(
                holder.itemView.getContext(), order.getStatus()));

        // Set status text color using OrderStatusConstants
        int statusColor = OrderStatusConstants.getStatusColor(order.getStatus());
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