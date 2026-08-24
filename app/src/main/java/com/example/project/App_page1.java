package com.example.project;
import android.os.Bundle;
import android.widget.Button;
import android.content.Intent;
import android.view.View;

public class App_page1 extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ui_page1);

        Button buttonGetStart = findViewById(R.id.button_getStart);
        buttonGetStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Class<?> destination = AppSettings.hasProfile(App_page1.this)
                        ? App_page3.class
                        : App_page2.class;
                Intent intent = new Intent(App_page1.this, destination);
                if (destination == App_page3.class) {
                    intent.putExtra(
                            AppContracts.EXTRA_LEVEL,
                            AppSettings.getDiabetesType(App_page1.this).getCode());
                }
                ScreenTransitions.startForward(App_page1.this, intent);
            }
        });
    }
}
