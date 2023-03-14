package edu.utsa.cs3443.anw198.foodtracker.ui.searchfood;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import edu.utsa.cs3443.anw198.foodtracker.FoodSearchListener;
import edu.utsa.cs3443.anw198.foodtracker.databinding.FragmentSearchfoodBinding;
import edu.utsa.cs3443.anw198.foodtracker.model.FoodSearchResult;
import edu.utsa.cs3443.anw198.foodtracker.model.usda.UsdaSearchResult;
import edu.utsa.cs3443.anw198.foodtracker.model.usda.UsdaSearchResultFood;
import edu.utsa.cs3443.anw198.foodtracker.model.usda.UsdaSearchResultFoodNutrient;
import edu.utsa.cs3443.anw198.foodtracker.providers.usda.UsdaFoodSearchProvider;
import edu.utsa.cs3443.anw198.foodtracker.providers.usda.UsdaSearchService;
import edu.utsa.cs3443.anw198.foodtracker.providers.usda.UsdaServiceGenerator;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchFoodFragment extends Fragment implements FoodSearchListener {

    private FragmentSearchfoodBinding binding;
    
    private AlertDialog dialog;
    private TextView textView;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        SearchFoodViewModel searchFoodViewModel =
                new ViewModelProvider(this).get(SearchFoodViewModel.class);

        binding = FragmentSearchfoodBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        textView = binding.textSearchfood;
        searchFoodViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);

        final Button button = binding.buttonSearchFood;
        final EditText query = binding.editTextSearchQuery;

        UsdaFoodSearchProvider provider = new UsdaFoodSearchProvider();

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setMessage("Searching database, please wait.");
        builder.setTitle("Searching...");
        builder.setCancelable(true);
        builder.setNegativeButton("Cancel", (dialogInterface, i) -> provider.cancelSearch());

        dialog = builder.create();
        
        button.setOnClickListener(view ->
        {
            provider.searchFoods(query.getText().toString(), this);
            dialog.show();
        });

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onResponse(FoodSearchResult[] results) {
        dialog.dismiss();
        StringBuilder sb = new StringBuilder();
        for (FoodSearchResult food : results) {
            sb.append(food.getName() + " / Calories: " + food.getCalories() +
                    " / Fats: " + food.getFat() +
                    " / Carbs: " + food.getCarbs() +
                    " / Protein: " + food.getProtein() + "\n");
        }

        textView.setText(sb.toString());
    }

    @Override
    public void onFailure(Throwable error) {
        dialog.dismiss();
        Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
    }
}