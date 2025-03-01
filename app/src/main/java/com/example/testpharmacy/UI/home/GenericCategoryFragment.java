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

import com.example.testpharmacy.Constants.CategoryConstants;
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
        allMedicineList = loadMedicinesForCategory(categoryName);
        filteredMedicineList = new ArrayList<>(allMedicineList);

        medicineAdapter = new MedicineAdapter(getContext(), filteredMedicineList);
        medicineRecyclerView.setAdapter(medicineAdapter);
    }

    public void filterMedicines(String query) {
        if (allMedicineList == null || medicineAdapter == null) return;

        // If query is empty, show all medicines
        if (query.isEmpty()) {
            filteredMedicineList = new ArrayList<>(allMedicineList);
            medicineAdapter = new MedicineAdapter(getContext(), filteredMedicineList);
            medicineRecyclerView.setAdapter(medicineAdapter);

            // Update visibility
            noResultsTextView.setVisibility(View.GONE);
            medicineRecyclerView.setVisibility(View.VISIBLE);
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
        filteredMedicineList = filteredList;
        medicineAdapter = new MedicineAdapter(getContext(), filteredMedicineList);
        medicineRecyclerView.setAdapter(medicineAdapter);

        // Update visibility based on results
        if (filteredList.isEmpty()) {
            noResultsTextView.setVisibility(View.VISIBLE);
            medicineRecyclerView.setVisibility(View.GONE);
        } else {
            noResultsTextView.setVisibility(View.GONE);
            medicineRecyclerView.setVisibility(View.VISIBLE);
        }
    }

    private List<Medicine> loadMedicinesForCategory(String categoryName) {
        // Get the string resource ID for "All Items" category
        String allItemsCategory = getString(R.string.category_all_items);

        try {
            if (categoryName.equals(allItemsCategory)) {
                // For "All Items" category, get all medicines
                return medicineDao.getAllMedicines();
            } else {
                // Convert UI category name to database category name
                String databaseCategory = CategoryConstants.getDatabaseCategory(getContext(), categoryName);

                // Get medicines for the specific category
                return medicineDao.getMedicinesByCategory(databaseCategory);
            }
        } catch (Exception e) {
            // Handle database error gracefully
            Log.e(TAG, String.format(getString(R.string.database_error_message), e.getMessage()));
            return new ArrayList<>(); // Return empty list on error
        }
    }
}