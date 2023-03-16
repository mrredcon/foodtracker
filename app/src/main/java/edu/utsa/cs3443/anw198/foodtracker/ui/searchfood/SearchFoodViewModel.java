package edu.utsa.cs3443.anw198.foodtracker.ui.searchfood;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import edu.utsa.cs3443.anw198.foodtracker.FoodSearchListener;
import edu.utsa.cs3443.anw198.foodtracker.model.FoodSearchResult;

public class SearchFoodViewModel extends ViewModel implements FoodSearchListener {

    private final MutableLiveData<String> mText;
    private MutableLiveData<FoodSearchResult[]> searchResults = new MutableLiveData<>();
    private MutableLiveData<String> errorMessage = new MutableLiveData<>();

    public SearchFoodViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is search food fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
    public LiveData<FoodSearchResult[]> getSearchResults() { return searchResults; }
    public LiveData<String> getErrorMessage() { return errorMessage; }

    @Override
    public void onResponse(FoodSearchResult[] results) {
        searchResults.setValue(results);
    }

    @Override
    public void onFailure(Throwable error) {
        errorMessage.setValue(error.getMessage());
    }
}