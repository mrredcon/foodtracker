package edu.utsa.cs3443.anw198.foodtracker.ui.searchfood;

import android.app.AlertDialog;
import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;
import android.provider.SearchRecentSuggestions;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import edu.utsa.cs3443.anw198.foodtracker.R;
import edu.utsa.cs3443.anw198.foodtracker.adapter.FoodSearchResultAdapter;
import edu.utsa.cs3443.anw198.foodtracker.databinding.FragmentSearchfoodBinding;
import edu.utsa.cs3443.anw198.foodtracker.model.FoodSearchResult;
import edu.utsa.cs3443.anw198.foodtracker.providers.FoodSearchProvider;
import edu.utsa.cs3443.anw198.foodtracker.providers.FoodSearchSuggestionProvider;
import edu.utsa.cs3443.anw198.foodtracker.providers.usda.UsdaFoodSearchProvider;

public class SearchFoodFragment extends Fragment {

    private FragmentSearchfoodBinding binding;
    
    private AlertDialog dialog;
    private FoodSearchProvider provider;
    private FoodSearchResult[] results;
    //private TextView textView;
    //private Menu menu;

    @Override
    public void onCreate(Bundle onCreateInstance) {
        super.onCreate(onCreateInstance);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.findItem(R.id.action_search).setVisible(true);

        //FoodSearchListener listener = this;
        //SearchFoodViewModel listener = new SearchFoodViewModel();

        // Set up search suggestions
        Intent intent  = getActivity().getIntent();

        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            SearchRecentSuggestions suggestions = new SearchRecentSuggestions(getContext(),
                    FoodSearchSuggestionProvider.AUTHORITY, FoodSearchSuggestionProvider.MODE);
            suggestions.saveRecentQuery(query, null);
        }
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        SearchFoodViewModel searchFoodViewModel = new ViewModelProvider(getActivity()).get(SearchFoodViewModel.class);

        binding = FragmentSearchfoodBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        //textView = binding.textSearchfood;
        //searchFoodViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);

        provider = new UsdaFoodSearchProvider();

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setMessage("Searching database, please wait.");
        builder.setTitle("Searching...");
        builder.setCancelable(true);
        builder.setNegativeButton("Cancel", (dialogInterface, i) -> provider.cancelSearch());

        dialog = builder.create();

        searchFoodViewModel.getSearchStatus().observe(this, searchStatus -> {
            switch (searchStatus) {
                case SEARCH_IN_PROGRESS:
                    dialog.show();
                    break;
                case SEARCH_FAILURE:
                case SEARCH_SUCCESS:
                    dialog.dismiss();
                    break;
            }
        });

        searchFoodViewModel.getSearchResults().observe(this, searchResults -> {
            RecyclerView recyclerView = getView().findViewById(R.id.recyclerView);
            recyclerView.setAdapter(new FoodSearchResultAdapter(getContext(), searchResults));
            recyclerView.setHasFixedSize(true);
            results = searchResults;
        });

        searchFoodViewModel.getErrorMessage().observe(this, errorMessage -> {
            Toast.makeText(getContext(), errorMessage, Toast.LENGTH_SHORT).show();
        });

        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
        dialog = null;
        provider = null;
    }
}