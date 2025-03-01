package com.example.testpharmacy.UI.home;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.testpharmacy.Database.MedicineDao;
import com.example.testpharmacy.Model.Medicine;
import com.example.testpharmacy.R;

import java.util.ArrayList;
import java.util.List;

public class GenericCategoryFragment extends Fragment {

    private static final String ARG_CATEGORY_NAME = "category_name"; // Argument key for category name
    private static final String TAG = "GenericCategoryFragment";

    private RecyclerView medicineRecyclerView;
    private MedicineAdapter medicineAdapter;
    private List<Medicine> allMedicineList; // All medicines in this category
    private List<Medicine> filteredMedicineList; // Filtered medicines to display
    private TextView noResultsTextView;

    private MedicineDao medicineDao;
    private String categoryName;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        medicineDao = new MedicineDao(getContext());
        medicineDao.open(); // Open the database connection when fragment is created
        
        if (getArguments() != null) {
            categoryName = getArguments().getString(ARG_CATEGORY_NAME);
        }
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
        return inflater.inflate(R.layout.fragment_generic_category, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        medicineRecyclerView = view.findViewById(R.id.category_medicine_recycler_view);
        noResultsTextView = view.findViewById(R.id.no_results_text_view);

        medicineRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2)); // 2 columns grid

        // Load all medicines for this category
        allMedicineList = createPlaceholderMedicinesForCategory(categoryName);
        filteredMedicineList = new ArrayList<>(allMedicineList);

        medicineAdapter = new MedicineAdapter(getContext(), filteredMedicineList);
        medicineRecyclerView.setAdapter(medicineAdapter);
    }

    public void filterMedicines(String query) {
        if (allMedicineList == null || medicineAdapter == null) return;

        // If query is empty, show all medicines
        if (query.isEmpty()) {
            medicineAdapter = new MedicineAdapter(getContext(), allMedicineList);
            medicineRecyclerView.setAdapter(medicineAdapter);
            return;
        }

        // Filter medicines based on query
        List<Medicine> filteredList = new ArrayList<>();
        query = query.toLowerCase().trim();

        for (Medicine medicine : allMedicineList) {
            // Search in name, description, category
            if (medicine.getName().toLowerCase().contains(query) ||
                    medicine.getDescription().toLowerCase().contains(query) ||
                    medicine.getCategory().toLowerCase().contains(query)) {
                filteredList.add(medicine);
            }
        }

        // Update adapter with filtered list
        MedicineAdapter filteredAdapter = new MedicineAdapter(getContext(), filteredList);
        medicineRecyclerView.setAdapter(filteredAdapter);
        //updateNoResultsView();
        if (noResultsTextView != null) {
            if (filteredList.isEmpty()) {
                noResultsTextView.setVisibility(View.VISIBLE);
                medicineRecyclerView.setVisibility(View.GONE);
            } else {
                noResultsTextView.setVisibility(View.GONE);
                medicineRecyclerView.setVisibility(View.VISIBLE);
            }
        }
    }

    // Placeholder method to create medicine data for a specific category
    private List<Medicine> createPlaceholderMedicinesForCategory(String categoryName) {
        List<Medicine> medicines = new ArrayList<>();

        if (categoryName.equals(getString(R.string.category_all_items))) {
            // Combine medicines from all categories for "All Items"
            medicines.addAll(createAllMedicine());
            // ... add calls to methods for other categories if you create them
        } else if (categoryName.equals(getString(R.string.category_pain_relievers))) {
            medicines.addAll(createPainRelievers()); // Use helper methods to create category-specific lists
        } else if (categoryName.equals(getString(R.string.category_antibiotics))) {
            medicines.addAll(createAntibiotics());
        } else if (categoryName.equals(getString(R.string.category_vitamins))) {
            medicines.addAll(createVitamins());
        } else if (categoryName.equals(getString(R.string.category_cold_flu))) {
            medicines.addAll(createColdAndFlu());
        } else if (categoryName.equals(getString(R.string.category_first_aid))) {
            medicines.addAll(createFirstAid());
        }
        // ... Add more categories and their medicines in else-if blocks

        // Default case (if category name is not recognized) - return empty list or some default medicines
        return medicines;
    }

    private List<Medicine> createAllMedicine() {
        List<Medicine> allMedicine = new ArrayList<>();
        try {
            allMedicine = medicineDao.getAllMedicines();
        } catch (Exception e) {
            // Handle database error gracefully
            Log.e(TAG, getString(R.string.database_error_message) + e.getMessage());
        }
        return allMedicine;
    }

    private List<Medicine> createPainRelievers() {
        return medicineDao.getMedicinesByCategory(getString(R.string.category_pain_relievers));
    }

    private List<Medicine> createAntibiotics() {
        return medicineDao.getMedicinesByCategory(getString(R.string.category_antibiotics));
    }

    private List<Medicine> createVitamins() {
        return medicineDao.getMedicinesByCategory(getString(R.string.category_vitamins));
    }

    private List<Medicine> createColdAndFlu() {
        return medicineDao.getMedicinesByCategory(getString(R.string.category_cold_flu));
    }

    private List<Medicine> createFirstAid() {
        return medicineDao.getMedicinesByCategory(getString(R.string.category_first_aid));
    }
}