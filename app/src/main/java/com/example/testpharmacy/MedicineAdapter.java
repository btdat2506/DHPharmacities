package com.example.testpharmacy; // Replace with your actual package name

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class MedicineAdapter extends RecyclerView.Adapter<MedicineAdapter.MedicineViewHolder> {

    private Context context;
    private List<Medicine> medicineList;

    public MedicineAdapter(Context context, List<Medicine> medicineList) {
        this.context = context;
        this.medicineList = medicineList;
    }

    @NonNull
    @Override
    public MedicineViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_medicine, parent, false);
        return new MedicineViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MedicineViewHolder holder, int position) {
        Medicine medicine = medicineList.get(position);
        holder.medicineNameTextView.setText(medicine.getName());
        holder.medicinePriceTextView.setText(String.format("%.3f", medicine.getPrice()) + "đ"); // Format price
        holder.medicineImageView.setImageResource(medicine.getImageResourceId()); // Set image resource

        holder.addToCartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: Implement Add to Cart functionality
                Toast.makeText(context, "Added " + medicine.getName() + " to cart!", Toast.LENGTH_SHORT).show();
                // You'll need to manage the cart data (e.g., using a CartManager class or similar)
            }
        });

        // Add click listener to the entire item view to open ProductDetailActivity
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ProductDetailActivity.class);
                intent.putExtra(ProductDetailActivity.EXTRA_MEDICINE, medicine); // Pass the Medicine object
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return medicineList.size();
    }

    public static class MedicineViewHolder extends RecyclerView.ViewHolder {
        ImageView medicineImageView;
        TextView medicineNameTextView;
        TextView medicinePriceTextView;
        Button addToCartButton;

        public MedicineViewHolder(@NonNull View itemView) {
            super(itemView);
            medicineImageView = itemView.findViewById(R.id.medicine_image_view);
            medicineNameTextView = itemView.findViewById(R.id.medicine_name_text_view);
            medicinePriceTextView = itemView.findViewById(R.id.medicine_price_text_view);
            addToCartButton = itemView.findViewById(R.id.add_to_cart_button);
        }
    }
}