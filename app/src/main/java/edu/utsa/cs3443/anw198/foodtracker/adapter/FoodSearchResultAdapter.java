package edu.utsa.cs3443.anw198.foodtracker.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import edu.utsa.cs3443.anw198.foodtracker.R;
import edu.utsa.cs3443.anw198.foodtracker.model.FoodSearchResult;

public class FoodSearchResultAdapter extends RecyclerView.Adapter<FoodSearchResultAdapter.FoodSearchResultViewHolder> {
    private FoodSearchResult[] searchResults;
    private Context context;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder.
    // Each data item is just a FoodSearchResult object.
    public FoodSearchResultAdapter(Context context, FoodSearchResult[] searchResults) {
        this.searchResults = searchResults;
        this.context = context;
    }

    @NonNull
    @Override
    /**
     * Create new views
     */
    public FoodSearchResultViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View adapterLayout = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item, parent, false);

        return new FoodSearchResultViewHolder(adapterLayout);
    }

    @Override
    /**
     * Replace the contents of a view
     */
    public void onBindViewHolder(@NonNull FoodSearchResultViewHolder holder, int position) {
        FoodSearchResult result = searchResults[position];
        holder.textView.setText(result.getName() +
                " / Calories: " + result.getCalories() +
                " / Fat: " + result.getFat() +
                " / Carbs: " + result.getCarbs() +
                " / Protein: " + result.getProtein());
    }

    @Override
    /**
     * Return size of data set
     */
    public int getItemCount() {
        return searchResults.length;
    }

    public class FoodSearchResultViewHolder extends RecyclerView.ViewHolder {
        private TextView textView;

        public FoodSearchResultViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.item_title);
        }
    }
}
