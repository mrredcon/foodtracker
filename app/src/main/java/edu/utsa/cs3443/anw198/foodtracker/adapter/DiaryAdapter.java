package edu.utsa.cs3443.anw198.foodtracker.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.card.MaterialCardView;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.LinkedHashMap;

import edu.utsa.cs3443.anw198.foodtracker.R;
import edu.utsa.cs3443.anw198.foodtracker.model.CompleteFood;
import edu.utsa.cs3443.anw198.foodtracker.model.Food;
import edu.utsa.cs3443.anw198.foodtracker.model.ServingSize;
import edu.utsa.cs3443.anw198.foodtracker.model.TrackedFood;

public class DiaryAdapter extends RecyclerView.Adapter<DiaryAdapter.DiaryViewHolder> {
    private LinkedHashMap<TrackedFood, CompleteFood> foods;
    private FragmentActivity activity;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder.
    public DiaryAdapter(FragmentActivity activity, LinkedHashMap<TrackedFood, CompleteFood> foods) {
        this.foods = foods;
        this.activity = activity;
    }

    private ServingSize findServingSize(TrackedFood trackedFood, CompleteFood completeFood) {
        for (ServingSize ss : completeFood.servingSizes) {
            if (ss.id == trackedFood.servingSizeId) {
                return ss;
            }
        }

        return null;
    }

    @NonNull
    @Override
    /**
     * Create new views
     */
    public DiaryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View adapterLayout = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_diary, parent, false);

        return new DiaryViewHolder(adapterLayout);
    }

    @Override
    /**
     * Replace the contents of a view
     */
    public void onBindViewHolder(@NonNull DiaryViewHolder holder, int position) {
        TrackedFood trackedFood = (TrackedFood) foods.keySet().toArray()[position];
        CompleteFood completeFood = foods.get(trackedFood);
        ServingSize servingSize = findServingSize(trackedFood, completeFood);

        // tracked food amount is how many servings
        // servingSize amount is base units per serving
        double quantity = trackedFood.amount * servingSize.amount;

        // needed to calculate nutrition and calories
        double multiplier = quantity / Food.DEFAULT_QUANTITY;

        String calories = holder.textViewTitle.getContext().getString(R.string.diary_calories,
                completeFood.food.getCalories() * multiplier);
        holder.textViewCalories.setText(calories);

        holder.textViewTitle.setText(completeFood.food.getName());
        String baseUnitAbv = holder.textViewTitle.getContext().getString(completeFood.food.getBaseUnit().getAbbreviationResource());
        String servingsText = holder.textViewTitle.getContext().getString(R.string.diary_servings,
                trackedFood.amount, servingSize.name, quantity, baseUnitAbv);

        // Show "1 cheese cracker" instead of "1.0 * 1 cheese cracker"
        if (trackedFood.amount == 1) {
            servingsText = holder.textViewTitle.getContext().getString(R.string.diary_servings_single,
                    servingSize.name, quantity, baseUnitAbv);
        }

        holder.textViewServings.setText(servingsText);
        Format formatter = new SimpleDateFormat("hh:mm a");
        holder.textViewTime.setText(formatter.format(trackedFood.dateConsumed));

        holder.cardView.setOnClickListener(c -> {
            //Toast.makeText(holder.cardView.getContext(), result.getCalories().intValue(), Toast.LENGTH_SHORT);
            //context.
            //Log.e("Nom", "I was clicked: " + result.getCalories());

            /*
            NavController navController = Navigation.findNavController(activity, R.id.nav_host_fragment_content_main);
            navController.navigate(R.id.nav_diary_entry);

            DiaryEntryViewModel diaryEntryViewModel = new ViewModelProvider(activity).get(DiaryEntryViewModel.class);
            FoodProvider provider = new UsdaFoodProvider();
            diaryEntryViewModel.beginSearch();
            provider.loadFood(result.getId(), diaryEntryViewModel);

             */
        });
    }

    @Override
    /**
     * Return size of data set
     */
    public int getItemCount() {
        return foods.size();
    }

    public class DiaryViewHolder extends RecyclerView.ViewHolder {
        //private Activity activity;
        private TextView textViewTitle;
        private TextView textViewServings;
        private TextView textViewTime;
        private TextView textViewCalories;
        private MaterialCardView cardView;

        public DiaryViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewTitle = itemView.findViewById(R.id.diary_title);
            textViewServings = itemView.findViewById(R.id.diary_servingsize);
            textViewTime = itemView.findViewById(R.id.diary_time);
            textViewCalories = itemView.findViewById(R.id.diary_calories);
            cardView = itemView.findViewById(R.id.diary_card);
        }
    }
}
