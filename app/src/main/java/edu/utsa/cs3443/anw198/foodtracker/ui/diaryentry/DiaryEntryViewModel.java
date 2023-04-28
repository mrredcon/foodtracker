package edu.utsa.cs3443.anw198.foodtracker.ui.diaryentry;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import edu.utsa.cs3443.anw198.foodtracker.APIListener;
import edu.utsa.cs3443.anw198.foodtracker.LoadingStatus;
import edu.utsa.cs3443.anw198.foodtracker.model.CompleteFood;

public class DiaryEntryViewModel extends ViewModel implements APIListener<CompleteFood> {
    private final MutableLiveData<String> mText;
    private MutableLiveData<CompleteFood> foodResult = new MutableLiveData<>();
    private MutableLiveData<String> errorMessage = new MutableLiveData<>();
    private MutableLiveData<LoadingStatus> loadStatus = new MutableLiveData<>();

    public DiaryEntryViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is diary fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
    public LiveData<CompleteFood> getFood() { return foodResult; }
    public LiveData<String> getErrorMessage() { return errorMessage; }
    public LiveData<LoadingStatus> getLoadStatus() { return loadStatus; }

    public void beginSearch() {
        loadStatus.setValue(LoadingStatus.IN_PROGRESS);
    }

    @Override
    public void onResponse(CompleteFood result) {
        foodResult.postValue(result);
        loadStatus.postValue(LoadingStatus.SUCCESS);
    }

    @Override
    public void onFailure(Throwable error) {
        errorMessage.setValue(error.getMessage());
        loadStatus.setValue(LoadingStatus.FAILURE);
    }
}
