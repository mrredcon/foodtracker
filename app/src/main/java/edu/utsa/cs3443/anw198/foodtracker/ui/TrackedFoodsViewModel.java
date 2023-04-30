package edu.utsa.cs3443.anw198.foodtracker.ui;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.Calendar;
import java.util.LinkedHashMap;
import java.util.List;

import edu.utsa.cs3443.anw198.foodtracker.model.CompleteFood;
import edu.utsa.cs3443.anw198.foodtracker.model.FoodDao;
import edu.utsa.cs3443.anw198.foodtracker.model.TrackedFood;
import edu.utsa.cs3443.anw198.foodtracker.providers.DbProvider;

public class TrackedFoodsViewModel extends ViewModel {
    private final Calendar startDate;
    private final Calendar endOfCurrentDate;
    private LinkedHashMap<TrackedFood, CompleteFood> foods;
    private final MutableLiveData<LinkedHashMap<TrackedFood, CompleteFood>> liveDataFoods;

    public TrackedFoodsViewModel() {
        startDate = Calendar.getInstance();
        endOfCurrentDate = Calendar.getInstance();
        foods = new LinkedHashMap<>();
        liveDataFoods = new MutableLiveData<>();
    }

    public LiveData<LinkedHashMap<TrackedFood, CompleteFood>> getFoods() {
        return liveDataFoods;
    }

    public void setDateAndReloadData(int year, int month, int date) {
        startDate.set(year, month, date);
        startDate.set(Calendar.HOUR_OF_DAY, 0);
        startDate.set(Calendar.MINUTE, 0);
        startDate.set(Calendar.SECOND, 0);
        startDate.set(Calendar.MILLISECOND, 0);

        endOfCurrentDate.set(year, month, date);
        endOfCurrentDate.set(Calendar.HOUR_OF_DAY, 23);
        endOfCurrentDate.set(Calendar.MINUTE, 59);
        endOfCurrentDate.set(Calendar.SECOND, 59);
        endOfCurrentDate.set(Calendar.MILLISECOND, 999);

        reloadData();
    }

    public void reloadData() {
        FoodDao dao = DbProvider.getInstance().foodDao();
        Thread thread = new Thread() {
            public void run() {
                long beginningOfDay = startDate.getTimeInMillis();
                long endOfDay = endOfCurrentDate.getTimeInMillis();

                loadCompleteFoods(dao.getTrackedFoodsFromDate(beginningOfDay, endOfDay));
                liveDataFoods.postValue(foods);
            }
        };
        thread.start();
    }

    private void loadCompleteFoods(List<TrackedFood> trackedFoods) {
        foods.clear();
        FoodDao dao = DbProvider.getInstance().foodDao();
        for (TrackedFood trackedFood : trackedFoods) {
            foods.put(trackedFood, dao.getCompleteFood(trackedFood.foodId));
        }
    }
}
