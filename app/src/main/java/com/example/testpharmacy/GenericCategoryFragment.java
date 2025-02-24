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

public class GenericCategoryFragment extends Fragment {

    private static final String ARG_CATEGORY_NAME = "category_name"; // Argument key for category name

    private RecyclerView medicineRecyclerView;
    private MedicineAdapter medicineAdapter;
    private List<Medicine> medicineList; // Placeholder for medicine data

    public GenericCategoryFragment() {
        // Required empty public constructor
    }

    // Factory method to create a new instance of GenericCategoryFragment with category name
    public static GenericCategoryFragment newInstance(String categoryName) {
        GenericCategoryFragment fragment = new GenericCategoryFragment();
        Bundle args = new Bundle();
        args.putString(ARG_CATEGORY_NAME, categoryName);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_generic_category, container, false); // Reusing fragment_category_pain_relievers.xml
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        medicineRecyclerView = view.findViewById(R.id.category_medicine_recycler_view);
        medicineRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2)); // 2 columns grid

        String categoryName = getArguments().getString(ARG_CATEGORY_NAME); // Get category name from arguments

        // Initialize placeholder medicine data based on category name
        medicineList = createPlaceholderMedicinesForCategory(categoryName);

        medicineAdapter = new MedicineAdapter(getContext(), medicineList);
        medicineRecyclerView.setAdapter(medicineAdapter);
    }

    // Placeholder method to create medicine data for a specific category
// Placeholder method to create medicine data for a specific category
    private List<Medicine> createPlaceholderMedicinesForCategory(String categoryName) {
        List<Medicine> medicines = new ArrayList<>();

        if (categoryName.equals("All Items")) {
            // Combine medicines from all categories for "All Items"
            medicines.addAll(createPainRelievers()); // Assuming you have helper methods like these
            medicines.addAll(createAntibiotics());
            medicines.addAll(createVitamins());
            medicines.addAll(createColdAndFlu());
            medicines.addAll(createFirstAid());
            // ... add calls to methods for other categories if you create them
        } else if (categoryName.equals("Pain Relievers")) {
            medicines.addAll(createPainRelievers()); // Use helper methods to create category-specific lists
        } else if (categoryName.equals("Antibiotics")) {
            medicines.addAll(createAntibiotics());
        } else if (categoryName.equals("Vitamins")) {
            medicines.addAll(createVitamins());
        } else if (categoryName.equals("Cold & Flu")) {
            medicines.addAll(createColdAndFlu());
        } else if (categoryName.equals("First Aid")) {
            medicines.addAll(createFirstAid());
        }
        // ... Add more categories and their medicines in else-if blocks

        // Default case (if category name is not recognized) - return empty list or some default medicines
        return medicines;
    }

// --- Helper methods to create lists for each category ---
// You can move these methods outside of createPlaceholderMedicinesForCategory for better organization

    private List<Medicine> createPainRelievers() {
        List<Medicine> painRelievers = new ArrayList<>();
        painRelievers.add(new Medicine("Aspirin", 50.0, "box 50 pills", R.drawable.ic_placeholder_medicine, "","","", 100)); // Updated constructor call
        painRelievers.add(new Medicine("Paracetamol", 30.0, "box 50 pills", R.drawable.ic_placeholder_medicine, "","","", 100)); // Updated constructor call
        painRelievers.add(new Medicine("Ibuprofen", 40.0, "box 50 pills", R.drawable.ic_placeholder_medicine, "","","", 100)); // Updated constructor call
        painRelievers.add(new Medicine("Naproxen", 60.0, "box 50 pills", R.drawable.ic_placeholder_medicine, "","","", 100)); // Updated constructor call
        return painRelievers;
    }

    private List<Medicine> createAntibiotics() {
        List<Medicine> antibiotics = new ArrayList<>();
        antibiotics.add(new Medicine("Amoxicillin", 70.0, "box 50 pills", R.drawable.ic_placeholder_medicine, "","","", 100)); // Updated constructor call
        antibiotics.add(new Medicine("Azithromycin", 90.0, "box 50 pills", R.drawable.ic_placeholder_medicine, "","","", 100)); // Updated constructor call
        antibiotics.add(new Medicine("Ciprofloxacin", 80.0, "box 50 pills", R.drawable.ic_placeholder_medicine, "","","", 100)); // Updated constructor call
        return antibiotics;
    }

    private List<Medicine> createVitamins() {
        List<Medicine> vitamins = new ArrayList<>();
        vitamins.add(new Medicine("Vitamin C", 25.0, "box 50 pills", R.drawable.ic_placeholder_medicine, "","","", 100)); // Updated constructor call
        vitamins.add(new Medicine("Vitamin D", 35.0, "box 50 pills", R.drawable.ic_placeholder_medicine, "","","", 100)); // Updated constructor call
        return vitamins;
    }

    private List<Medicine> createColdAndFlu() {
        List<Medicine> coldAndFlu = new ArrayList<>();
        coldAndFlu.add(new Medicine("Cough Syrup", 55.0, "box 50 pills", R.drawable.ic_placeholder_medicine, "","","", 100)); // Updated constructor call
        coldAndFlu.add(new Medicine("Nasal Spray", 45.0, "box 50 pills", R.drawable.ic_placeholder_medicine, "","","", 100)); // Updated constructor call
        return coldAndFlu;
    }

    private List<Medicine> createFirstAid() {
        List<Medicine> firstAid = new ArrayList<>();
        firstAid.add(new Medicine("Bandages", 20.0, "box 50 pills", R.drawable.ic_placeholder_medicine, "","","", 100)); // Updated constructor call
        firstAid.add(new Medicine("Antiseptic Cream", 30.0, "box 50 pills", R.drawable.ic_placeholder_medicine, "","","", 100)); // Updated constructor call
        return firstAid;
    }
}