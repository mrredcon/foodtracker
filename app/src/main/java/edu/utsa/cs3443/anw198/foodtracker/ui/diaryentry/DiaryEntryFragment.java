package edu.utsa.cs3443.anw198.foodtracker.ui.diaryentry;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.RecyclerView;

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
import edu.utsa.cs3443.anw198.foodtracker.ui.TrackedFoodsViewModel;

public class DiaryEntryFragment extends Fragment {
    private FragmentDiaryEntryBinding binding;
    private AlertDialog loadFoodDialog;
    private CompleteFood completeFood;
    private String baseUnitName;
    private Spinner dropdown;
    private TrackedFoodsViewModel trackedFoodsViewModel;
    private DiaryEntryViewModel diaryEntryViewModel;
    private ServingSize selectedServingSize;
    private boolean editing;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onPrepareOptionsMenu(@NonNull Menu menu) {
        super.onPrepareOptionsMenu(menu);
        menu.findItem(R.id.action_calendar).setVisible(true);
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentDiaryEntryBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        loadFoodDialog = setupAlertDialog();
        setupViewModels();

        return root;
    }

    private void navigateUp(Fragment fragment) {
        NavController navController = NavHostFragment.findNavController(fragment);
        navController.navigateUp();
    }

    private void setupDeleteButton() {
        Button deleteButton = getView().findViewById(R.id.buttonDeleteEntry);
        if (editing) {
            TrackedFood tf = diaryEntryViewModel.getTrackedFood();
            deleteButton.setVisibility(View.VISIBLE);
            deleteButton.setOnClickListener(view -> {
                trackedFoodsViewModel.deleteTrackedFood(tf);
                navigateUp(this);
            });
        } else {
            deleteButton.setVisibility(View.GONE);
        }
    }

    private void setupSaveButton() {
        Button saveButton = getView().findViewById(R.id.buttonSaveToDiary);
        saveButton.setOnClickListener(btn -> save());
        saveButton.setText(getString(R.string.save));
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        dropdown = getView().findViewById(R.id.spinnerServingSize);
        dropdown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                selectedServingSize = completeFood.servingSizes.get(position);
                EditText quantityInput = getView().findViewById(R.id.editTextQuantity);
                if (!editing) {
                    if (selectedServingSize.name.equals(baseUnitName)) {
                        quantityInput.setText(String.valueOf(Food.DEFAULT_QUANTITY));
                    } else {
                        quantityInput.setText(String.valueOf(1.0));
                    }
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
        trackedFood.dateConsumed = trackedFoodsViewModel.getDate().getTime();
        trackedFood.servingSizeId = selectedServingSize.id;
        trackedFood.amount = quantity;
        return trackedFood;
    }

    private void hideKeyboard() {
        // Check if no view has focus:
        Activity activity = this.getActivity();
        View view = activity == null ? null : activity.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    private void save() {
        EditText quantityEditText = getView().findViewById(R.id.editTextQuantity);
        Double quantityInput = getQuantityInput(quantityEditText.getText());

        // User's quantity input is valid, go ahead and save
        if (quantityInput != null) {
            hideKeyboard();
            // save it to db and go back one page in the UI
            FoodDao dao = DbProvider.getInstance().foodDao();

            Fragment frag = this;
            Thread thread = new Thread() {
                public void run() {
                    TrackedFood newTrackedFood = buildTrackedFood(quantityInput);
                    TrackedFood oldTrackedFood = diaryEntryViewModel.getTrackedFood();
                    if (editing) {
                        newTrackedFood.id = oldTrackedFood.id;
                        newTrackedFood.dateConsumed = oldTrackedFood.dateConsumed;
                    }

                    dao.insertTrackedFood(newTrackedFood);

                    trackedFoodsViewModel.reloadData();

                    getActivity().runOnUiThread(() -> {
                        navigateUp(frag);
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
        builder.setOnCancelListener(dialogInterface -> diaryEntryViewModel.cancelSearch());
        builder.setNegativeButton("Cancel", (dialogInterface, i) -> {
           dialogInterface.cancel();
        });

        return builder.create();
    }

    private void setupViewModels() {
        diaryEntryViewModel = new ViewModelProvider(getActivity()).get(DiaryEntryViewModel.class);
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
            this.editing = diaryEntryViewModel.isEditingEntry();

            setupSaveButton();
            setupDeleteButton();
            setupFoodServingSizes(); // Only do this once to avoid infinite loop
            if (editing) {
                EditText quantityInput = getView().findViewById(R.id.editTextQuantity);
                double amt = diaryEntryViewModel.getTrackedFood().amount;
                quantityInput.setText(String.valueOf(amt));
                updateFoodInfo(amt);
            } else {
                updateFoodInfo(1.0);
            }
        });

        diaryEntryViewModel.getErrorMessage().observe(this, errorMessage -> {
            TextView title = getView().findViewById(R.id.diaryEntryTitle);
            title.setText(getString(R.string.diary_entry_error, errorMessage));

            Button saveButton = getView().findViewById(R.id.buttonSaveToDiary);
            saveButton.setOnClickListener(btn -> navigateUp(this));
            saveButton.setText(getString(R.string.cancel));

            Button deleteButton = getView().findViewById(R.id.buttonDeleteEntry);
            deleteButton.setVisibility(View.GONE);
        });
    }

    void setupFoodServingSizes() {
        // TODO: handle adding default units
        //servingSizes.add(new ServingSize(getContext().getString(food.getBaseUnit().getTitleResource()), 1.0, 0));
        ((EditText)getView().findViewById(R.id.editTextQuantity)).setText(String.valueOf(1.0));
        Spinner dropdown = getView().findViewById(R.id.spinnerServingSize);

        int size = completeFood.servingSizes.size();

        int selectedIndex = 0;
        selectedServingSize = completeFood.servingSizes.get(0);
        String[] servingSizeNames = new String[size];
        TrackedFood tf = diaryEntryViewModel.getTrackedFood();

        for (int i = 0; i < size; i++) {
            ServingSize servingSize = completeFood.servingSizes.get(i);
            servingSizeNames[i] = servingSize.name;
            if (editing) {
                if (tf.servingSizeId == servingSize.id) {
                    selectedServingSize = servingSize;
                    selectedIndex = i;
                }
            }
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(),
                android.R.layout.simple_spinner_dropdown_item,
                servingSizeNames);

        dropdown.setAdapter(adapter);
        dropdown.setSelection(selectedIndex);
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

    private ServingSize getServingSizeByName(String name, CompleteFood completeFood) {
        for (ServingSize ss : completeFood.servingSizes) {
            if (ss.name.equals(name)) {
                return ss;
            }
        }
        return null;
    }

    private void updateFoodInfo(double quantity) {
        LinkedHashMap<TrackedFood, CompleteFood> foods = new LinkedHashMap<>();
        TrackedFood trackedFood = buildTrackedFood(quantity);
        foods.put(trackedFood, completeFood);

        TextView title = getView().findViewById(R.id.diaryEntryTitle);
        title.setText(completeFood.food.getName());

        // TODO: For some reason there was an ID mismatch between the trackedFood serving size ID
        //  and the ones contained within completeFood when using the commented out code
        //double calories = trackedFood.getMultiplier(completeFood) * completeFood.food.getCalories();

        String servingSizeName = (String)dropdown.getSelectedItem();
        ServingSize servingSize = getServingSizeByName(servingSizeName, completeFood);
        double amountConsumed = servingSize.amount * trackedFood.amount;
        double multiplier = amountConsumed / Food.DEFAULT_QUANTITY;
        double calories = multiplier * completeFood.food.getCalories();

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
