package com.example.project;
import android.os.Bundle;
import android.widget.Button;
import android.content.Intent;
import android.view.View;

public class App_page1 extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (AppSettings.hasProfile(this)) {
            openHome();
            return;
        }

        setContentView(R.layout.ui_page1);

        Button buttonGetStart = findViewById(R.id.button_getStart);
        buttonGetStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(App_page1.this, App_page2.class);
                ScreenTransitions.startForward(App_page1.this, intent);
            }
        });
    }

    private void openHome() {
        Intent intent = new Intent(this, App_page3.class);
        intent.putExtra(
                AppContracts.EXTRA_LEVEL,
                AppSettings.getDiabetesType(this).getCode());
        ScreenTransitions.startTopLevel(this, intent);
        finish();
    }
}
