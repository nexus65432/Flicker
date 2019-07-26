package co.affrim.flicker;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.provider.SearchRecentSuggestions;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import co.affrim.flicker.adapter.ImageAdapter;
import co.affrim.flicker.controller.MainPresenterImpl;
import co.affrim.flicker.datautils.SearchSuggestionProvider;
import co.affrim.flicker.listener.MainView;
import co.affrim.flicker.model.Photo;
import co.affrim.flicker.ui.BaseActivity;
import co.affrim.flicker.util.NetworkUtils;

public class MainActivity extends BaseActivity implements MainView, SearchView.OnQueryTextListener  {

    private static final String TAG = "MainActivity";

    private static final int GRID_COUNT = 2;

    private Toolbar mToolbar;
    private TextView mToolbarTitle;
    private RecyclerView mRecyclerGridView;
    private TextView mDefaultMessage;
    private MenuItem mSearchViewItem;
    private SearchView mSearchView;
    private ImageAdapter mImageAdapter;

    private List<Photo> mPhoto = new ArrayList<>();
    private MainPresenterImpl mMainPresenterImpl;
    private String mUserQuery;

    boolean isLoading = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.d(TAG, " onCreate ");

        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
                .detectAll()
                .detectDiskReads()
                .detectDiskWrites()
                .detectNetwork()
                // alternatively .detectAll() for all detectable problems
                .penaltyLog()
                .penaltyDeath()
                .build());

        StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
                .detectLeakedSqlLiteObjects()
                .detectLeakedClosableObjects()
                // alternatively .detectAll() for all detectable problems
                .penaltyLog()
                .penaltyDeath()
                .build());

        setContentView(R.layout.activity_main);

        mDefaultMessage = findViewById(R.id.error_message);
        setupToolbar();
        setUpRecyclerView();
        initScrollListener();

        mMainPresenterImpl = new MainPresenterImpl(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        mMainPresenterImpl.onAttach();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.search_view_menu_item, menu);

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        mSearchViewItem = menu.findItem(R.id.search);
        mSearchView = (SearchView) mSearchViewItem.getActionView();

        mSearchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        mSearchView.setSubmitButtonEnabled(false);
        mSearchView.setIconifiedByDefault(false);
        mSearchView.setOnQueryTextListener(this);

        return true;
    }

    /**
     * ToDo:
     * This is called when user selects data from history
     * @param intent
     */
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            SearchRecentSuggestions suggestions = new SearchRecentSuggestions(this,
                    SearchSuggestionProvider.AUTHORITY, SearchSuggestionProvider.MODE);
            suggestions.saveRecentQuery(query, null);
            //mSearchView.setQuery(query, true);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        // ToDo: Clear history
        clearQueryHistory();
        mMainPresenterImpl.prepareToExit();
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        Log.d(TAG, " onQueryTextSubmit  " + query);
        if (NetworkUtils.getInstance().isNetworkAvailable(MainActivity.this)) {
            // ToDo: Save Query and load from cache
            //saveQuery(query);
            prepareForNewList();
            hideKeyboard();
            mUserQuery = query;
            isLoading = false;
            mMainPresenterImpl.getPhotosForUserQuery(mUserQuery);
        }
        if (mSearchView != null) {
            mSearchView.clearFocus();
        }
        return true;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return false;
    }

    @Override
    public void addNewItemsToList(@NonNull List<Photo> results) {
        Log.d(TAG, " addNewItemsToList received list size " + results.size());
        if (results != null && results.size() > 0) {
            mPhoto.addAll(results);
            mImageAdapter.addList(mPhoto);
            updateMessage(false);
            mImageAdapter.notifyDataSetChanged();
            isLoading = false;
        }
    }

    @Override
    public void showEmptyList() {
        updateSearchStatus(R.string.error_message);
        mPhoto.clear();
        mImageAdapter.replaceWithNewList(mPhoto);
        mImageAdapter.notifyDataSetChanged();
    }

    @Override
    public void hideKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    @Override
    public void prepareForNewList() {
        mPhoto.clear();
        updateSearchStatus(R.string.loading_images);
    }

    @Override
    public void updateSearchStatus(int resource) {
        mDefaultMessage.setText(getString(resource));
        mRecyclerGridView.setVisibility(View.GONE);
    }

    @Override
    public void onError(int resource) {
    }

    @Override
    public void onSuccess() {
        updateMessage(false);
    }

    @Override
    public void onError() {
        updateMessage(true);
    }

    @Override
    public void saveQuery(@NonNull String query) {
        SearchRecentSuggestions suggestions = new SearchRecentSuggestions(this,
                SearchSuggestionProvider.AUTHORITY, SearchSuggestionProvider.MODE);
        suggestions.saveRecentQuery(query, null);
    }

    @Override
    public void clearQueryHistory() {
        SearchRecentSuggestions suggestions = new SearchRecentSuggestions(this,
                SearchSuggestionProvider.AUTHORITY, SearchSuggestionProvider.MODE);
        suggestions.clearHistory();
    }

    private void setupToolbar() {
        mToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        mToolbarTitle = findViewById(R.id.toolbar_title);
        mToolbarTitle.setText(getString(R.string.actionbar_title));
    }

    /**
     * Setup recyclerView
     */
    private void setUpRecyclerView() {
        mImageAdapter = new ImageAdapter(this, mPhoto);
        mRecyclerGridView = findViewById(R.id.recyclerView);
        mRecyclerGridView.setAdapter(mImageAdapter);

        GridLayoutManager manager = new GridLayoutManager(this, GRID_COUNT, GridLayoutManager.VERTICAL, false);
        mRecyclerGridView.setLayoutManager(manager);
    }

    private void initScrollListener() {
        mRecyclerGridView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (!isLoading) {
                    LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                    if (linearLayoutManager != null &&
                            linearLayoutManager.findLastCompletelyVisibleItemPosition() >= (mPhoto.size() - 25)) {
                        mMainPresenterImpl.getPhotosForUserQuery(mUserQuery);
                        isLoading = true;
                    }
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        mImageAdapter.onDestroy();
        super.onDestroy();
    }

    private void updateMessage(boolean show) {
        mDefaultMessage.setVisibility(show ? View.VISIBLE : View.GONE);
        mRecyclerGridView.setVisibility(show ? View.GONE : View.VISIBLE);
    }

}
