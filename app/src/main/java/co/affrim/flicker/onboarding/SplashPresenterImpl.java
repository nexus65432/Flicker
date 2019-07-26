package co.affrim.flicker.onboarding;

import co.affrim.flicker.listener.SplashPresenter;
import co.affrim.flicker.listener.SplashView;

public class SplashPresenterImpl implements SplashPresenter {

    private SplashView mSplashView;

    public SplashPresenterImpl(SplashView view) {
        this.mSplashView = view;
    }

    @Override
    public void onLoginSuccess() {
        mSplashView.openMainActivity();
    }

    @Override
    public void onLoginFailed() {
        mSplashView.showLoginActivity();
    }

    @Override
    public void onDetach() {

    }
}
