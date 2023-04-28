package edu.utsa.cs3443.anw198.foodtracker.ui.diary;

import android.app.DatePickerDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

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
import edu.utsa.cs3443.anw198.foodtracker.adapter.DiaryAdapter;
import edu.utsa.cs3443.anw198.foodtracker.adapter.FoodSearchResultAdapter;
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
    private List<TrackedFood> filteredTrackedFoods;
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
        filteredTrackedFoods = new ArrayList<>();
        currentDate = Calendar.getInstance();

        setupCalendarButton();
        loadData();
    }

    /**
     * Filters tracked foods that have been consumed on the currently set date.
     * @return Returns a list containing ids of the foods that have been consumed today.
     */
    private List<Long> filterTrackedFoods() {
        filteredTrackedFoods.clear();
        Calendar tempDate = Calendar.getInstance();
        List<Long> idsToLoad = new ArrayList<>();

        for (TrackedFood trackedFood : allTrackedFoods) {
            Date myDate = trackedFood.dateConsumed;
            tempDate.setTime(myDate);
            boolean sameDay = currentDate.get(Calendar.DAY_OF_YEAR) == tempDate.get(Calendar.DAY_OF_YEAR) &&
                    currentDate.get(Calendar.YEAR) == tempDate.get(Calendar.YEAR);
            if (sameDay) {
                filteredTrackedFoods.add(trackedFood);
                idsToLoad.add(trackedFood.foodId);
            }
        }

        return idsToLoad;
    }

    private void populateUI() {
        RecyclerView recyclerView = getView().findViewById(R.id.recyclerViewDiary);
        recyclerView.setAdapter(new DiaryAdapter(getActivity(), filteredTrackedFoods, completeFoods));
        recyclerView.setHasFixedSize(true);
    }

    private void loadData() {
        FoodDao dao = DbProvider.getInstance().foodDao();
        Thread thread = new Thread() {
            public void run() {
                allTrackedFoods = dao.getAllTrackedFoods();
                List<Long> ids = filterTrackedFoods();
                completeFoods = dao.getCompleteFoods(ids);
                getActivity().runOnUiThread(() -> {
                    populateUI();
                });
            }
        };
        thread.start();
    }

    private void onDateSet(int year, int month, int day) {
        currentDate.set(year, month, day);
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