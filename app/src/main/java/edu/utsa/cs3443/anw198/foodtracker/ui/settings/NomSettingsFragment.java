package edu.utsa.cs3443.anw198.foodtracker.ui.settings;

import android.os.Bundle;
import android.provider.SearchRecentSuggestions;

import androidx.appcompat.app.AlertDialog;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

import edu.utsa.cs3443.anw198.foodtracker.R;
import edu.utsa.cs3443.anw198.foodtracker.providers.FoodSearchSuggestionProvider;

public class NomSettingsFragment extends PreferenceFragmentCompat {
    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.preferences, rootKey);
        //EditTextPreference signaturePreference = findPreference("signature");
        Preference clearSuggestions = findPreference("clear-suggestions");
        clearSuggestions.setOnPreferenceClickListener(preference -> {
            new AlertDialog.Builder(preference.getContext())
                    .setTitle("Clear search suggestions")
                    .setMessage("Are you sure you want to delete all saved search suggestions?")
                    .setPositiveButton(android.R.string.ok, (dialogInterface, i) -> {
                        SearchRecentSuggestions suggestions = new SearchRecentSuggestions(preference.getContext(),
                                FoodSearchSuggestionProvider.AUTHORITY, FoodSearchSuggestionProvider.MODE);
                        suggestions.clearHistory();
                    })
                    .setNegativeButton(android.R.string.cancel, null).show();
            return true;
        });
    }
}
