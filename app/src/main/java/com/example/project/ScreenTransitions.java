package com.example.project;

import android.app.Activity;
import android.content.Intent;

public final class ScreenTransitions {
    private ScreenTransitions() {
    }

    public static void startTopLevel(Activity activity, Intent intent) {
        activity.startActivity(intent);
        apply(activity, R.anim.navigation_fade_in, R.anim.navigation_fade_out);
    }

    public static void startForward(Activity activity, Intent intent) {
        activity.startActivity(intent);
        apply(activity, R.anim.screen_forward_enter, R.anim.screen_forward_exit);
    }

    public static void applyBackward(Activity activity) {
        apply(activity, R.anim.screen_back_enter, R.anim.screen_back_exit);
    }

    @SuppressWarnings("deprecation")
    private static void apply(Activity activity, int enterAnimation, int exitAnimation) {
        activity.overridePendingTransition(enterAnimation, exitAnimation);
    }
}
