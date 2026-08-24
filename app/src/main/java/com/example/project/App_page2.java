package com.example.project;

import android.content.Intent;
import android.os.Bundle;
import com.google.android.material.textfield.MaterialAutoCompleteTextView;
import com.google.android.material.textfield.TextInputLayout;
import android.widget.Button;
import com.google.android.material.datepicker.CalendarConstraints;
import com.google.android.material.datepicker.DateValidatorPointBackward;
import com.google.android.material.datepicker.MaterialDatePicker;
import android.widget.TextView;
import android.widget.EditText;
import android.widget.Toast;
import android.view.View;
import java.util.Date;
import java.text.DateFormat;


public class App_page2 extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ui_page2);
        findViewById(R.id.buttonBack).setOnClickListener(view -> finish());
        findViewById(R.id.profilePhotoButton).setOnClickListener(view -> Toast.makeText(
                this,
                R.string.profile_photo_unavailable,
                Toast.LENGTH_SHORT).show());

        EditText inputName = findViewById(R.id.inputName);

        EditText inputHeight = findViewById(R.id.inputHeight);
        EditText inputWeight = findViewById(R.id.inputWeight);
        EditText inputDay = findViewById(R.id.inputDay);
        TextInputLayout birthDateLayout = findViewById(R.id.birthDateLayout);
        inputDay.setKeyListener(null);
        View.OnClickListener datePickerClickListener = view -> {
            CalendarConstraints constraints = new CalendarConstraints.Builder()
                    .setValidator(DateValidatorPointBackward.now())
                    .build();
            MaterialDatePicker<Long> datePicker = MaterialDatePicker.Builder.datePicker()
                    .setTitleText(R.string.select_birth_date)
                    .setCalendarConstraints(constraints)
                    .build();
            datePicker.show(getSupportFragmentManager(), "DATE_PICKER");
            datePicker.addOnPositiveButtonClickListener(selection -> {
                Date date = new Date(selection);
                DateFormat dateFormat = DateFormat.getDateInstance(
                        DateFormat.MEDIUM,
                        getResources().getConfiguration().getLocales().get(0));
                inputDay.setText(dateFormat.format(date));
            });
        };
        inputDay.setOnClickListener(datePickerClickListener);
        birthDateLayout.setEndIconOnClickListener(datePickerClickListener);


        MaterialAutoCompleteTextView spinner1 = findViewById(R.id.inputSex);
        String[] sex = getResources().getStringArray(R.array.sex_options);
        spinner1.setSimpleItems(sex);

        spinner1.setOnClickListener(view -> spinner1.showDropDown());

        MaterialAutoCompleteTextView spinner2 = findViewById(R.id.inputDiabetes);
        String[] diabetesLevels = getResources().getStringArray(R.array.diabetes_options);
        spinner2.setSimpleItems(diabetesLevels);

        spinner2.setOnClickListener(view -> spinner2.showDropDown());

        boolean editMode = getIntent().getBooleanExtra(AppContracts.EXTRA_EDIT_PROFILE, false);
        String editSection = getIntent().getStringExtra(
                AppContracts.EXTRA_PROFILE_EDIT_SECTION);
        boolean personalEdit = editMode
                && AppContracts.PROFILE_SECTION_PERSONAL.equals(editSection);
        boolean healthEdit = editMode
                && AppContracts.PROFILE_SECTION_HEALTH.equals(editSection);
        View personalInformationSection = findViewById(R.id.personalInformationSection);
        View healthInformationSection = findViewById(R.id.healthInformationSection);
        View profilePrivacyCard = findViewById(R.id.profilePrivacyCard);
        TextView formTitle = findViewById(R.id.formTitle);
        if (personalEdit) {
            healthInformationSection.setVisibility(View.GONE);
            profilePrivacyCard.setVisibility(View.GONE);
            formTitle.setText(R.string.edit_personal_information);
        } else if (healthEdit) {
            personalInformationSection.setVisibility(View.GONE);
            formTitle.setText(R.string.health_info_title);
        }

        if (AppSettings.hasProfile(this)) {
            inputName.setText(AppSettings.getUserName(this));
            inputHeight.setText(AppSettings.getHeight(this));
            inputWeight.setText(AppSettings.getWeight(this));
            inputDay.setText(AppSettings.getBirthDate(this));
            int sexPosition = AppSettings.getSexPosition(this);
            if (sexPosition >= 0 && sexPosition < sex.length) {
                spinner1.setText(sex[sexPosition], false);
            }
            int diabetesPosition = AppSettings.getDiabetesType(this).getPosition();
            if (diabetesPosition >= 0 && diabetesPosition < diabetesLevels.length) {
                spinner2.setText(diabetesLevels[diabetesPosition], false);
            }
        }

        TextView buttonErrorText = findViewById(R.id.buttonErrorText);

        Button button_Record = findViewById(R.id.button_Record);
        button_Record.setOnClickListener(v -> {
            String name = inputName.getText().toString().trim();
            String height = inputHeight.getText().toString().trim();
            String weight = inputWeight.getText().toString().trim();
            String date = inputDay.getText().toString().trim();
            String selectedSex = spinner1.getText().toString().trim();
            int selectedSexPosition = positionOf(selectedSex, sex);
            String diabetesLabel = spinner2.getText().toString().trim();
            DiabetesType diabetesType = diabetesTypeForLabel(diabetesLabel, diabetesLevels);
            String diabetesTypeCode = diabetesType.getCode();

            ProfileValidator.Result validation = ProfileValidator.validate(
                    name, height, weight, date, selectedSex, diabetesTypeCode);
            if (validation != ProfileValidator.Result.VALID) {
                buttonErrorText.setText(errorMessageFor(validation));
                buttonErrorText.setVisibility(View.VISIBLE);
                return;
            }

            buttonErrorText.setVisibility(View.GONE);
            AppSettings.saveProfile(
                    this,
                    name,
                    height,
                    weight,
                    date,
                    selectedSexPosition,
                    diabetesType);
            if (editMode) {
                setResult(RESULT_OK);
                finish();
                return;
            }
            Intent intent = new Intent(App_page2.this, App_page3.class);
            intent.putExtra(AppContracts.EXTRA_USER_NAME, name);
            intent.putExtra(AppContracts.EXTRA_HEIGHT, height);
            intent.putExtra(AppContracts.EXTRA_WEIGHT, weight);
            intent.putExtra(AppContracts.EXTRA_BIRTH_DATE, date);
            intent.putExtra(AppContracts.EXTRA_SEX, selectedSex);
            intent.putExtra(AppContracts.EXTRA_LEVEL, diabetesTypeCode);
            ScreenTransitions.startForward(this, intent);
        });


    }

    @Override
    public void finish() {
        super.finish();
        ScreenTransitions.applyBackward(this);
    }

    private DiabetesType diabetesTypeForLabel(String selectedLabel, String[] labels) {
        for (int index = 0; index < labels.length; index++) {
            if (labels[index].equals(selectedLabel)) {
                return DiabetesType.fromPosition(index);
            }
        }
        return DiabetesType.UNKNOWN;
    }

    private int positionOf(String selectedLabel, String[] labels) {
        for (int index = 0; index < labels.length; index++) {
            if (labels[index].equals(selectedLabel)) {
                return index;
            }
        }
        return -1;
    }

    private int errorMessageFor(ProfileValidator.Result result) {
        switch (result) {
            case INVALID_NAME:
                return R.string.error_invalid_name;
            case INVALID_HEIGHT:
                return R.string.error_invalid_height;
            case INVALID_WEIGHT:
                return R.string.error_invalid_weight;
            case MISSING_FIELD:
            default:
                return R.string.error_missing_fields;
        }
    }
}
