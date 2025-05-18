package com.example.rasklad.activities;

import android.os.Bundle;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Switch;

import androidx.appcompat.app.AppCompatActivity;

import com.example.rasklad.R;
import com.example.rasklad.constants.AppConstants;
import com.example.rasklad.utils.PreferenceHelper;

public class SettingsActivity extends AppCompatActivity {
    private Switch switchNotifications;
    private RadioGroup radioGroupTheme, radioGroupSort;
    private RadioButton radioLight, radioDark, radioDate, radioPriority;
    private Button buttonSaveSettings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        switchNotifications = findViewById(R.id.switchNotifications);
        radioGroupTheme = findViewById(R.id.radioGroupTheme);
        radioGroupSort = findViewById(R.id.radioGroupSort);
        radioLight = findViewById(R.id.radioLight);
        radioDark = findViewById(R.id.radioDark);
        radioDate = findViewById(R.id.radioDate);
        radioPriority = findViewById(R.id.radioPriority);
        buttonSaveSettings = findViewById(R.id.buttonSaveSettings);

        // Загрузка сохранённых настроек
        switchNotifications.setChecked(PreferenceHelper.isNotificationsEnabled(this));
        int theme = PreferenceHelper.getTheme(this);
        if (theme == AppConstants.THEME_LIGHT) radioLight.setChecked(true);
        else radioDark.setChecked(true);
        int sortOrder = PreferenceHelper.getSortOrder(this);
        if (sortOrder == AppConstants.SORT_BY_DATE) radioDate.setChecked(true);
        else radioPriority.setChecked(true);

        buttonSaveSettings.setOnClickListener(v -> {
            PreferenceHelper.setNotificationsEnabled(this, switchNotifications.isChecked());
            int selectedTheme = radioLight.isChecked() ? AppConstants.THEME_LIGHT : AppConstants.THEME_DARK;
            PreferenceHelper.setTheme(this, selectedTheme);
            int selectedSort = radioDate.isChecked() ? AppConstants.SORT_BY_DATE : AppConstants.SORT_BY_PRIORITY;
            PreferenceHelper.setSortOrder(this, selectedSort);
            finish();
        });
    }
}
