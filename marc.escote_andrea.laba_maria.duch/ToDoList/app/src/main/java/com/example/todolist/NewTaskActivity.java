package com.example.todolist;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class NewTaskActivity extends AppCompatActivity {

    public static final String DATE_FORMAT = "dd/MM/yyyy 'at' HH:mm:ss a";
    private EditText mTaskTitle;
    private TextView mDetails;
    private Date mCreationDate;
    private Button mCreateButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_task);

        mCreationDate = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT, Locale.ROOT);
        String date = dateFormat.format(mCreationDate);
        mDetails = (TextView) findViewById(R.id.date_new_task);
        mDetails.setText(date);

        mCreateButton = (Button) findViewById(R.id.button_new_task);
        mCreateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickCreateTask(view);
            }
        });
    }

    public void onClickCreateTask(View view) {
        mTaskTitle = (EditText) findViewById(R.id.input_new_task_title);
        String taskTitle = mTaskTitle.getText().toString();

        if (taskTitle.equals("")) {
            mTaskTitle.setHintTextColor(ResourcesCompat.getColor(getResources(), R.color.red, null));
        }
        else {
            TaskManager taskManager = TaskManager.getInstance();
            taskManager.addTask(new Task(taskTitle, mCreationDate));
            finish();

        }
    }
}