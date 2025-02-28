package com.example.testpharmacy.UI.admin.customers;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.testpharmacy.Model.User;
import com.example.testpharmacy.R;

import java.util.List;

public class CustomerAdapter extends RecyclerView.Adapter<CustomerAdapter.CustomerViewHolder> {

    private List<User> customers;
    private OnCustomerClickListener listener;

    public interface OnCustomerClickListener {
        void onCustomerClick(User customer);
    }

    public CustomerAdapter(List<User> customers, OnCustomerClickListener listener) {
        this.customers = customers;
        this.listener = listener;
    }

    public void setCustomers(List<User> customers) {
        this.customers = customers;
    }

    @NonNull
    @Override
    public CustomerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_customer, parent, false);
        return new CustomerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CustomerViewHolder holder, int position) {
        User customer = customers.get(position);
        
        holder.nameTextView.setText(customer.getName());
        holder.emailTextView.setText(customer.getEmail());
        holder.phoneTextView.setText(customer.getPhoneNumber() != null ? 
                customer.getPhoneNumber() : "No phone number");
        
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onCustomerClick(customer);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return customers.size();
    }

    static class CustomerViewHolder extends RecyclerView.ViewHolder {
        TextView nameTextView;
        TextView emailTextView;
        TextView phoneTextView;

        public CustomerViewHolder(@NonNull View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.customer_name_text_view);
            emailTextView = itemView.findViewById(R.id.customer_email_text_view);
            phoneTextView = itemView.findViewById(R.id.customer_phone_text_view);
        }
    }
}
