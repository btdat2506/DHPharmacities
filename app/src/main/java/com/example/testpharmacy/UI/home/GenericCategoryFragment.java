package com.example.testpharmacy.UI.home;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
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
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class GenericCategoryFragment extends Fragment {

    private static final String ARG_CATEGORY_NAME = "category_name"; // Argument key for category name
    private static final String TAG = "GenericCategoryFragment";

    private RecyclerView medicineRecyclerView;
    private MedicineAdapter medicineAdapter;
    private List<Medicine> allMedicineList; // All medicines in this category
    private List<Medicine> filteredMedicineList; // Filtered medicines to display
    private TextView noResultsTextView;
    private Spinner sortSpinner;

    private int currentSortOption = 0; // Default sort option
    private String currentSearchQuery = ""; // Current search query

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
        sortSpinner = view.findViewById(R.id.category_sort_spinner);

        medicineRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2)); // 2 columns grid

        // Load all medicines for this category
        allMedicineList = loadMedicinesForCategory(categoryName);
        filteredMedicineList = new ArrayList<>(allMedicineList);

        // Setup sort spinner
        setupSortSpinner();

        medicineAdapter = new MedicineAdapter(getContext(), filteredMedicineList);
        medicineRecyclerView.setAdapter(medicineAdapter);
    }

    private void setupSortSpinner() {

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.product_sort_options, android.R.layout.simple_spinner_item);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sortSpinner.setAdapter(adapter);

        // Handle selection changes
        sortSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                currentSortOption = position;
                applyFilterAndSort();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Do nothing
            }
        });
    }

    // Combined method to apply both filtering and sorting
    private void applyFilterAndSort() {
        // First apply filter
        if (currentSearchQuery.isEmpty()) {
            // If no search query, use all medicines
            filteredMedicineList = new ArrayList<>(allMedicineList);
        } else {
            // Filter medicines based on query
            filteredMedicineList = new ArrayList<>();
            String query = currentSearchQuery.toLowerCase().trim();

            for (Medicine medicine : allMedicineList) {
                // Search in name, description, category
                if (medicine.getName().toLowerCase().contains(query) ||
                        medicine.getDescription().toLowerCase().contains(query) ||
                        medicine.getCategory().toLowerCase().contains(query)) {
                    filteredMedicineList.add(medicine);
                }
            }
        }

        // Now apply sorting
        switch (currentSortOption) {
            case 0: // Name (A-Z)
                Collections.sort(filteredMedicineList, new Comparator<Medicine>() {
                    @Override
                    public int compare(Medicine m1, Medicine m2) {
                        return m1.getName().compareToIgnoreCase(m2.getName());
                    }
                });
                break;

            case 1: // Name (Z-A)
                Collections.sort(filteredMedicineList, new Comparator<Medicine>() {
                    @Override
                    public int compare(Medicine m1, Medicine m2) {
                        return m2.getName().compareToIgnoreCase(m1.getName());
                    }
                });
                break;

            case 2: // Price (Low to High)
                Collections.sort(filteredMedicineList, new Comparator<Medicine>() {
                    @Override
                    public int compare(Medicine m1, Medicine m2) {
                        return Double.compare(m1.getPrice(), m2.getPrice());
                    }
                });
                break;

            case 3: // Price (High to Low)
                Collections.sort(filteredMedicineList, new Comparator<Medicine>() {
                    @Override
                    public int compare(Medicine m1, Medicine m2) {
                        return Double.compare(m2.getPrice(), m1.getPrice());
                    }
                });
                break;

            default: // Default - anti-bug
                break;
        }

        // Update adapter and UI
        medicineAdapter = new MedicineAdapter(getContext(), filteredMedicineList);
        medicineRecyclerView.setAdapter(medicineAdapter);

        // Update visibility based on results
        if (filteredMedicineList.isEmpty()) {
            noResultsTextView.setVisibility(View.VISIBLE);
            medicineRecyclerView.setVisibility(View.GONE);
        } else {
            noResultsTextView.setVisibility(View.GONE);
            medicineRecyclerView.setVisibility(View.VISIBLE);
        }
    }

    public void filterMedicines(String query) {
        currentSearchQuery = query;
        applyFilterAndSort();
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