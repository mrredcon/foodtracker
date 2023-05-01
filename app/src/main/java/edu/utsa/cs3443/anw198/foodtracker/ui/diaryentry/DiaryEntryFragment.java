package edu.utsa.cs3443.anw198.foodtracker.ui.diaryentry;

import android.app.AlertDialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Locale;

import edu.utsa.cs3443.anw198.foodtracker.R;
import edu.utsa.cs3443.anw198.foodtracker.adapter.NutrientSummaryAdapter;
import edu.utsa.cs3443.anw198.foodtracker.databinding.FragmentDiaryEntryBinding;
import edu.utsa.cs3443.anw198.foodtracker.model.CompleteFood;
import edu.utsa.cs3443.anw198.foodtracker.model.Food;
import edu.utsa.cs3443.anw198.foodtracker.model.FoodDao;
import edu.utsa.cs3443.anw198.foodtracker.model.ServingSize;
import edu.utsa.cs3443.anw198.foodtracker.model.TrackedFood;
import edu.utsa.cs3443.anw198.foodtracker.model.units.VolumeUnit;
import edu.utsa.cs3443.anw198.foodtracker.providers.DbProvider;
import edu.utsa.cs3443.anw198.foodtracker.providers.FoodProvider;
import edu.utsa.cs3443.anw198.foodtracker.providers.usda.UsdaFoodProvider;
import edu.utsa.cs3443.anw198.foodtracker.ui.TrackedFoodsViewModel;

public class DiaryEntryFragment extends Fragment {
    private FragmentDiaryEntryBinding binding;
    private AlertDialog loadFoodDialog;
    private FoodProvider provider;
    private CompleteFood completeFood;
    private String baseUnitName;
    private Spinner dropdown;
    private TrackedFoodsViewModel trackedFoodsViewModel;
    private ServingSize selectedServingSize;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentDiaryEntryBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        provider = new UsdaFoodProvider();

        loadFoodDialog = setupAlertDialog();
        setupViewModels();

        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        dropdown = getView().findViewById(R.id.spinnerServingSize);
        dropdown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                selectedServingSize = completeFood.servingSizes.get(position);
                String key = (String)adapterView.getSelectedItem();
                EditText quantityInput = getView().findViewById(R.id.editTextQuantity);

                // If the selected dropdown item is the base unit (like grams),
                // set the suggested amount to default quantity (like 100 grams)
                if (key.equals(baseUnitName)) {
                    quantityInput.setText(String.valueOf(Food.DEFAULT_QUANTITY));
                } else {
                    quantityInput.setText(String.valueOf(1.0));
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {}
        });

        EditText quantityEditText = getView().findViewById(R.id.editTextQuantity);
        quantityEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void afterTextChanged(Editable editable) {
                String key = (String) dropdown.getSelectedItem();
                if (key == null) return;

                TextView gramsDisplay = getView().findViewById(R.id.textViewGrams);

                Double gramsInServing = getServingSizeAmount(key);
                if (gramsInServing == null) return;

                Double quantityInput = getQuantityInput(editable);
                if (quantityInput == null) return;

                double baseUnitCalc = gramsInServing * quantityInput;

                String baseUnitAbv = getContext().getText(completeFood.food.getBaseUnit().getAbbreviationResource()).toString();

                updateFoodInfo(quantityInput);
                gramsDisplay.setText(baseUnitCalc + " " + baseUnitAbv);
            }
        });

        Button saveButton = getView().findViewById(R.id.buttonSaveToDiary);
        saveButton.setOnClickListener(btn -> {
            save();
        });
    }

    private Double getQuantityInput(Editable editable) {
        String quantityString = editable.toString();
        if (quantityString.equals("")) return null;

        double quantityInput;
        try {
            quantityInput = Double.parseDouble(quantityString);
        } catch (Exception e) {
            return null;
        }

        return quantityInput;
    }

    private TrackedFood buildTrackedFood(double quantity) {
        TrackedFood trackedFood = new TrackedFood();
        trackedFood.foodId = completeFood.food.id;
        trackedFood.dateConsumed = new Date();
        trackedFood.servingSizeId = selectedServingSize.id;
        trackedFood.amount = quantity;
        return trackedFood;
    }

    private void save() {
        EditText quantityEditText = getView().findViewById(R.id.editTextQuantity);
        Double quantityInput = getQuantityInput(quantityEditText.getText());

        // User's quantity input is valid, go ahead and save
        if (quantityInput != null) {
            // save it to db and go back one page in the UI
            FoodDao dao = DbProvider.getInstance().foodDao();

            Fragment frag = this;
            Thread thread = new Thread() {
                public void run() {
                    dao.insertTrackedFood(buildTrackedFood(quantityInput));
                    trackedFoodsViewModel.reloadData();

                    getActivity().runOnUiThread(() -> {
                        NavController navController = NavHostFragment.findNavController(frag);
                        navController.navigateUp();
                    });
                }
            };
            thread.start();
        }
    }

    private Double getServingSizeAmount(String name) {
        for (ServingSize servingSize : completeFood.servingSizes) {
            if (servingSize.name.equals(name)) {
                return servingSize.amount;
            }
        }

        return null;
    }

    private AlertDialog setupAlertDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setMessage("Loading food, please wait.");
        builder.setTitle("Loading...");
        builder.setCancelable(true);
        builder.setNegativeButton("Cancel", (dialogInterface, i) -> provider.cancelLoad());

        return builder.create();
    }

    private void setupViewModels() {
        DiaryEntryViewModel diaryEntryViewModel = new ViewModelProvider(getActivity()).get(DiaryEntryViewModel.class);
        trackedFoodsViewModel = new ViewModelProvider(getActivity()).get(TrackedFoodsViewModel.class);

        diaryEntryViewModel.getLoadStatus().observe(this, searchStatus -> {
            switch (searchStatus) {
                case IN_PROGRESS:
                    loadFoodDialog.show();
                    break;
                case FAILURE:
                case SUCCESS:
                    loadFoodDialog.dismiss();
                    break;
            }
        });

        diaryEntryViewModel.getFood().observe(this, foodResult -> {
            this.completeFood = foodResult;
            this.baseUnitName = getContext().getString(completeFood.food.getBaseUnit().getTitleResource());

            setupFoodServingSizes(); // Only do this once to avoid infinite loop
            updateFoodInfo(Food.DEFAULT_QUANTITY);
        });

        diaryEntryViewModel.getErrorMessage().observe(this, errorMessage -> {
            Toast.makeText(getContext(), errorMessage, Toast.LENGTH_SHORT).show();
        });
    }

    void setupFoodServingSizes() {
        // TODO: handle adding default units
        //servingSizes.add(new ServingSize(getContext().getString(food.getBaseUnit().getTitleResource()), 1.0, 0));
        ((EditText)getView().findViewById(R.id.editTextQuantity)).setText(String.valueOf(Food.DEFAULT_QUANTITY));
        Spinner dropdown = getView().findViewById(R.id.spinnerServingSize);

        int size = completeFood.servingSizes.size();
        if (size == 0) {
            completeFood.servingSizes.add(new ServingSize(baseUnitName, 1.0));
            size = 1;
        }

        String[] servingSizeNames = new String[size];
        for (int i = 0; i < size; i++) {
            ServingSize servingSize = completeFood.servingSizes.get(i);
            servingSizeNames[i] = servingSize.name;
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(),
                android.R.layout.simple_spinner_dropdown_item,
                servingSizeNames);

        dropdown.setAdapter(adapter);
        selectedServingSize = completeFood.servingSizes.get(0);
    }

    private String[] getBuiltInUnits() {
        String[] unitNames;
        if (completeFood.food.isMeasuredInVolume()) {
            // handle volume
            VolumeUnit[] units = VolumeUnit.values();
            // TODO: finish me
        } else {
            // handle mass
        }
        return null;
    }

    private void updateFoodInfo(double quantity) {
        LinkedHashMap<TrackedFood, CompleteFood> foods = new LinkedHashMap<>();
        TrackedFood trackedFood = buildTrackedFood(quantity);
        foods.put(trackedFood, completeFood);

        TextView title = getView().findViewById(R.id.diaryEntryTitle);
        title.setText(completeFood.food.getName());

        double calories = trackedFood.getMultiplier(completeFood) * completeFood.food.getCalories();
        TextView caloriesText = getView().findViewById(R.id.diaryEntryCalories);
        caloriesText.setText(String.format(Locale.US, "%,.0f", calories));

        RecyclerView recyclerView = getView().findViewById(R.id.diaryEntryRecyclerView);
        recyclerView.setAdapter(new NutrientSummaryAdapter(getContext(), foods));
        // Show every nutrient, if the user has not ate any of it, just put 0.
        recyclerView.setHasFixedSize(true);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
