package com.example.testpharmacy; // Replace with your actual package name

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

public class CategoryPainRelieversFragment extends Fragment {

    private RecyclerView medicineRecyclerView;
    private MedicineAdapter medicineAdapter;
    private List<Medicine> medicineList; // Placeholder for medicine data

    public CategoryPainRelieversFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_category_pain_relievers, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        medicineRecyclerView = view.findViewById(R.id.category_medicine_recycler_view);
        medicineRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2)); // 2 columns grid

        // Initialize placeholder medicine data
        medicineList = createPlaceholderMedicines();

        medicineAdapter = new MedicineAdapter(getContext(), medicineList);
        medicineRecyclerView.setAdapter(medicineAdapter);
    }

    // Placeholder method to create medicine data (replace with actual data loading)
    private List<Medicine> createPlaceholderMedicines() {
        List<Medicine> medicines = new ArrayList<>();
        medicines.add(new Medicine("Aspirin", 50.0, R.drawable.ic_placeholder_medicine));
        medicines.add(new Medicine("Paracetamol", 30.0, R.drawable.ic_placeholder_medicine));
        medicines.add(new Medicine("Ibuprofen", 40.0, R.drawable.ic_placeholder_medicine));
        medicines.add(new Medicine("Naproxen", 60.0, R.drawable.ic_placeholder_medicine));
        // ... add more placeholder medicines
        return medicines;
    }
}