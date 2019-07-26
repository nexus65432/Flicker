package co.affrim.flicker.onboarding;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import co.affrim.flicker.MainActivity;
import co.affrim.flicker.R;
import co.affrim.flicker.listener.SplashView;
import co.affrim.flicker.ui.BaseActivity;

/**
 * This class is for showing loading screen or loading animation when opening the application
 * The time interval can change based on the requirement
 */
public class SplashActivity extends BaseActivity implements SplashView {

    public static final int SPLASH_DISPLAY_INTERVAL = 1000;

    private SplashPresenterImpl mPresenter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.splash_layout);

        mPresenter = new SplashPresenterImpl(this);

        // Calling this as we are not validating at this time.
        mPresenter.onLoginSuccess();
    }

    // TODO: Implement logic to show Login screen
    @Override
    public void showLoginActivity() {
        new Handler().postDelayed(new Runnable() {
            public void run() {
                showMainActivity();
            }
        }, SPLASH_DISPLAY_INTERVAL);
    }

    @Override
    public void openMainActivity() {
        showMainActivity();
    }

    private void showMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}
