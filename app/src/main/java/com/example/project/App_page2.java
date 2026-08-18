package com.example.project;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import com.google.android.material.datepicker.CalendarConstraints;
import com.google.android.material.datepicker.DateValidatorPointBackward;
import com.google.android.material.datepicker.MaterialDatePicker;
import android.widget.TextView;
import android.widget.EditText;
import android.view.View;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;


public class App_page2 extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ui_page2);
        EditText inputName = findViewById(R.id.inputName);

        EditText inputHeight = findViewById(R.id.inputHeight);
        EditText inputWeight = findViewById(R.id.inputWeight);
        EditText inputDay = findViewById(R.id.inputDay);
        inputDay.setFocusable(false);
        inputDay.setOnClickListener(view -> {
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
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(date);
                int yearAD = calendar.get(Calendar.YEAR);
                int yearBE = yearAD + 543;
                String dayMonth = new java.text.SimpleDateFormat("d MMMM", new Locale("th", "TH")).format(date);
                String selectedDate = dayMonth + " " + yearBE;
                inputDay.setText(selectedDate);
            });
        });


        AutoCompleteTextView spinner1 = findViewById(R.id.inputSex);
        spinner1.setFocusable(false);
        String[] sex = getResources().getStringArray(R.array.sex_options);
        ArrayAdapter<String> adapter1 = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, sex);
        spinner1.setAdapter(adapter1);

        AutoCompleteTextView spinner2 = findViewById(R.id.inputDiabetes);
        spinner2.setFocusable(false);
        String[] diabetesLevels = getResources().getStringArray(R.array.diabetes_options);
        ArrayAdapter<String> adapter2 = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, diabetesLevels);
        spinner2.setAdapter(adapter2);

        TextView buttonErrorText = findViewById(R.id.buttonErrorText);

        Button button_Record = findViewById(R.id.button_Record);
        button_Record.setOnClickListener(v -> {
            String name = inputName.getText().toString().trim();
            String height = inputHeight.getText().toString().trim();
            String weight = inputWeight.getText().toString().trim();
            String date = inputDay.getText().toString().trim();
            String selectedSex = spinner1.getText().toString().trim();
            String level = spinner2.getText().toString().trim();

            ProfileValidator.Result validation = ProfileValidator.validate(
                    name, height, weight, date, selectedSex, level);
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
            intent.putExtra(AppContracts.EXTRA_LEVEL, level);
            startActivity(intent);
        });


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
