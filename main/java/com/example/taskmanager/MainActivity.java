package com.example.taskmanager;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    TaskAdapter adapter;
    ArrayList<Task> tasks;
    TaskDatabaseHelper db;
    Button btnAddTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        db = new TaskDatabaseHelper(this);
        recyclerView = findViewById(R.id.recyclerViewTasks);
        btnAddTask = findViewById(R.id.btnAddTask);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        loadTasks();

        btnAddTask.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, AddEditTaskActivity.class);
            startActivity(intent);
        });

    }

    private void loadTasks() {
        tasks = db.getAllTasks();
        adapter = new TaskAdapter(this, tasks, new TaskAdapter.OnItemClickListener() {
            @Override
            public void onEdit(Task task) {
                Intent intent = new Intent(MainActivity.this, AddEditTaskActivity.class);
                intent.putExtra("task", task);
                startActivity(intent);
            }

            @Override
            public void onDelete(Task task) {
                db.deleteTask(task.getId());
                loadTasks();
            }
        });
        recyclerView.setAdapter(adapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadTasks();
    }
}
