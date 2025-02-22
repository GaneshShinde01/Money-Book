package com.gs.moneybook.Fragments;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;
import com.gs.moneybook.R;
import com.gs.moneybook.databinding.FragmentAddIncomeBinding;
import java.util.ArrayList;

public class AddIncomeFragment extends Fragment {
    FragmentAddIncomeBinding binding;
    private ArrayList<String> categories;
    private ArrayAdapter<String> spinnerAdapter;
    private ArrayAdapter<String> autoCompleteAdapter;

    public AddIncomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentAddIncomeBinding.inflate(inflater, container, false);

        // Initialize categories
        categories = new ArrayList<>();
        categories.add("Category 1");
        categories.add("Category 2");
        categories.add("Category 3");
        categories.add("Add new category"); // Option to add a new category

        // Set up spinner adapter
        spinnerAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, categories);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.categorySpinner.setAdapter(spinnerAdapter);

        // Set up AutoCompleteTextView adapter (same as spinner options)
        autoCompleteAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_dropdown_item_1line, categories);
        binding.autoCompleteEditText.setAdapter(autoCompleteAdapter);
        binding.autoCompleteEditText.setThreshold(1); // Start showing suggestions after typing one character

        // Handle spinner item selection
        binding.categorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedCategory = categories.get(position);
                if (selectedCategory.equals("Add new category")) {
                    // Handle adding a new category
                    String newCategory = binding.autoCompleteEditText.getText().toString().trim();
                    if (!newCategory.isEmpty() && !categories.contains(newCategory)) {
                        categories.add(categories.size() - 1, newCategory); // Add before 'Add new category'
                        spinnerAdapter.notifyDataSetChanged();
                        autoCompleteAdapter.notifyDataSetChanged();
                        binding.autoCompleteEditText.setText(""); // Clear input after addition
                        Toast.makeText(getActivity(), "New category added: " + newCategory, Toast.LENGTH_SHORT).show();
                    } else if (newCategory.isEmpty()) {
                        Toast.makeText(getActivity(), "Please enter a category name", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getActivity(), "Category already exists", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        return binding.getRoot();
    }
}
