package com.example.project;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.core.app.ActivityScenario;
import androidx.test.core.app.ApplicationProvider;

import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {
    @Test
    public void useAppContext() {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        assertEquals("com.example.project", appContext.getPackageName());
    }

    @Test
    public void internalActivities_areNotExported() throws PackageManager.NameNotFoundException {
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        assertActivityIsNotExported(appContext, App_page2.class);
        assertActivityIsNotExported(appContext, App_page3.class);
        assertActivityIsNotExported(appContext, App_page4.class);
    }

    private void assertActivityIsNotExported(Context context, Class<?> activityClass)
            throws PackageManager.NameNotFoundException {
        ActivityInfo activityInfo = context.getPackageManager().getActivityInfo(
                new ComponentName(context, activityClass), 0);
        assertFalse(activityInfo.exported);
    }

    @Test
    public void detailWithInvalidImage_usesFallbackWithoutCrashing() {
        Intent intent = new Intent(ApplicationProvider.getApplicationContext(), App_page4.class);
        intent.putExtra(AppContracts.EXTRA_FRUIT_IMAGE, Integer.MAX_VALUE);

        try (ActivityScenario<App_page4> scenario = ActivityScenario.launch(intent)) {
            scenario.onActivity(activity -> assertNotNull(
                    activity.<android.widget.ImageView>findViewById(R.id.detail_image).getDrawable()));
        }
    }

    @Test
    public void detailBackButton_finishesCurrentActivity() {
        Intent intent = new Intent(ApplicationProvider.getApplicationContext(), App_page4.class);

        try (ActivityScenario<App_page4> scenario = ActivityScenario.launch(intent)) {
            scenario.onActivity(activity -> {
                activity.findViewById(R.id.button_Next).performClick();
                assertTrue(activity.isFinishing());
            });
        }
    }

    @Test
    public void launcherHasNoActionBar() {
        try (ActivityScenario<App_page1> scenario = ActivityScenario.launch(App_page1.class)) {
            scenario.onActivity(activity -> assertNull(activity.getSupportActionBar()));
        }
    }
}
