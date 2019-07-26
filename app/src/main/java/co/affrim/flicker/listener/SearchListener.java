package co.affrim.flicker.listener;

import android.support.annotation.NonNull;

public interface SearchListener {

    /**
     * Save search history to default search content provider
     * @param query
     */
    void saveQuery(@NonNull String query);

    /**
     * Clear Search History
     */
    void clearQueryHistory();
}
