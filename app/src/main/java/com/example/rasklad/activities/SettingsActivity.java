package com.example.rasklad.activities;

import android.os.Bundle;
import android.widget.ImageButton;
import androidx.appcompat.app.AppCompatActivity;
import com.example.rasklad.R;

public class SettingsActivity extends AppCompatActivity {

    private ImageButton buttonSettingsBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        buttonSettingsBack = findViewById(R.id.buttonSettingsBack);
        buttonSettingsBack.setOnClickListener(v -> {
            getOnBackPressedDispatcher().onBackPressed();
        });
    }
}