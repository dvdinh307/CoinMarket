package midas.coinmarket.view;

import android.content.SearchRecentSuggestionsProvider;

import java.util.Random;

public class SuggestionProvider extends SearchRecentSuggestionsProvider {

    public final static String AUTHORITY = SuggestionProvider.class.getName();
    public final static int MODE = SearchRecentSuggestionsProvider.DATABASE_MODE_2LINES
            | SearchRecentSuggestionsProvider.DATABASE_MODE_QUERIES;

    public SuggestionProvider() {
        super();
        setupSuggestions(AUTHORITY, MODE);
    }

}
