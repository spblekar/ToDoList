package com.example.rasklad.activities;

import android.app.TimePickerDialog;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.CheckBox;
import androidx.appcompat.app.AppCompatActivity;
import com.example.rasklad.R;
import com.example.rasklad.database.repository.CategoryRepository;
import com.example.rasklad.database.repository.TaskRepository;
import com.example.rasklad.models.Category;
import com.example.rasklad.models.Task;

import java.util.Calendar;
import java.util.List;

public class TaskEditActivity extends AppCompatActivity {
    private EditText editTextTitle, editTextDescription;
    private DatePicker datePicker;
    private Spinner spinnerCategory;
    private RadioGroup radioGroupPriority;
    private RadioButton radioLow, radioMedium, radioHigh;
    private CheckBox checkBoxCompleted;
    private Button buttonSaveTask;

    private TaskRepository taskRepository;
    private CategoryRepository categoryRepository;
    private Task task;
    private long date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_edit);
        editTextTitle = findViewById(R.id.editTextTitle);
        editTextDescription = findViewById(R.id.editTextDescription);
        datePicker = findViewById(R.id.datePicker);
        spinnerCategory = findViewById(R.id.spinnerCategory);
        radioGroupPriority = findViewById(R.id.radioGroupPriority);
        radioLow = findViewById(R.id.radioLow);
        radioMedium = findViewById(R.id.radioMedium);
        radioHigh = findViewById(R.id.radioHigh);
        checkBoxCompleted = findViewById(R.id.checkBoxCompleted);
        buttonSaveTask = findViewById(R.id.buttonSaveTask);

        taskRepository = new TaskRepository(this);
        categoryRepository = new CategoryRepository(this);

        int taskId = getIntent().getIntExtra("taskId", -1);
        date = getIntent().getLongExtra("date", -1);

        List<Category> categories = categoryRepository.getAllCategories();
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item);
        for (Category cat : categories) {
            adapter.add(cat.getName());
        }
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCategory.setAdapter(adapter);

        if (taskId != -1) {
            task = taskRepository.getTask(taskId);
            if (task != null) {
                editTextTitle.setText(task.getTitle());
                editTextDescription.setText(task.getDescription());
                Calendar cal = Calendar.getInstance();
                cal.setTimeInMillis(task.getDueDate());
                datePicker.updateDate(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH));

                for (int i = 0; i < categories.size(); i++) {
                    if (categories.get(i).getId() == task.getCategoryId()) {
                        spinnerCategory.setSelection(i);
                        break;
                    }
                }
                int priority = task.getPriority();
                if (priority == 0) radioLow.setChecked(true);
                else if (priority == 1) radioMedium.setChecked(true);
                else if (priority == 2) radioHigh.setChecked(true);
                checkBoxCompleted.setChecked(task.isCompleted());
            }
        } else {
            task = new Task();
            if (date != -1) {
                Calendar cal = Calendar.getInstance();
                cal.setTimeInMillis(date);
                datePicker.updateDate(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH));
            }
        }

        Button buttonSelectTime = findViewById(R.id.buttonSelectTime);
        final Calendar timeCalendar = Calendar.getInstance();

        buttonSelectTime.setOnClickListener(v -> {
            int hour = timeCalendar.get(Calendar.HOUR_OF_DAY);
            int minute = timeCalendar.get(Calendar.MINUTE);
            new TimePickerDialog(this, (view, selectedHour, selectedMinute) -> {
                timeCalendar.set(Calendar.HOUR_OF_DAY, selectedHour);
                timeCalendar.set(Calendar.MINUTE, selectedMinute);
            }, hour, minute, true).show();
        });


        buttonSaveTask.setOnClickListener(v -> {
            task.setTitle(editTextTitle.getText().toString());
            task.setDescription(editTextDescription.getText().toString());
            Calendar cal = Calendar.getInstance();
            cal.set(datePicker.getYear(), datePicker.getMonth(), datePicker.getDayOfMonth(), 0, 0, 0);
            task.setDueDate(cal.getTimeInMillis());
            int categoryIndex = spinnerCategory.getSelectedItemPosition();
            if (categoryIndex >= 0) {
                task.setCategoryId(categories.get(categoryIndex).getId());
            }
            int priority = 0;
            int checkedId = radioGroupPriority.getCheckedRadioButtonId();
            if (checkedId == R.id.radioLow) priority = 0;
            else if (checkedId == R.id.radioMedium) priority = 1;
            else if (checkedId == R.id.radioHigh) priority = 2;
            task.setPriority(priority);
            task.setCompleted(checkBoxCompleted.isChecked());

            cal.set(Calendar.HOUR_OF_DAY, timeCalendar.get(Calendar.HOUR_OF_DAY));
            cal.set(Calendar.MINUTE, timeCalendar.get(Calendar.MINUTE));
            cal.set(Calendar.SECOND, 0);
            cal.set(Calendar.MILLISECOND, 0);
            task.setDueDate(cal.getTimeInMillis());


            if (task.getId() > 0) {
                taskRepository.updateTask(task);
            } else {
                taskRepository.addTask(task);
            }
            finish();
        });

        Button buttonCancel = findViewById(R.id.buttonCancelTask);
        buttonCancel.setOnClickListener(v -> finish());
    }
}