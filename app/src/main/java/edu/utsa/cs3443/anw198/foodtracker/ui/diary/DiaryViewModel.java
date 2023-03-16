package edu.utsa.cs3443.anw198.foodtracker.ui.diary;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import edu.utsa.cs3443.anw198.foodtracker.model.Food;

public class DiaryViewModel extends ViewModel {
    private final MutableLiveData<Food> food = new MutableLiveData<>();

    public DiaryViewModel() {
        //mText.setValue("This is gallery fragment");
    }

    public LiveData<Food> getFood() {
        return food;
    }
}
