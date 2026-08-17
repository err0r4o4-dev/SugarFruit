package com.example.project;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import com.google.android.material.datepicker.MaterialDatePicker;
import android.widget.TextView;
import android.widget.EditText;
import android.view.View;
import java.text.SimpleDateFormat;
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
        EditText inputWidth = findViewById(R.id.inputWeight);
        EditText inputDay = findViewById(R.id.inputDay);
        inputDay.setFocusable(false);
        inputDay.setOnClickListener(view -> {
            MaterialDatePicker<Long> datePicker = MaterialDatePicker.Builder.datePicker().setTitleText("").build();
            datePicker.show(getSupportFragmentManager(), "DATE_PICKER");
            datePicker.addOnPositiveButtonClickListener(selection -> {
                SimpleDateFormat sdf = new SimpleDateFormat("d MMMM yyyy", new Locale("th", "TH"));
                Date date = new Date(selection);
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(date);
                int yearAD = calendar.get(Calendar.YEAR);
                int yearBE = yearAD + 543;
                String dayMonth = new SimpleDateFormat("d MMMM", new Locale("th", "TH")).format(date);
                String selectedDate = dayMonth + " " + yearBE;
                inputDay.setText(selectedDate);
            });
        });


        AutoCompleteTextView spinner1 = findViewById(R.id.inputSex);
        spinner1.setFocusable(false);
        String[] sex = {"ชาย", "หญิง"};
        ArrayAdapter<String> adapter1 = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, sex);
        spinner1.setAdapter(adapter1);

        AutoCompleteTextView spinner2 = findViewById(R.id.inputDiabetes);
        spinner2.setFocusable(false);
        String[] country = {"เบาหวานชนิดที่ 1", "เบาหวานชนิดที่ 2", "เบาหวานขณะตั้งครรภ์"};
        ArrayAdapter<String> adapter2 = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, country);
        spinner2.setAdapter(adapter2);

        TextView buttonErrorText = findViewById(R.id.buttonErrorText);

        Button button_Record = findViewById(R.id.button_Record);
        button_Record.setOnClickListener(v -> {
            String name = inputName.getText().toString().trim();
            String height = inputHeight.getText().toString().trim();
            String width = inputWidth.getText().toString().trim();
            String date = inputDay.getText().toString().trim();
            String Sex = spinner1.getText().toString().trim();
            String level = spinner2.getText().toString().trim();

            if (name.isEmpty() || height.isEmpty() || width.isEmpty() || date.isEmpty() || Sex.isEmpty() || level.isEmpty()) {
                buttonErrorText.setText("**กรอกข้อมูลให้ครบ**");
                buttonErrorText.setVisibility(View.VISIBLE);
            } else if (name.matches(".*\\d.*")) {
                buttonErrorText.setText("**กรุณากรอกชื่อเป็นอักษรเท่านั้น**");
                buttonErrorText.setVisibility(View.VISIBLE);
            } else if (!height.matches("\\d+")) {
                buttonErrorText.setText("**กรุณากรอกความสูงเป็นตัวเลขเท่านั้น**");
                buttonErrorText.setVisibility(View.VISIBLE);
            } else if (!width.matches("\\d+")) {
                buttonErrorText.setText("**กรุณากรอกน้ำหนักเป็นตัวเลขเท่านั้น**");
                buttonErrorText.setVisibility(View.VISIBLE);
            } else {
                Intent intent = new Intent(App_page2.this, App_page3.class);
                buttonErrorText.setText("");
                buttonErrorText.setVisibility(View.VISIBLE);
                intent.putExtra("level", level);
                startActivity(intent);
            }
        });


    }
}