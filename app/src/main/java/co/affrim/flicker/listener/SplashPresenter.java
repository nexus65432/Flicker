package co.affrim.flicker.listener;


public interface SplashPresenter {

    /**
     * On user successfully authenticated
     */
    void onLoginSuccess();

    /**
     * When user authentication failed
     */
    void onLoginFailed();

    /**
     * Cleanup resources when activity is destroyed
     */
    void onDetach();
}
