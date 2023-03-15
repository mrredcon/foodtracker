package edu.utsa.cs3443.anw198.foodtracker.ui.searchfood;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import edu.utsa.cs3443.anw198.foodtracker.FoodSearchListener;
import edu.utsa.cs3443.anw198.foodtracker.R;
import edu.utsa.cs3443.anw198.foodtracker.adapter.FoodSearchResultAdapter;
import edu.utsa.cs3443.anw198.foodtracker.databinding.FragmentSearchfoodBinding;
import edu.utsa.cs3443.anw198.foodtracker.model.FoodSearchResult;
import edu.utsa.cs3443.anw198.foodtracker.providers.FoodSearchProvider;
import edu.utsa.cs3443.anw198.foodtracker.providers.usda.UsdaFoodSearchProvider;

public class SearchFoodFragment extends Fragment implements FoodSearchListener {

    private FragmentSearchfoodBinding binding;
    
    private AlertDialog dialog;
    //private TextView textView;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        SearchFoodViewModel searchFoodViewModel =
                new ViewModelProvider(this).get(SearchFoodViewModel.class);

        binding = FragmentSearchfoodBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        //textView = binding.textSearchfood;
        //searchFoodViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);

        final Button button = binding.buttonSearchFood;
        final EditText query = binding.editTextSearchQuery;

        FoodSearchProvider provider = new UsdaFoodSearchProvider();

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
        RecyclerView recyclerView = getView().findViewById(R.id.recyclerView);
        recyclerView.setAdapter(new FoodSearchResultAdapter(getContext(), results));
        recyclerView.setHasFixedSize(true);
    }

    @Override
    public void onFailure(Throwable error) {
        dialog.dismiss();
        Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
    }
}