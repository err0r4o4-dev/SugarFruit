package com.example.project;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatDelegate;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.switchmaterial.SwitchMaterial;

public class App_profile extends BaseActivity {
    private TextView userNameTextView;
    private TextView fontSizeValueTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ui_profile);

        userNameTextView = findViewById(R.id.profileUserName);
        findViewById(R.id.profilePhotoButton).setOnClickListener(view -> Toast.makeText(
                this,
                R.string.profile_photo_unavailable,
                Toast.LENGTH_SHORT).show());

        setupUserInformation();
        setupApplicationSettings();
        setupPrivacy();
        setupAdditionalInformation();

        BottomNavigationCoordinator.bind(
                this,
                (BottomNavigationView) findViewById(R.id.bottomNavigation),
                BottomNavigationCoordinator.Destination.PROFILE);
    }

    @Override
    protected void onResume() {
        super.onResume();
        BottomNavigationCoordinator.selectCurrent(
                (BottomNavigationView) findViewById(R.id.bottomNavigation),
                BottomNavigationCoordinator.Destination.PROFILE);
        if (userNameTextView != null) {
            bindProfileHeader();
        }
    }

    private void setupUserInformation() {
        LinearLayout container = findViewById(R.id.profileUserInfoContainer);
        addActionRow(
                container,
                R.drawable.ic_profile_person,
                R.string.edit_personal_information,
                null,
                view -> openProfileEditor(AppContracts.PROFILE_SECTION_PERSONAL));
        addActionRow(
                container,
                R.drawable.ic_detail_check,
                R.string.health_info_title,
                null,
                view -> openProfileEditor(AppContracts.PROFILE_SECTION_HEALTH));
        hideLastDivider(container);
    }

    private void setupApplicationSettings() {
        LinearLayout container = findViewById(R.id.profileAppSettingsContainer);
        View darkModeItem = addActionRow(
                container,
                R.drawable.ic_profile_dark_mode,
                R.string.dark_mode,
                null,
                null);
        View darkModeRow = darkModeItem.findViewById(R.id.profileActionRow);
        SwitchMaterial darkModeSwitch = darkModeItem.findViewById(R.id.profileActionSwitch);
        darkModeSwitch.setVisibility(View.VISIBLE);
        darkModeItem.findViewById(R.id.profileActionChevron).setVisibility(View.GONE);
        darkModeSwitch.setChecked(AppSettings.isDarkModeEnabled(this));
        darkModeSwitch.setOnCheckedChangeListener((button, isChecked) -> {
            AppSettings.setDarkModeEnabled(this, isChecked);
            AppCompatDelegate.setDefaultNightMode(
                    isChecked
                            ? AppCompatDelegate.MODE_NIGHT_YES
                            : AppCompatDelegate.MODE_NIGHT_NO);
        });
        darkModeRow.setOnClickListener(view -> darkModeSwitch.toggle());

        View fontSizeItem = addActionRow(
                container,
                R.drawable.ic_profile_font_size,
                R.string.font_size,
                fontScaleLabel(AppSettings.getFontScale(this)),
                view -> showFontSizeDialog());
        fontSizeValueTextView = fontSizeItem.findViewById(R.id.profileActionValue);
        hideLastDivider(container);
    }

    private void setupPrivacy() {
        LinearLayout container = findViewById(R.id.profilePrivacyContainer);
        addActionRow(
                container,
                R.drawable.ic_profile_lock,
                R.string.privacy_settings,
                null,
                view -> showInformationDialog(
                        R.string.privacy_settings,
                        R.string.privacy_settings_message));
        hideLastDivider(container);
    }

    private void setupAdditionalInformation() {
        LinearLayout container = findViewById(R.id.profileAdditionalContainer);
        addActionRow(
                container,
                R.drawable.ic_detail_warning,
                R.string.nutrition_data_sources,
                null,
                view -> showInformationDialog(
                        R.string.nutrition_data_sources,
                        R.string.detail_source));
        addActionRow(
                container,
                R.drawable.ic_profile_info,
                R.string.about_application,
                null,
                view -> showInformationDialog(
                        R.string.about_application,
                        R.string.about_application_message));
        hideLastDivider(container);
    }

    private View addActionRow(LinearLayout container, int iconResource, int titleResource,
            Integer valueResource, View.OnClickListener clickListener) {
        View item = LayoutInflater.from(this).inflate(
                R.layout.item_profile_action,
                container,
                false);
        ImageView icon = item.findViewById(R.id.profileActionIcon);
        TextView title = item.findViewById(R.id.profileActionTitle);
        TextView value = item.findViewById(R.id.profileActionValue);
        View row = item.findViewById(R.id.profileActionRow);
        icon.setImageResource(iconResource);
        title.setText(titleResource);
        row.setContentDescription(getString(titleResource));
        if (valueResource != null) {
            value.setText(valueResource);
            value.setVisibility(View.VISIBLE);
        }
        if (clickListener != null) {
            row.setOnClickListener(clickListener);
        }
        container.addView(item);
        return item;
    }

    private void hideLastDivider(LinearLayout container) {
        if (container.getChildCount() > 0) {
            container.getChildAt(container.getChildCount() - 1)
                    .findViewById(R.id.profileActionDivider)
                    .setVisibility(View.GONE);
        }
    }

    private void bindProfileHeader() {
        String name = AppSettings.getUserName(this);
        userNameTextView.setText(name.trim().isEmpty()
                ? getString(R.string.profile_user_name_placeholder)
                : name);
    }

    private void openProfileEditor(String section) {
        Intent intent = new Intent(this, App_page2.class);
        intent.putExtra(AppContracts.EXTRA_EDIT_PROFILE, true);
        intent.putExtra(AppContracts.EXTRA_PROFILE_EDIT_SECTION, section);
        startActivity(intent);
    }

    private void showFontSizeDialog() {
        float currentScale = AppSettings.getFontScale(this);
        int checkedItem = currentScale == AppSettings.FONT_SCALE_SMALL
                ? 0
                : currentScale == AppSettings.FONT_SCALE_LARGE ? 2 : 1;
        new MaterialAlertDialogBuilder(this)
                .setTitle(R.string.font_size)
                .setSingleChoiceItems(
                        R.array.font_size_options,
                        checkedItem,
                        (dialog, which) -> {
                            float scale = which == 0
                                    ? AppSettings.FONT_SCALE_SMALL
                                    : which == 2
                                            ? AppSettings.FONT_SCALE_LARGE
                                            : AppSettings.FONT_SCALE_STANDARD;
                            AppSettings.setFontScale(this, scale);
                            fontSizeValueTextView.setText(fontScaleLabel(scale));
                            dialog.dismiss();
                            recreate();
                        })
                .setNegativeButton(R.string.cancel, null)
                .show();
    }

    private int fontScaleLabel(float scale) {
        if (scale == AppSettings.FONT_SCALE_SMALL) {
            return R.string.font_size_small;
        }
        if (scale == AppSettings.FONT_SCALE_LARGE) {
            return R.string.font_size_large;
        }
        return R.string.font_size_standard;
    }

    private void showInformationDialog(int titleResource, int messageResource) {
        new MaterialAlertDialogBuilder(this)
                .setTitle(titleResource)
                .setMessage(messageResource)
                .setPositiveButton(android.R.string.ok, null)
                .show();
    }

}
