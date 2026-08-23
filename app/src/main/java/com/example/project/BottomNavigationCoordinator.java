package com.example.project;

import android.app.Activity;
import android.content.Intent;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public final class BottomNavigationCoordinator {
    public enum Destination {
        HOME(R.id.navigation_home),
        SAVED(R.id.navigation_saved),
        PROFILE(R.id.navigation_profile);

        private final int menuItemId;

        Destination(int menuItemId) {
            this.menuItemId = menuItemId;
        }
    }

    private BottomNavigationCoordinator() {
    }

    public static void bind(Activity activity, BottomNavigationView navigationView,
            Destination currentDestination) {
        selectCurrent(navigationView, currentDestination);
        navigationView.setOnItemReselectedListener(item -> {
            // The current Activity already owns the selected destination.
        });
        navigationView.setOnItemSelectedListener(item -> {
            Destination target = destinationFor(item.getItemId());
            if (target == null) {
                return false;
            }
            if (target == currentDestination) {
                return true;
            }

            Intent intent = new Intent(activity, activityClassFor(target));
            intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            if (target == Destination.HOME) {
                intent.putExtra(
                        AppContracts.EXTRA_LEVEL,
                        AppSettings.getDiabetesType(activity).getCode());
            }
            activity.startActivity(intent);
            return true;
        });
    }

    public static void selectCurrent(BottomNavigationView navigationView,
            Destination currentDestination) {
        navigationView.setSelectedItemId(currentDestination.menuItemId);
    }

    private static Destination destinationFor(int itemId) {
        for (Destination destination : Destination.values()) {
            if (destination.menuItemId == itemId) {
                return destination;
            }
        }
        return null;
    }

    private static Class<? extends Activity> activityClassFor(Destination destination) {
        switch (destination) {
            case SAVED:
                return App_saved.class;
            case PROFILE:
                return App_profile.class;
            case HOME:
            default:
                return App_page3.class;
        }
    }
}
