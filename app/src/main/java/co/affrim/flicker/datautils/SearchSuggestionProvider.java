package co.affrim.flicker.datautils;

import android.content.SearchRecentSuggestionsProvider;

/**
 * This superclass is used to create a simple search suggestions provider for the application.
 * It creates suggestions (as the user types) based on recent queries and/or recent views.
 */
public class SearchSuggestionProvider extends SearchRecentSuggestionsProvider {

    public final static String AUTHORITY = SearchSuggestionProvider.class.getName();
    public final static int MODE = DATABASE_MODE_QUERIES;

    public SearchSuggestionProvider() {
        setupSuggestions(AUTHORITY, MODE);
    }
}