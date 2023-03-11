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

import edu.utsa.cs3443.anw198.foodtracker.databinding.FragmentSearchfoodBinding;
import edu.utsa.cs3443.anw198.foodtracker.model.usda.UsdaSearchResult;
import edu.utsa.cs3443.anw198.foodtracker.model.usda.UsdaSearchResultFood;
import edu.utsa.cs3443.anw198.foodtracker.model.usda.UsdaSearchResultFoodNutrient;
import edu.utsa.cs3443.anw198.foodtracker.providers.UsdaSearchService;
import edu.utsa.cs3443.anw198.foodtracker.providers.UsdaServiceGenerator;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchFoodFragment extends Fragment {

    private FragmentSearchfoodBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        SearchFoodViewModel searchFoodViewModel =
                new ViewModelProvider(this).get(SearchFoodViewModel.class);

        binding = FragmentSearchfoodBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.textSearchfood;
        searchFoodViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);

        final Button button = binding.buttonSearchFood;
        final EditText query = binding.editTextSearchQuery;

        button.setOnClickListener(view ->
        {
            UsdaSearchService service = UsdaServiceGenerator.createService(UsdaSearchService.class);
            Call<UsdaSearchResult> callAsync = service.searchFoods(query.getText().toString());
            AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
            builder.setMessage("Searching USDA FoodData Central, please wait.");
            builder.setTitle("Searching...");
            builder.setCancelable(true);
            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    callAsync.cancel();
                    dialogInterface.cancel();
                }
            });

            AlertDialog dialog = builder.create();
            dialog.show();

            callAsync.enqueue(new Callback<UsdaSearchResult>() {
                @Override
                public void onResponse(Call<UsdaSearchResult> call, Response<UsdaSearchResult> response) {
                    dialog.dismiss();
                    UsdaSearchResult result = response.body();
                    StringBuilder sb = new StringBuilder();
                    for (UsdaSearchResultFood food : result.getFoods()) {
                        sb.append(food.getDescription() + "\n");
                        for (UsdaSearchResultFoodNutrient nutrient : food.getFoodNutrients()) {
                            sb.append(nutrient.getNutrientName() + " " + nutrient.getValue() + "\n");
                        }
                        sb.append("---\n\n");
                    }
                    textView.setText(sb.toString());
                }

                @Override
                public void onFailure(Call<UsdaSearchResult> call, Throwable throwable) {
                    dialog.dismiss();
                    System.out.println(throwable);
                }
            });
        });

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}