package edu.floridapoly.mobiledeviceapplications.fall22.triviachance;

import android.app.Activity;

import android.content.Intent;

public class ThemeUtil {
    private static int cTheme;

    public static void changeToTheme(Activity activity, int theme) {
        cTheme = theme;
        activity.finish();
        activity.startActivity(new Intent(activity, activity.getClass()));
    }

    public static void onActivityCreateTheme(Activity activity) {
        switch (cTheme) {
            case R.style.Theme_TriviaChance_Gold:
                activity.setTheme(R.style.Theme_TriviaChance_Gold);
                break;
            default:
                activity.setTheme(R.style.Theme_TriviaChance_Main);
                break;
        }

    }

    public static int getCurrentTheme() {
        return cTheme;
    }
}
