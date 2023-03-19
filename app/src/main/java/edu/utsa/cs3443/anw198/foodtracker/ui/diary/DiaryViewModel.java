package edu.utsa.cs3443.anw198.foodtracker.ui.diary;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import edu.utsa.cs3443.anw198.foodtracker.APIListener;
import edu.utsa.cs3443.anw198.foodtracker.LoadingStatus;
import edu.utsa.cs3443.anw198.foodtracker.model.Food;

public class DiaryViewModel extends ViewModel implements APIListener<Food> {
    private final MutableLiveData<String> mText;
    private MutableLiveData<Food> foodResult = new MutableLiveData<>();
    private MutableLiveData<String> errorMessage = new MutableLiveData<>();
    private MutableLiveData<LoadingStatus> loadStatus = new MutableLiveData<>();

    public DiaryViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is diary fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
    public LiveData<Food> getFood() { return foodResult; }
    public LiveData<String> getErrorMessage() { return errorMessage; }
    public LiveData<LoadingStatus> getLoadStatus() { return loadStatus; }

    public void beginSearch() {
        loadStatus.setValue(LoadingStatus.IN_PROGRESS);
    }

    @Override
    public void onResponse(Food result) {
        foodResult.setValue(result);
        loadStatus.setValue(LoadingStatus.SUCCESS);
    }

    @Override
    public void onFailure(Throwable error) {
        errorMessage.setValue(error.getMessage());
        loadStatus.setValue(LoadingStatus.FAILURE);
    }
}
