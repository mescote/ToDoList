package com.example.todolist;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private static final String FILE_NAME = "data.txt";
    public static final String DATE_FORMAT = "dd/MM/yyyy 'at' HH:mm:ss a";

    private ImageButton mAddTaskButton;
    private RecyclerView mRecyclerView;
    private TaskAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        List<Task> tasks = new ArrayList<>();

        try (FileInputStream file = openFileInput(FILE_NAME)) {
            BufferedReader reader = new BufferedReader(new InputStreamReader(file));
            String data;
            while ((data = reader.readLine()) != null) {
                tasks.add(Task.fromString(data));
            }
        } catch (IOException e) {}
        finally {
            TaskManager taskManager = TaskManager.getInstance();
            taskManager.setTasks(tasks);
        }

        mAddTaskButton = (ImageButton) findViewById(R.id.add_task);
        mAddTaskButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, NewTaskActivity.class);
                startActivity(intent);
            }
        });

        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view_task);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(mRecyclerView);
    }

    @Override
    protected void onStart() {
        super.onStart();

        TaskManager taskManager = TaskManager.getInstance();
        List<Task> tasks = taskManager.getTasks();

        mAdapter = new TaskAdapter(tasks);
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    protected void onStop() {
        super.onStop();

        String data = getData();

        try (FileOutputStream file = openFileOutput(FILE_NAME, MODE_PRIVATE)) {
            file.write(data.getBytes());
        } catch (IOException e) { }
    }

    private String getData() {
        TaskManager taskManager = TaskManager.getInstance();
        List<Task> tasks = taskManager.getTasks();

        StringBuilder data = new StringBuilder();
        for (Task task : tasks) {
            data.append(task.toString()).append("\n");
        }
        return data.toString();
    }

    private class TaskAdapter extends RecyclerView.Adapter<TaskHolder> {

        private List<Task> mTasks;
        public TaskAdapter(List<Task> tasks) {
            mTasks = tasks;
        }

        @Override
        public TaskHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getApplicationContext());
            return new TaskHolder(layoutInflater, parent);
        }

        @Override
        public void onBindViewHolder(TaskHolder holder, int position) {
            Task task = mTasks.get(position);
            holder.bind(task);
        }

        @Override
        public int getItemCount() {
            return  mTasks.size();
        }
    }

    private class TaskHolder extends RecyclerView.ViewHolder {
        private Task mTask;
        private TextView mTitleTextView;
        private TextView mDateTextView;
        private CheckBox mCompleted;

        public TaskHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.task_box, parent, false));
            mTitleTextView = (TextView) itemView.findViewById(R.id.title_list_task);
            mDateTextView = (TextView) itemView.findViewById(R.id.date_list_task);
            mCompleted = (CheckBox) itemView.findViewById(R.id.task_box);
        }

        public void bind(Task task) {
            mTask = task;
            mTitleTextView.setText(mTask.getTitle());
            SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT, Locale.ROOT);
            String date = dateFormat.format(mTask.getDate());
            mDateTextView.setText(date);
        }
    }

    ItemTouchHelper.SimpleCallback itemTouchHelperCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
            removeTask(viewHolder.getAdapterPosition());
        }
    };

    private void removeTask(int position) {
        TaskManager.getInstance().removeTask(position);
        mAdapter.notifyItemRemoved(position);
    }
}