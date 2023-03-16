package edu.utsa.cs3443.anw198.foodtracker.ui.searchfood;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import edu.utsa.cs3443.anw198.foodtracker.FoodSearchListener;
import edu.utsa.cs3443.anw198.foodtracker.FoodSearchStatus;
import edu.utsa.cs3443.anw198.foodtracker.model.FoodSearchResult;

public class SearchFoodViewModel extends ViewModel implements FoodSearchListener {

    private final MutableLiveData<String> mText;
    private MutableLiveData<FoodSearchResult[]> searchResults = new MutableLiveData<>();
    private MutableLiveData<String> errorMessage = new MutableLiveData<>();
    private MutableLiveData<FoodSearchStatus> searchStatus = new MutableLiveData<>();

    public SearchFoodViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is search food fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
    public LiveData<FoodSearchResult[]> getSearchResults() { return searchResults; }
    public LiveData<String> getErrorMessage() { return errorMessage; }
    public LiveData<FoodSearchStatus> getSearchStatus() { return searchStatus; }

    public void beginSearch() {
        searchStatus.setValue(FoodSearchStatus.SEARCH_IN_PROGRESS);
    }

    @Override
    public void onResponse(FoodSearchResult[] results) {
        searchResults.setValue(results);
        searchStatus.setValue(FoodSearchStatus.SEARCH_SUCCESS);
    }

    @Override
    public void onFailure(Throwable error) {
        errorMessage.setValue(error.getMessage());
        searchStatus.setValue(FoodSearchStatus.SEARCH_FAILURE);
    }
}