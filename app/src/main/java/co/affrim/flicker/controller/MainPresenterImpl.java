package co.affrim.flicker.controller;

import android.support.annotation.NonNull;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import co.affrim.flicker.R;
import co.affrim.flicker.listener.MainPresenter;
import co.affrim.flicker.model.Photo;
import co.affrim.flicker.model.PhotosBaseResponse;
import co.affrim.flicker.network.RetrofitService;
import co.affrim.flicker.listener.MainView;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;

/**
 * This class implements the logic which are needed for the UI
 * MainPresenter - controller for MainActivity
 */
public class MainPresenterImpl implements MainPresenter {

    private static final String TAG = "MainPresenterImpl";

    private CompositeDisposable mCompositeDisposable = null;
    private MainView mMainView;

    public MainPresenterImpl(@NonNull MainView view) {
        this.mMainView = view;
    }

    @Override
    public void onAttach() {
        mCompositeDisposable = new CompositeDisposable();
    }

    @Override
    public void prepareToExit() {
        mCompositeDisposable.dispose();
    }

    @Override
    public void onDetach() {
        mCompositeDisposable = null;
    }

    @Override
    public void getPhotosForUserQuery(@NonNull String query) {
        Log.d(TAG, "getPhotosForUserQuery " + query);
        mCompositeDisposable.add(RetrofitService.getInstance().getSearchResults(query)
                .subscribeOn(Schedulers.single())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(fetchPhotosObserver()));
    }

    private DisposableSingleObserver<PhotosBaseResponse> fetchPhotosObserver() {
        return new DisposableSingleObserver<PhotosBaseResponse>() {
            @Override
            public void onSuccess(PhotosBaseResponse value) {
                Log.d(TAG, "fetchPhotosObserver onSuccess ");

                if (value != null &&
                        value.getPage() != null &&
                        value.getPage().getPhoto() != null &&
                        value.getPage().getPhoto().size() > 0) {

                    List<Photo> mPhotosFromServer = new ArrayList<>();
                    mPhotosFromServer.addAll(value.getPage().getPhoto());
                    mMainView.addNewItemsToList(mPhotosFromServer);
                }
            }

            @Override
            public void onError(Throwable e) {
                Log.d(TAG, "fetchPhotosObserver onError ");
                mMainView.onError(R.string.error_message);
            }
        };
    }

}
