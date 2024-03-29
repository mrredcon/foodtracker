package edu.utsa.cs3443.anw198.foodtracker.ui.diary;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import java.util.LinkedHashMap;

import edu.utsa.cs3443.anw198.foodtracker.R;
import edu.utsa.cs3443.anw198.foodtracker.adapter.DiaryAdapter;
import edu.utsa.cs3443.anw198.foodtracker.model.CompleteFood;
import edu.utsa.cs3443.anw198.foodtracker.model.TrackedFood;
import edu.utsa.cs3443.anw198.foodtracker.ui.TrackedFoodsViewModel;

/**
 * A simple {@link Fragment} subclass.
 */
public class DiaryFragment extends Fragment {
    private TrackedFoodsViewModel trackedFoodsViewModel;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onPrepareOptionsMenu(@NonNull Menu menu) {
        super.onPrepareOptionsMenu(menu);
        menu.findItem(R.id.action_calendar).setVisible(true);
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
        //setupCalendarButton();
        trackedFoodsViewModel = new ViewModelProvider(getActivity()).get(TrackedFoodsViewModel.class);
        populateUI(trackedFoodsViewModel.getFoods().getValue());
        trackedFoodsViewModel.getFoods().observe(this, this::populateUI);
    }

    private void populateUI(LinkedHashMap<TrackedFood, CompleteFood> foods) {
        RecyclerView recyclerView = getView().findViewById(R.id.recyclerViewDiary);
        recyclerView.setAdapter(new DiaryAdapter(getActivity(), trackedFoodsViewModel, foods));
        // User can delete diary entries
        recyclerView.setHasFixedSize(false);
    }
}