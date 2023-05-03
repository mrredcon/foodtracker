package edu.utsa.cs3443.anw198.foodtracker.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.card.MaterialCardView;

import java.util.List;

import edu.utsa.cs3443.anw198.foodtracker.R;
import edu.utsa.cs3443.anw198.foodtracker.model.FoodSearchResult;
import edu.utsa.cs3443.anw198.foodtracker.providers.FoodProvider;
import edu.utsa.cs3443.anw198.foodtracker.providers.usda.UsdaFoodProvider;
import edu.utsa.cs3443.anw198.foodtracker.ui.diaryentry.DiaryEntryViewModel;

public class FoodSearchResultAdapter extends RecyclerView.Adapter<FoodSearchResultAdapter.FoodSearchResultViewHolder> {
    private List<FoodSearchResult> searchResults;
    private FragmentActivity activity;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder.
    // Each data item is just a FoodSearchResult object.
    public FoodSearchResultAdapter(FragmentActivity activity, List<FoodSearchResult> searchResults) {
        this.searchResults = searchResults;
        this.activity = activity;
    }

    @NonNull
    @Override
    /**
     * Create new views
     */
    public FoodSearchResultViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View adapterLayout = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_foodsearchresult, parent, false);

        return new FoodSearchResultViewHolder(adapterLayout);
    }

    @Override
    /**
     * Replace the contents of a view
     */
    public void onBindViewHolder(@NonNull FoodSearchResultViewHolder holder, int position) {
        FoodSearchResult result = searchResults.get(position);
        String macroText = holder.textViewTitle.getContext().getString(R.string.foodsearchresult_macros, result.getCalories(), result.getFat(), result.getCarbs(), result.getProtein());

        holder.textViewTitle.setText(result.getName());
        holder.textViewMacros.setText(macroText);

        String brand = result.getBrand();
        if (brand != null) {
            holder.textViewBrand.setVisibility(View.VISIBLE);
            holder.textViewBrand.setText(brand);
        }

        holder.cardView.setOnClickListener(c -> {
            //Toast.makeText(holder.cardView.getContext(), result.getCalories().intValue(), Toast.LENGTH_SHORT);
            //context.
            //Log.e("Nom", "I was clicked: " + result.getCalories());

            NavController navController = Navigation.findNavController(activity, R.id.nav_host_fragment_content_main);
            navController.navigate(R.id.nav_diary_entry);

            DiaryEntryViewModel diaryEntryViewModel = new ViewModelProvider(activity).get(DiaryEntryViewModel.class);
            FoodProvider provider = new UsdaFoodProvider(activity);
            diaryEntryViewModel.beginSearch(provider);
            provider.loadFood(result.getId(), diaryEntryViewModel);
        });
    }

    @Override
    /**
     * Return size of data set
     */
    public int getItemCount() {
        return searchResults.size();
    }

    public class FoodSearchResultViewHolder extends RecyclerView.ViewHolder {
        //private Activity activity;
        private TextView textViewTitle;
        private TextView textViewMacros;
        private TextView textViewBrand;
        private MaterialCardView cardView;

        public FoodSearchResultViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewTitle = itemView.findViewById(R.id.foodsearchresult_title);
            textViewMacros = itemView.findViewById(R.id.foodsearchresult_macros);
            textViewBrand = itemView.findViewById(R.id.foodsearchresult_brand);
            cardView = itemView.findViewById(R.id.foodsearchresult_card);
            //activity = FragmentManager.findFragment(itemView).getActivity();
        }
    }
}
