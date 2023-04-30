package edu.utsa.cs3443.anw198.foodtracker.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import java.util.LinkedHashMap;
import java.util.Locale;

import edu.utsa.cs3443.anw198.foodtracker.R;
import edu.utsa.cs3443.anw198.foodtracker.adapter.NutrientSummaryAdapter;
import edu.utsa.cs3443.anw198.foodtracker.databinding.FragmentHomeBinding;
import edu.utsa.cs3443.anw198.foodtracker.model.CompleteFood;
import edu.utsa.cs3443.anw198.foodtracker.model.ServingSize;
import edu.utsa.cs3443.anw198.foodtracker.model.TrackedFood;
import edu.utsa.cs3443.anw198.foodtracker.ui.TrackedFoodsViewModel;

public class HomeFragment extends Fragment {
    private FragmentHomeBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        HomeViewModel homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        TrackedFoodsViewModel trackedFoodsViewModel = new ViewModelProvider(getActivity()).get(TrackedFoodsViewModel.class);
        trackedFoodsViewModel.getFoods().observe(this, this::populateUI);
    }

    private void populateUI(LinkedHashMap<TrackedFood, CompleteFood> foods) {
        int foodCount = 0;
        double caloriesSum = 0.0;
        double massSum = 0.0;

        for (TrackedFood trackedFood : foods.keySet()) {
            CompleteFood complete = foods.get(trackedFood);
            ServingSize servingSize = trackedFood.findServingSize(complete);

            double multiplier = trackedFood.getMultiplier(complete);

            caloriesSum += complete.food.getCalories() * multiplier;
            if (!complete.food.isMeasuredInVolume()) {
                massSum += servingSize.amount * trackedFood.amount;
            }

            foodCount++;
        }

        TextView massText = getView().findViewById(R.id.homeGramsConsumed);
        massText.setText(String.format(Locale.US, "%,.1f g", massSum));

        TextView amountTracked = getView().findViewById(R.id.homeFoodsTracked);
        amountTracked.setText(foodCount + " foods tracked");

        TextView caloriesText = getView().findViewById(R.id.homeCalories);
        caloriesText.setText(String.format(Locale.US, "%,.0f", caloriesSum));

        RecyclerView recyclerView = getView().findViewById(R.id.recyclerViewHome);
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