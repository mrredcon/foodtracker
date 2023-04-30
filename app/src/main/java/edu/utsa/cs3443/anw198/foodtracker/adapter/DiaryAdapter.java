package edu.utsa.cs3443.anw198.foodtracker.adapter;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
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
import edu.utsa.cs3443.anw198.foodtracker.ui.TrackedFoodsViewModel;

public class DiaryAdapter extends RecyclerView.Adapter<DiaryAdapter.DiaryViewHolder> {
    private TrackedFoodsViewModel trackedFoodsViewModel;
    private LinkedHashMap<TrackedFood, CompleteFood> foods;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder.
    public DiaryAdapter(TrackedFoodsViewModel trackedFoodsViewModel, LinkedHashMap<TrackedFood, CompleteFood> foods) {
        this.trackedFoodsViewModel = trackedFoodsViewModel;
        this.foods = foods;
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
        ServingSize servingSize = trackedFood.findServingSize(completeFood);

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
            AlertDialog.Builder builder = new AlertDialog.Builder(c.getContext());

            builder.setTitle("Delete tracked food");
            builder.setMessage("Are you sure you want to delete this tracked food?");

            builder.setPositiveButton("Yes, delete it!", (dialog, which) -> {
                trackedFoodsViewModel.deleteTrackedFood(trackedFood);
                dialog.dismiss();
            });

            builder.setNegativeButton("CANCEL", (dialog, which) -> {
                // Do nothing
                dialog.dismiss();
            });

            AlertDialog alert = builder.create();
            alert.show();
        });
    }

    private DialogInterface.OnClickListener dialogClickListener = (dialog, which) -> {
        switch (which){
            case DialogInterface.BUTTON_POSITIVE:
                //Yes button clicked
                break;

            case DialogInterface.BUTTON_NEGATIVE:
                //No button clicked
                break;
        }
    };


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
