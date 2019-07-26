package co.affrim.flicker.listener;

import android.support.annotation.StringRes;

public interface NetworkStatusListener {

    void updateSearchStatus(@StringRes int resource);

    void onSuccess();

    void onError();

    void onError(@StringRes int resource);
}
