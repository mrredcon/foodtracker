package edu.utsa.cs3443.anw198.foodtracker.ui.diary;

import android.app.DatePickerDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import edu.utsa.cs3443.anw198.foodtracker.R;
import edu.utsa.cs3443.anw198.foodtracker.model.CompleteFood;
import edu.utsa.cs3443.anw198.foodtracker.model.FoodDao;
import edu.utsa.cs3443.anw198.foodtracker.model.ServingSize;
import edu.utsa.cs3443.anw198.foodtracker.model.TrackedFood;
import edu.utsa.cs3443.anw198.foodtracker.providers.DbProvider;

/**
 * A simple {@link Fragment} subclass.
 */
public class DiaryFragment extends Fragment {
    private List<TrackedFood> allTrackedFoods;
    private List<TrackedFood> trackedFoodsForTheDay;
    private List<CompleteFood> completeFoods;
    private Calendar currentDate;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_diary, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        completeFoods = new ArrayList<>();
        trackedFoodsForTheDay = new ArrayList<>();
        currentDate = Calendar.getInstance();

        setupCalendarButton();
        loadTrackedFoods();
        loadRelatedData();
    }

    private void loadRelatedData() {
        // Load CompleteFoods from TrackedFoods that have been tracked on
        // the same day as currentDate
        trackedFoodsForTheDay.clear();
        Calendar tempDate = Calendar.getInstance();
        FoodDao dao = DbProvider.getInstance().foodDao();
        List<Long> idsToLoad = new ArrayList<>();

        for (TrackedFood trackedFood : allTrackedFoods) {
            Date myDate = trackedFood.dateConsumed;
            tempDate.setTime(myDate);
            boolean sameDay = currentDate.get(Calendar.DAY_OF_YEAR) == tempDate.get(Calendar.DAY_OF_YEAR) &&
                    currentDate.get(Calendar.YEAR) == tempDate.get(Calendar.YEAR);
            if (sameDay) {
                trackedFoodsForTheDay.add(trackedFood);
                idsToLoad.add(trackedFood.foodId);
            }
        }

        // load the related data
        Thread thread = new Thread() {
            public void run() {
                completeFoods = dao.getCompleteFoods(idsToLoad);
                getActivity().runOnUiThread(() -> {
                    populateUI();
                });
            }
        };
        thread.start();
    }

    private CompleteFood findCompleteFood(TrackedFood trackedFood) {
        for (CompleteFood completeFood : completeFoods) {
            if (completeFood.food.id == trackedFood.foodId) {
                return completeFood;
            }
        }

        return null;
    }

    private void populateUI() {
        Toast.makeText(getContext(), "Haha debugging time!", Toast.LENGTH_SHORT).show();
        StringBuilder sb = new StringBuilder();

        for (TrackedFood trackedFood : trackedFoodsForTheDay) {
            long servingSizeId = trackedFood.servingSizeId;
            CompleteFood completeFood = findCompleteFood(trackedFood);
            ServingSize servingSize = null;

            for (ServingSize ss : completeFood.servingSizes) {
                if (ss.id == servingSizeId) {
                    servingSize = ss;
                    break;
                }
            }
            sb.append(completeFood.food.getName()).append(" ").append(servingSize.name).append(" ").append(servingSize.amount).append("\n");
        }

        TextView textView = getView().findViewById(R.id.textViewTrackedFoods);
        textView.setText(sb.toString());
    }

    private void loadTrackedFoods() {
        FoodDao dao = DbProvider.getInstance().foodDao();
        Thread thread = new Thread() {
            public void run() {
                allTrackedFoods = dao.getAllTrackedFoods();
            }
        };
        thread.start();
    }

    private void onDateSet(int year, int month, int day) {
        currentDate.set(year, month, day);
        loadTrackedFoods();
        loadRelatedData();
        //Button calButton = getView().findViewById(R.id.buttonOpenCalendar);
        //calButton.setText(currentDate.get(Calendar.MONTH)+1 + "/" + currentDate.get(Calendar.DAY_OF_MONTH) + "/" + currentDate.get(Calendar.YEAR));
    }

    private void setupCalendarButton() {
        Button calButton = getView().findViewById(R.id.buttonOpenCalendar);
        calButton.setOnClickListener(btn -> {
            DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), 0,
                    (datePicker, year, month, day) -> onDateSet(year, month, day),
                    currentDate.get(Calendar.YEAR),
                    currentDate.get(Calendar.MONTH),
                    currentDate.get(Calendar.DAY_OF_MONTH));

            datePickerDialog.show();
        });
    }
}