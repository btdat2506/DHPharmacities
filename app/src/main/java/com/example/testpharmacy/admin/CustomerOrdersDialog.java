package com.example.testpharmacy.admin;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.testpharmacy.Model.Bill;
import com.example.testpharmacy.R;

import java.util.List;

public class CustomerOrdersDialog extends Dialog {

    private List<Bill> orders;
    private RecyclerView ordersRecyclerView;
    private Button closeButton;

    public CustomerOrdersDialog(@NonNull Context context, List<Bill> orders) {
        super(context);
        this.orders = orders;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_customer_orders);

        ordersRecyclerView = findViewById(R.id.customer_orders_recycler_view);
        closeButton = findViewById(R.id.customer_orders_close_button);

        OrderAdapter adapter = new OrderAdapter(orders, new OrderAdapter.OnOrderClickListener() {
            @Override
            public void onOrderClick(Bill order) {
                Intent intent = new Intent(getContext(), OrderDetailActivity.class);
                intent.putExtra("order_number", order.getOrderNumber());
                getContext().startActivity(intent);
                dismiss();
            }
        });

        ordersRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        ordersRecyclerView.setAdapter(adapter);

        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }
}