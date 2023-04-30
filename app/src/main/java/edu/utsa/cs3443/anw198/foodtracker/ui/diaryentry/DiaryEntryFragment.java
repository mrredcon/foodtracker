package edu.utsa.cs3443.anw198.foodtracker.ui.diaryentry;

import android.app.AlertDialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.ScrollingMovementMethod;
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

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;
import java.util.Locale;

import edu.utsa.cs3443.anw198.foodtracker.R;
import edu.utsa.cs3443.anw198.foodtracker.databinding.FragmentDiaryEntryBinding;
import edu.utsa.cs3443.anw198.foodtracker.model.CompleteFood;
import edu.utsa.cs3443.anw198.foodtracker.model.Food;
import edu.utsa.cs3443.anw198.foodtracker.model.FoodDao;
import edu.utsa.cs3443.anw198.foodtracker.model.Nutrient;
import edu.utsa.cs3443.anw198.foodtracker.model.NutrientType;
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
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
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

                updateFoodInfo(baseUnitCalc);
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

    private void save() {
        TrackedFood trackedFood = new TrackedFood();
        trackedFood.foodId = completeFood.food.id;
        trackedFood.dateConsumed = new Date();

        String selectedItem = (String)dropdown.getSelectedItem();
        if (selectedItem == null) {
            return;
        }

        trackedFood.servingSizeId = completeFood.getServingSizeFromName(selectedItem).id;

        EditText quantityEditText = getView().findViewById(R.id.editTextQuantity);
        Double quantityInput = getQuantityInput(quantityEditText.getText());

        // User's quantity input is valid, go ahead and save
        if (quantityInput != null) {
            trackedFood.amount = quantityInput;
            // save it to db and go back one page in the UI
            FoodDao dao = DbProvider.getInstance().foodDao();

            Fragment frag = this;
            Thread thread = new Thread() {
                public void run() {
                    dao.insertTrackedFood(trackedFood);
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
        String[] choices = completeFood.getServingSizeNames();
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_dropdown_item, choices);
        dropdown.setAdapter(adapter);
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
        TextView textView = getView().findViewById(R.id.textViewSaveToDiary);
        textView.setMovementMethod(new ScrollingMovementMethod());

        // Food assumes 100 grams/mL/baseUnit
        double multiplier = quantity / Food.DEFAULT_QUANTITY;

        StringBuilder sb = new StringBuilder();
        sb.append("Name: ").append(completeFood.food.getName()).append("\n");
        sb.append("Calories: ");
        sb.append(String.format(Locale.getDefault(), "%.1f", completeFood.food.getCalories() * multiplier)).append("\n");

        sb.append("\nNutrients:\n");
        for (Nutrient nutrient : completeFood.nutrients) {
            NutrientType type = nutrient.nutrientType;
            String nutrientName = getContext().getText(type.stringResource).toString();
            sb.append("\t").append(nutrientName).append(": ");
            Double nutrientValue = new BigDecimal(nutrient.amount * multiplier)
                    .setScale(2, RoundingMode.HALF_UP).doubleValue();

            sb.append(nutrientValue).append(" ");
            String nutritionAbv = getContext().getText(Nutrient.NUTRIENT_UNITS.get(type).getAbbreviationResource()).toString();
            sb.append(nutritionAbv).append("\n");
        }

        textView.setText(sb.toString());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
