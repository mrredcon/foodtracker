package edu.utsa.cs3443.anw198.foodtracker.ui.diary;

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
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Locale;

import edu.utsa.cs3443.anw198.foodtracker.R;
import edu.utsa.cs3443.anw198.foodtracker.databinding.FragmentDiaryBinding;
import edu.utsa.cs3443.anw198.foodtracker.model.Food;
import edu.utsa.cs3443.anw198.foodtracker.model.Nutrient;
import edu.utsa.cs3443.anw198.foodtracker.model.NutrientType;
import edu.utsa.cs3443.anw198.foodtracker.model.ServingSize;
import edu.utsa.cs3443.anw198.foodtracker.providers.FoodProvider;
import edu.utsa.cs3443.anw198.foodtracker.providers.usda.UsdaFoodProvider;

public class DiaryFragment extends Fragment {
    private FragmentDiaryBinding binding;
    private AlertDialog dialog;
    private FoodProvider provider;
    private Food food;
    private List<ServingSize> servingSizes;
    private List<Nutrient> nutrients;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        //GalleryViewModel galleryViewModel = new ViewModelProvider(this).get(GalleryViewModel.class);

        binding = FragmentDiaryBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        //final TextView textView = binding.textGallery;
        //galleryViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);

        provider = new UsdaFoodProvider();

        setupAlertDialog();
        setupViewModel();

        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Spinner dropdown = getView().findViewById(R.id.spinnerServingSize);
        dropdown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String key = (String)adapterView.getSelectedItem();
                EditText quantityInput = getView().findViewById(R.id.editTextQuantity);

                if (key.equals(getContext().getText(food.getBaseUnit().getTitleResource()))) {
                    quantityInput.setText(String.valueOf(Food.DEFAULT_QUANTITY));
                } else {
                    quantityInput.setText(String.valueOf(1.0));
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {}
        });

        EditText quantityInput = getView().findViewById(R.id.editTextQuantity);
        quantityInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void afterTextChanged(Editable editable) {
                String key = (String) dropdown.getSelectedItem();
                if (key == null) return;

                TextView gramsDisplay = getView().findViewById(R.id.textViewGrams);
                String quantityString = editable.toString();
                if (quantityString.equals("")) return;

                double quantityInput = Double.parseDouble(quantityString);
                Double gramsInServing = getServingSizeAmount(key);
                if (gramsInServing == null) return;

                double baseUnitCalc = gramsInServing * quantityInput;
                String baseUnitAbv = getContext().getText(food.getBaseUnit().getAbbreviationResource()).toString();

                updateFoodInfo(baseUnitCalc);
                gramsDisplay.setText(baseUnitCalc + " " + baseUnitAbv);
            }
        });
    }

    private Double getServingSizeAmount(String name) {
        for (ServingSize servingSize : servingSizes) {
            if (servingSize.name.equals(name)) {
                return servingSize.amount;
            }
        }

        return null;
    }

    private void setupAlertDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setMessage("Loading food, please wait.");
        builder.setTitle("Loading...");
        builder.setCancelable(true);
        builder.setNegativeButton("Cancel", (dialogInterface, i) -> provider.cancelLoad());

        dialog = builder.create();
    }

    private void setupViewModel() {
        DiaryViewModel diaryViewModel = new ViewModelProvider(getActivity()).get(DiaryViewModel.class);

        diaryViewModel.getLoadStatus().observe(this, searchStatus -> {
            switch (searchStatus) {
                case IN_PROGRESS:
                    dialog.show();
                    break;
                case FAILURE:
                case SUCCESS:
                    dialog.dismiss();
                    break;
            }
        });

        diaryViewModel.getFood().observe(this, foodResult -> {
            this.food = foodResult.food;
            this.servingSizes = foodResult.servingSizes;
            this.nutrients = foodResult.nutrients;

            setupFoodServingSizes(); // Only do this once to avoid infinite loop
            updateFoodInfo(Food.DEFAULT_QUANTITY);
        });

        diaryViewModel.getErrorMessage().observe(this, errorMessage -> {
            Toast.makeText(getContext(), errorMessage, Toast.LENGTH_SHORT).show();
        });
    }

    void setupFoodServingSizes() {
        servingSizes.add(new ServingSize(getContext().getString(food.getBaseUnit().getTitleResource()), 1.0, 0));
        ((EditText)getView().findViewById(R.id.editTextQuantity)).setText(String.valueOf(Food.DEFAULT_QUANTITY));
        Spinner dropdown = getView().findViewById(R.id.spinnerServingSize);
        String[] choices = getServingSizeNames();
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_dropdown_item, choices);
        dropdown.setAdapter(adapter);
    }

    private String[] getServingSizeNames() {
        String[] names = new String[servingSizes.size()];
        for (int i = 0; i < names.length; i++) {
            names[i] = servingSizes.get(i).name;
        }

        return names;
    }

    private void updateFoodInfo(double quantity) {
        TextView textView = getView().findViewById(R.id.textViewSaveToDiary);
        textView.setMovementMethod(new ScrollingMovementMethod());

        // Food assumes 100 grams/mL/baseUnit
        double multiplier = quantity / Food.DEFAULT_QUANTITY;

        StringBuilder sb = new StringBuilder();
        sb.append("Name: ").append(food.getName()).append("\n");
        sb.append("Calories: ");
        sb.append(String.format(Locale.getDefault(), "%.1f", food.getCalories() * multiplier)).append("\n");

        sb.append("\nNutrients:\n");
        for (Nutrient nutrient : nutrients) {
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
