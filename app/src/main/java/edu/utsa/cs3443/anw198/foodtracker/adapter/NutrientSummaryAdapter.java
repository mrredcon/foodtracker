package edu.utsa.cs3443.anw198.foodtracker.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Space;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.res.ResourcesCompat;
import androidx.core.text.HtmlCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.LinkedHashMap;

import edu.utsa.cs3443.anw198.foodtracker.R;
import edu.utsa.cs3443.anw198.foodtracker.model.CompleteFood;
import edu.utsa.cs3443.anw198.foodtracker.model.Nutrient;
import edu.utsa.cs3443.anw198.foodtracker.model.NutrientType;
import edu.utsa.cs3443.anw198.foodtracker.model.TrackedFood;
import edu.utsa.cs3443.anw198.foodtracker.model.units.MassUnit;

public class NutrientSummaryAdapter extends RecyclerView.Adapter<NutrientSummaryAdapter.NutrientSummaryViewHolder> {
    private Context context;
    private LinkedHashMap<TrackedFood, CompleteFood> foods;
    private static final int NUTRIENT_TEXT_WEIGHT_DEFAULT = 8;
    private static final int NUTRIENT_TEXT_WEIGHT_INDENT_SINGLE = 7;
    private static final int NUTRIENT_TEXT_WEIGHT_INDENT_DOUBLE = 6;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder.
    public NutrientSummaryAdapter(Context context, LinkedHashMap<TrackedFood, CompleteFood> foods) {
        this.context = context;
        this.foods = foods;
    }

    @NonNull
    @Override
    /**
     * Create new views
     */
    public NutrientSummaryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View adapterLayout = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_nutrient, parent, false);

        return new NutrientSummaryViewHolder(adapterLayout);
    }

    @Override
    /**
     * Replace the contents of a view
     */
    public void onBindViewHolder(@NonNull NutrientSummaryViewHolder holder, int position) {
        NutrientType nutrientType = NutrientType.getNutrientTypeFromOrder(context, position);

        // Handle indents
        LinearLayout.LayoutParams nutrientTextLayoutParams = (LinearLayout.LayoutParams) holder.nutrientText.getLayoutParams();
        nutrientTextLayoutParams.weight = NUTRIENT_TEXT_WEIGHT_DEFAULT;
        holder.indent1.setVisibility(View.GONE);
        holder.indent2.setVisibility(View.GONE);

        switch (nutrientType) {
            case FAT_SATURATED:
            case FAT_TRANS:
            case FIBER:
            case SUGAR:
                holder.indent1.setVisibility(View.VISIBLE);
                nutrientTextLayoutParams.weight = NUTRIENT_TEXT_WEIGHT_INDENT_SINGLE;
                break;
            case SUGAR_ADDED:
            case SUGAR_ALCOHOL:
                holder.indent1.setVisibility(View.VISIBLE);
                holder.indent2.setVisibility(View.VISIBLE);
                nutrientTextLayoutParams.weight = NUTRIENT_TEXT_WEIGHT_INDENT_DOUBLE;
                break;
        }

        holder.nutrientText.setLayoutParams(nutrientTextLayoutParams); // done handling indents

        MassUnit unit = Nutrient.NUTRIENT_UNITS.get(nutrientType);
        String unitAbv = context.getString(unit.getAbbreviationResource());

        double sum = Math.floor(sumNutrient(nutrientType));
        String nutrientName = context.getString(nutrientType.stringResource);

        // Handle bold and italics
        String nutrientString;
        switch (nutrientType) {
            case FAT:
            case CHOLESTEROL:
            case SODIUM:
            case CARBOHYDRATES:
            case PROTEIN:
                nutrientString = context.getString(R.string.home_nutrient_bold, nutrientName, sum, unitAbv);
                break;
            case FAT_TRANS:
                nutrientString = context.getString(R.string.home_nutrient_italic, nutrientName, sum, unitAbv);
                break;
            default:
                nutrientString = context.getString(R.string.home_nutrient, nutrientName, sum, unitAbv);
                break;
        }

        CharSequence styledText = HtmlCompat.fromHtml(nutrientString, HtmlCompat.FROM_HTML_MODE_LEGACY);
        holder.nutrientText.setText(styledText);

        // Daily value
        Float dv = ResourcesCompat.getFloat(context.getResources(), nutrientType.dailyValueResource);

        if (dv == null || dv == 0.0f) {
            holder.dvText.setVisibility(View.INVISIBLE);
        } else {
            holder.dvText.setVisibility(View.VISIBLE);
            double percent = (sum / dv) * 100;
            holder.dvText.setText(context.getString(R.string.home_dv, percent));
        }
    }

    private double sumNutrient(NutrientType nutrientType) {
        double sum = 0.0;

        for (TrackedFood trackedFood : foods.keySet()) {
            CompleteFood completeFood = foods.get(trackedFood);
            double multiplier = trackedFood.getMultiplier(completeFood);
            Nutrient nutrient = completeFood.getNutrient(nutrientType);
            if (nutrient != null) {
                sum += nutrient.amount * multiplier;
            }
        }

        return sum;
    }

    @Override
    /**
     * Return size of data set
     */
    public int getItemCount() {
        return NutrientType.getNutrientOrder(context).size();
    }

    public class NutrientSummaryViewHolder extends RecyclerView.ViewHolder {
        private Space indent1;
        private Space indent2;
        private TextView nutrientText;
        private TextView dvText;

        public NutrientSummaryViewHolder(@NonNull View itemView) {
            super(itemView);
            indent1 = itemView.findViewById(R.id.homeIndent1);
            indent2 = itemView.findViewById(R.id.homeIndent2);
            nutrientText = itemView.findViewById(R.id.homeNutrient);
            dvText = itemView.findViewById(R.id.homePercentDV);
        }
    }
}
