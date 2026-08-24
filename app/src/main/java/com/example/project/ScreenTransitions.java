package com.example.project;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.Interpolator;

public final class ScreenTransitions {
    static final String EXTRA_ANIMATE_TOP_LEVEL =
            "com.example.project.extra.ANIMATE_TOP_LEVEL";
    private static final long TOP_LEVEL_DURATION_MS = 170L;
    private static final float TOP_LEVEL_START_ALPHA = 0.96f;
    private static final float TOP_LEVEL_OFFSET_DP = 6f;

    private ScreenTransitions() {
    }

    public static void startTopLevel(Activity activity, Intent intent) {
        intent.putExtra(EXTRA_ANIMATE_TOP_LEVEL, true);
        activity.startActivity(intent);
        apply(activity, 0, 0);
    }

    public static void startForward(Activity activity, Intent intent) {
        activity.startActivity(intent);
        apply(activity, R.anim.screen_forward_enter, R.anim.screen_forward_exit);
    }

    public static void applyBackward(Activity activity) {
        apply(activity, R.anim.screen_back_enter, R.anim.screen_back_exit);
    }

    static void animateTopLevelContent(Activity activity) {
        View content = activity.findViewById(android.R.id.content);
        View bottomNavigation = activity.findViewById(R.id.bottomNavigation);
        if (!(content instanceof ViewGroup) || bottomNavigation == null) {
            return;
        }

        ViewGroup contentGroup = (ViewGroup) content;
        ViewGroup pageRoot = contentGroup;
        if (contentGroup.getChildCount() == 1
                && contentGroup.getChildAt(0) instanceof ViewGroup) {
            pageRoot = (ViewGroup) contentGroup.getChildAt(0);
        }

        float startOffset = TOP_LEVEL_OFFSET_DP
                * activity.getResources().getDisplayMetrics().density;
        Interpolator interpolator = AnimationUtils.loadInterpolator(
                activity,
                android.R.interpolator.fast_out_slow_in);
        for (int index = 0; index < pageRoot.getChildCount(); index++) {
            View child = pageRoot.getChildAt(index);
            if (child == bottomNavigation) {
                continue;
            }
            child.animate().cancel();
            child.setAlpha(TOP_LEVEL_START_ALPHA);
            child.setTranslationY(startOffset);
            child.animate()
                    .alpha(1f)
                    .translationY(0f)
                    .setDuration(TOP_LEVEL_DURATION_MS)
                    .setInterpolator(interpolator)
                    .start();
        }
    }

    @SuppressWarnings("deprecation")
    private static void apply(Activity activity, int enterAnimation, int exitAnimation) {
        activity.overridePendingTransition(enterAnimation, exitAnimation);
    }
}
