package com.example.testpharmacy; // Replace with your actual package name

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.testpharmacy.Database.MedicineDao;
import com.example.testpharmacy.Database.UserDao;

import java.util.ArrayList;
import java.util.List;

public class GenericCategoryFragment extends Fragment {

    private static final String ARG_CATEGORY_NAME = "category_name"; // Argument key for category name

    private RecyclerView medicineRecyclerView;
    private MedicineAdapter medicineAdapter;
    private List<Medicine> medicineList; // Placeholder for medicine data

    private MedicineDao medicineDao;

    /*@Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        medicineDao = new MedicineDao(getContext());
    }

    @Override
    public void onPause() {
        super.onPause();
        medicineDao.close();
    }*/

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        medicineDao = new MedicineDao(getContext());
        medicineDao.open(); // Open the database connection when fragment is created
    }

    @Override
    public void onResume() {
        super.onResume();
        medicineDao.open(); // Reopen if fragment comes back to foreground
    }

    @Override
    public void onPause() {
        super.onPause();
        medicineDao.close(); // Close when fragment is paused
    }

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
            medicines.addAll(createFirstAid()); // to test
            medicines.addAll(createAllMedicine());
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

    /*private List<Medicine> createAllMedicine() {
        List<Medicine> allMedicien = new ArrayList<>();
        allMedicien = medicineDao.getAllMedicines();
        return allMedicien;
    }*/

    private List<Medicine> createAllMedicine() {
        List<Medicine> allMedicien = new ArrayList<>();
        try {
            allMedicien = medicineDao.getAllMedicines();
        } catch (Exception e) {
            // Handle database error gracefully
            Log.e("GenericCategoryFragment", "Database error: " + e.getMessage());
        }
        return allMedicien;
    }

    private List<Medicine> createPainRelievers() {
        List<Medicine> painRelievers = new ArrayList<>();
        painRelievers = medicineDao.getMedicinesByCategory("Pain Relievers");
        return painRelievers;
    }

    private List<Medicine> createAntibiotics() {
        List<Medicine> antibiotics = new ArrayList<>();
        antibiotics = medicineDao.getMedicinesByCategory("Antibiotics");
        return antibiotics;
    }

    private List<Medicine> createVitamins() {
        List<Medicine> vitamins = new ArrayList<>();
        vitamins = medicineDao.getMedicinesByCategory("Vitamins");
        return vitamins;
    }

    private List<Medicine> createColdAndFlu() {
        List<Medicine> coldAndFlu = new ArrayList<>();
        coldAndFlu = medicineDao.getMedicinesByCategory("Cold & Flu");
        return coldAndFlu;
    }

    private List<Medicine> createFirstAid() {
        List<Medicine> firstAid = new ArrayList<>();
        firstAid.add(new Medicine("Bandages", 20.0, "box 50 pills", R.drawable.ic_placeholder_medicine, "","","", 100)); // Updated constructor call
        firstAid.add(new Medicine("Antiseptic Cream", 30.0, "box 50 pills", R.drawable.ic_placeholder_medicine, "","","", 100)); // Updated constructor call
        return firstAid;
    }
}