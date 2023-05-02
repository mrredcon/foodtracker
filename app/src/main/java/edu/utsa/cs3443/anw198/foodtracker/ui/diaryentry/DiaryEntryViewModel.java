package edu.utsa.cs3443.anw198.foodtracker.ui.diaryentry;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import edu.utsa.cs3443.anw198.foodtracker.APIListener;
import edu.utsa.cs3443.anw198.foodtracker.LoadingStatus;
import edu.utsa.cs3443.anw198.foodtracker.model.CompleteFood;
import edu.utsa.cs3443.anw198.foodtracker.model.TrackedFood;
import edu.utsa.cs3443.anw198.foodtracker.providers.FoodProvider;

public class DiaryEntryViewModel extends ViewModel implements APIListener<CompleteFood> {
    private MutableLiveData<CompleteFood> foodResult = new MutableLiveData<>();
    private MutableLiveData<String> errorMessage = new MutableLiveData<>();
    private MutableLiveData<LoadingStatus> loadStatus = new MutableLiveData<>();
    private TrackedFood trackedFood = null;
    private FoodProvider foodProvider = null;
    private boolean editFood = false;

    public DiaryEntryViewModel() {
    }

    public boolean isEditingEntry() {
        return editFood;
    }

    public LiveData<CompleteFood> getFood() { return foodResult; }
    public LiveData<String> getErrorMessage() { return errorMessage; }
    public LiveData<LoadingStatus> getLoadStatus() { return loadStatus; }
    public void beginEditing(TrackedFood trackedFood, CompleteFood completeFood) {
        this.editFood = true;
        this.trackedFood = trackedFood;
        foodResult.postValue(completeFood);
    }
    public TrackedFood getTrackedFood() { return trackedFood; }

    public void beginSearch(FoodProvider foodProvider) {
        this.foodProvider = foodProvider;
        loadStatus.setValue(LoadingStatus.IN_PROGRESS);
    }

    public void cancelSearch() {
        this.foodProvider.cancelLoad();
    }

    @Override
    public void onResponse(CompleteFood result) {
        editFood = false;
        foodResult.postValue(result);
        loadStatus.postValue(LoadingStatus.SUCCESS);
    }

    @Override
    public void onFailure(Throwable error) {
        errorMessage.setValue(error.getMessage());
        loadStatus.setValue(LoadingStatus.FAILURE);
    }
}
