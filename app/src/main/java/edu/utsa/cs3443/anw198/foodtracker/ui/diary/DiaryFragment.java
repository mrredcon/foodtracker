package edu.utsa.cs3443.anw198.foodtracker.ui.diary;

import android.app.AlertDialog;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import java.math.BigDecimal;
import java.math.RoundingMode;

import edu.utsa.cs3443.anw198.foodtracker.R;
import edu.utsa.cs3443.anw198.foodtracker.databinding.FragmentDiaryBinding;
import edu.utsa.cs3443.anw198.foodtracker.model.Food;
import edu.utsa.cs3443.anw198.foodtracker.model.Nutrient;
import edu.utsa.cs3443.anw198.foodtracker.providers.FoodProvider;
import edu.utsa.cs3443.anw198.foodtracker.providers.usda.UsdaFoodProvider;

public class DiaryFragment extends Fragment {
    private FragmentDiaryBinding binding;
    private AlertDialog dialog;
    private FoodProvider provider;

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
            TextView textView = getView().findViewById(R.id.textViewSaveToDiary);
            textView.setMovementMethod(new ScrollingMovementMethod());

            StringBuilder sb = new StringBuilder();
            sb.append("Name: ").append(foodResult.getName()).append("\n");
            sb.append("Calories: ");
            sb.append(String.format("%s", foodResult.getCalories())).append("\n");

            String baseUnitAbv = getContext().getText(foodResult.getBaseUnit().getAbbreviationResource()).toString();

            sb.append("\nServing Sizes:\n");
            for (String key : foodResult.getServingSizes().keySet()) {
                sb.append("\t").append(key).append(": ")
                        .append(foodResult.getServingSizes().get(key))
                        .append(" ").append(baseUnitAbv).append("\n");
            }
            sb.append("\nNutrients:\n");
            for (Nutrient nutrient : foodResult.getNutrients().keySet()) {
                String nutrientName = getContext().getText(nutrient.stringResource).toString();
                sb.append("\t").append(nutrientName).append(": ");
                Double nutrientValue = new BigDecimal(foodResult.getNutrients().get(nutrient))
                        .setScale(3, RoundingMode.HALF_UP).doubleValue();

                sb.append(nutrientValue).append(" ");
                String nutritionAbv = getContext().getText(Food.NUTRIENT_UNITS.get(nutrient).getAbbreviationResource()).toString();
                sb.append(nutritionAbv).append("\n");
            }
            textView.setText(sb.toString());
            /*
            RecyclerView recyclerView = getView().findViewById(R.id.recyclerView);
            recyclerView.setAdapter(new FoodAdapter(getContext(), foodResult));
            recyclerView.setHasFixedSize(true);
             */
        });

        diaryViewModel.getErrorMessage().observe(this, errorMessage -> {
            Toast.makeText(getContext(), errorMessage, Toast.LENGTH_SHORT).show();
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
