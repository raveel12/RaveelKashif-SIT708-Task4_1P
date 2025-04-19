package com.example.taskmanager;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class AddEditTaskActivity extends AppCompatActivity {

    EditText etTitle, etDescription, etDueDate;
    Button btnSave, btnDelete;
    TaskDatabaseHelper db;
    int taskId = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_task);

        etTitle = findViewById(R.id.etTitle);
        etDescription = findViewById(R.id.etDescription);
        etDueDate = findViewById(R.id.etDueDate);
        btnSave = findViewById(R.id.btnSave);
        btnDelete = findViewById(R.id.btnDelete); // NEW DELETE BUTTON

        db = new TaskDatabaseHelper(this);

        btnDelete.setVisibility(Button.GONE);

        Intent intent = getIntent();
        if (intent.hasExtra("task")) {
            Task task = (Task) intent.getSerializableExtra("task");
            etTitle.setText(task.getTitle());
            etDescription.setText(task.getDescription());
            etDueDate.setText(task.getDueDate());
            taskId = task.getId();
            btnDelete.setEnabled(true);
            btnDelete.setVisibility(Button.VISIBLE);
        } else {
            btnDelete.setEnabled(false); // Disable delete on new task
        }

        btnSave.setOnClickListener(v -> {
            String title = etTitle.getText().toString().trim();
            String desc = etDescription.getText().toString().trim();
            String due = etDueDate.getText().toString().trim();

            if (title.isEmpty() || due.isEmpty()) {
                Toast.makeText(this, "Title and Due Date are required", Toast.LENGTH_SHORT).show();
                return;
            }

            Task task = new Task(taskId, title, desc, due);
            if (taskId == -1) {
                db.addTask(task);
                Toast.makeText(this, "Task added", Toast.LENGTH_SHORT).show();
            } else {
                db.updateTask(task);
                Toast.makeText(this, "Task updated", Toast.LENGTH_SHORT).show(); // ✅ Toast after update
            }

            finish();
        });

        btnDelete.setOnClickListener(v -> {
            if (taskId != -1) {
                db.deleteTask(taskId);
                Toast.makeText(this, "Task deleted", Toast.LENGTH_SHORT).show();
                finish();
            }
        });

    }
}
