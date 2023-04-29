package edu.utsa.cs3443.anw198.foodtracker.ui.diary;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Calendar;
import java.util.LinkedHashMap;
import java.util.List;

import edu.utsa.cs3443.anw198.foodtracker.R;
import edu.utsa.cs3443.anw198.foodtracker.adapter.DiaryAdapter;
import edu.utsa.cs3443.anw198.foodtracker.model.CompleteFood;
import edu.utsa.cs3443.anw198.foodtracker.model.FoodDao;
import edu.utsa.cs3443.anw198.foodtracker.model.TrackedFood;
import edu.utsa.cs3443.anw198.foodtracker.providers.DbProvider;

/**
 * A simple {@link Fragment} subclass.
 */
public class DiaryFragment extends Fragment {
    private LinkedHashMap<TrackedFood, CompleteFood> foods;
    private Calendar currentDate;
    private Calendar endOfCurrentDate;

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
        foods = new LinkedHashMap<>();
        currentDate = Calendar.getInstance();
        endOfCurrentDate = Calendar.getInstance();
        setupCalendarButton();

        onDateSet(currentDate.get(Calendar.YEAR), currentDate.get(Calendar.MONTH), currentDate.get(Calendar.DAY_OF_MONTH));
    }

    /**
     * Filters tracked foods that have been consumed on the currently set date.
     * @return Returns a list containing ids of the foods that have been consumed today.
     */
    private void loadCompleteFoods(List<TrackedFood> trackedFoods) {
        foods.clear();
        FoodDao dao = DbProvider.getInstance().foodDao();
        for (TrackedFood trackedFood : trackedFoods) {
            foods.put(trackedFood, dao.getCompleteFood(trackedFood.foodId));
        }
    }

    private void populateUI() {
        RecyclerView recyclerView = getView().findViewById(R.id.recyclerViewDiary);
        recyclerView.setAdapter(new DiaryAdapter(getActivity(), foods));
        recyclerView.setHasFixedSize(true);
    }

    private void loadData() {
        FoodDao dao = DbProvider.getInstance().foodDao();
        Thread thread = new Thread() {
            public void run() {
                long beginningOfDay = currentDate.getTimeInMillis();
                long endOfDay = endOfCurrentDate.getTimeInMillis();

                loadCompleteFoods(dao.getTrackedFoodsFromDate(beginningOfDay, endOfDay));
                getActivity().runOnUiThread(() -> {
                    populateUI();
                });
            }
        };
        thread.start();
    }

    private void onDateSet(int year, int month, int date) {
        currentDate.set(year, month, date);
        currentDate.set(Calendar.HOUR_OF_DAY, 0);
        currentDate.set(Calendar.MINUTE, 0);
        currentDate.set(Calendar.SECOND, 0);
        currentDate.set(Calendar.MILLISECOND, 0);

        endOfCurrentDate.set(year, month, date);
        endOfCurrentDate.set(Calendar.HOUR_OF_DAY, 23);
        endOfCurrentDate.set(Calendar.MINUTE, 59);
        endOfCurrentDate.set(Calendar.SECOND, 59);
        endOfCurrentDate.set(Calendar.MILLISECOND, 999);

        loadData();
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