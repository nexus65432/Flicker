package co.affrim.flicker.listener;

import android.support.annotation.NonNull;

import java.util.List;

import co.affrim.flicker.model.Photo;

/**
 * This interface will be extended by a more specific interface
 * that then usually will be implemented by an Activity or Fragment.
 */
public interface MainView extends SearchListener, NetworkStatusListener {

    /**
     * Add photos to the adapter
     * @param results
     */
    void addNewItemsToList(@NonNull List<Photo> results);

    /**
     * Clear the list and present user with right full information
     */
    void showEmptyList();

    /**
     * Prepare for showing new lists when user request with new search
     */
    void prepareForNewList();

    /**
     * Hide keyboard after user action
     */
    void hideKeyboard();
}
