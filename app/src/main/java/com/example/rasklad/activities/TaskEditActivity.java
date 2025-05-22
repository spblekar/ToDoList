package com.example.rasklad.activities;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.example.rasklad.R;
import com.example.rasklad.database.repository.CategoryRepository;
import com.example.rasklad.database.repository.TaskRepository;
import com.example.rasklad.models.Category;
import com.example.rasklad.models.Task;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class TaskEditActivity extends AppCompatActivity {
    private TextView textViewEditTitle;
    private EditText editTextTitle, editTextDescription;
    private Spinner spinnerCategory;
    private RadioGroup radioGroupPriority;
    private RadioButton radioLow, radioMedium, radioHigh;
    private Button buttonSelectTime, buttonSaveTask, buttonCancelTask, buttonSelectDate;
    private TaskRepository taskRepository;
    private CategoryRepository categoryRepository;
    private Task task;
    private Calendar dueCalendar = Calendar.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_edit);

        textViewEditTitle   = findViewById(R.id.textViewEditTitle);
        editTextTitle       = findViewById(R.id.editTextTitle);
        editTextDescription = findViewById(R.id.editTextDescription);
        spinnerCategory     = findViewById(R.id.spinnerCategory);
        radioGroupPriority  = findViewById(R.id.radioGroupPriority);
        radioLow            = findViewById(R.id.radioLow);
        radioMedium         = findViewById(R.id.radioMedium);
        radioHigh           = findViewById(R.id.radioHigh);
        buttonSelectDate  = findViewById(R.id.buttonSelectDate);
        buttonSelectTime    = findViewById(R.id.buttonSelectTime);
        buttonSaveTask      = findViewById(R.id.buttonSaveTask);
        buttonCancelTask    = findViewById(R.id.buttonCancelTask);

        taskRepository     = new TaskRepository(this);
        categoryRepository = new CategoryRepository(this);

        List<Category> categories = categoryRepository.getAllCategories();
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        for (Category c : categories) spinnerAdapter.add(c.getName());
        spinnerCategory.setAdapter(spinnerAdapter);

        int taskId = getIntent().getIntExtra("taskId", -1);
        long dateMillis = getIntent().getLongExtra("date", -1);
        if (taskId != -1) {
            task = taskRepository.getTask(taskId);
            textViewEditTitle.setText("Редактировать задачу");
        } else {
            task = new Task();
            textViewEditTitle.setText("Новая задача");
        }

        if (task.getId()>0) {
            dueCalendar.setTimeInMillis(task.getDueDate());
        } else if (dateMillis != -1) {
            dueCalendar.setTimeInMillis(dateMillis);
        }
        updateDateButtonText();
        editTextTitle.setText(task.getTitle());
        editTextDescription.setText(task.getDescription());

        switch (task.getPriority()) {
            case 0: radioLow.setChecked(true); break;
            case 1: radioMedium.setChecked(true); break;
            case 2: radioHigh.setChecked(true); break;
        }

        for (int i = 0; i < categories.size(); i++) {
            if (categories.get(i).getId() == task.getCategoryId()) {
                spinnerCategory.setSelection(i);
                break;
            }
        }

        updateTimeButtonText();
        buttonSelectTime.setOnClickListener(v -> {
            int hour = dueCalendar.get(Calendar.HOUR_OF_DAY);
            int minute = dueCalendar.get(Calendar.MINUTE);
            new TimePickerDialog(this, (view, h, m) -> {
                dueCalendar.set(Calendar.HOUR_OF_DAY, h);
                dueCalendar.set(Calendar.MINUTE, m);
                updateTimeButtonText();
            }, hour, minute, true).show();
        });

        buttonSelectDate.setOnClickListener(v -> {
            int year  = dueCalendar.get(Calendar.YEAR);
            int month = dueCalendar.get(Calendar.MONTH);
            int day   = dueCalendar.get(Calendar.DAY_OF_MONTH);
            new DatePickerDialog(this, (view, y, mo, d) -> {
                dueCalendar.set(Calendar.YEAR, y);
                dueCalendar.set(Calendar.MONTH, mo);
                dueCalendar.set(Calendar.DAY_OF_MONTH, d);
                updateDateButtonText();
            }, year, month, day).show();
        });


        buttonSaveTask.setOnClickListener(v -> {
            String title = editTextTitle.getText().toString().trim();
            if (title.isEmpty()) {
                editTextTitle.setError("Название задачи не может быть пустым");
                return;
            }

            task.setTitle(editTextTitle.getText().toString().trim());
            task.setDescription(editTextDescription.getText().toString().trim());
            task.setDueDate(dueCalendar.getTimeInMillis());

            int pos = spinnerCategory.getSelectedItemPosition();
            if (pos >= 0) task.setCategoryId(categories.get(pos).getId());

            int pid = radioGroupPriority.getCheckedRadioButtonId();
            task.setPriority(pid == R.id.radioLow ? 0 : pid == R.id.radioMedium ? 1 : 2);

            if (task.getId() > 0) taskRepository.updateTask(task);
            else                taskRepository.addTask(task);
            finish();
        });

        buttonCancelTask.setOnClickListener(v -> finish());
    }

    private void updateTimeButtonText() {
        int h = dueCalendar.get(Calendar.HOUR_OF_DAY);
        int m = dueCalendar.get(Calendar.MINUTE);
        buttonSelectTime.setText(String.format(Locale.getDefault(), "%02d:%02d", h, m));
    }

    private void updateDateButtonText() {
        int y = dueCalendar.get(Calendar.YEAR);
        int m = dueCalendar.get(Calendar.MONTH) + 1;
        int d = dueCalendar.get(Calendar.DAY_OF_MONTH);
        buttonSelectDate.setText(String.format(Locale.getDefault(), "%02d.%02d.%04d", d, m, y));
    }
}