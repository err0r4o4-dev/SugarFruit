package com.example.project;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.MaterialAutoCompleteTextView;
import com.google.android.material.textfield.TextInputLayout;
import android.widget.Button;
import com.google.android.material.datepicker.CalendarConstraints;
import com.google.android.material.datepicker.DateValidatorPointBackward;
import com.google.android.material.datepicker.MaterialDatePicker;
import android.widget.TextView;
import android.widget.EditText;
import android.view.View;
import java.util.Date;
import java.text.DateFormat;


public class App_page2 extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ui_page2);
        findViewById(R.id.buttonBack).setOnClickListener(view -> finish());

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

        TextView buttonErrorText = findViewById(R.id.buttonErrorText);

        Button button_Record = findViewById(R.id.button_Record);
        button_Record.setOnClickListener(v -> {
            String name = inputName.getText().toString().trim();
            String height = inputHeight.getText().toString().trim();
            String weight = inputWeight.getText().toString().trim();
            String date = inputDay.getText().toString().trim();
            String selectedSex = spinner1.getText().toString().trim();
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
            Intent intent = new Intent(App_page2.this, App_page3.class);
            intent.putExtra(AppContracts.EXTRA_USER_NAME, name);
            intent.putExtra(AppContracts.EXTRA_HEIGHT, height);
            intent.putExtra(AppContracts.EXTRA_WEIGHT, weight);
            intent.putExtra(AppContracts.EXTRA_BIRTH_DATE, date);
            intent.putExtra(AppContracts.EXTRA_SEX, selectedSex);
            intent.putExtra(AppContracts.EXTRA_LEVEL, diabetesTypeCode);
            startActivity(intent);
        });


    }

    private DiabetesType diabetesTypeForLabel(String selectedLabel, String[] labels) {
        for (int index = 0; index < labels.length; index++) {
            if (labels[index].equals(selectedLabel)) {
                return DiabetesType.fromPosition(index);
            }
        }
        return DiabetesType.UNKNOWN;
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
