package edu.utsa.cs3443.anw198.foodtracker.ui.searchfood;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class SearchFoodViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public SearchFoodViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is search food fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}