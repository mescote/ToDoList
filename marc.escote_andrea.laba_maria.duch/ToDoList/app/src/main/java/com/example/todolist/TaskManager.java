package com.example.todolist;

import java.util.ArrayList;
import java.util.List;

public class TaskManager {

    private static TaskManager mTaskManager;
    private List<Task> mTasks;

    public static TaskManager getInstance() {
        if (mTaskManager == null) {
            mTaskManager = new TaskManager();
        }
        return mTaskManager;
    }

    private TaskManager() {
        mTasks = new ArrayList<>();
    }

    public List<Task> getTasks() {
        return mTasks;
    }
    public void setTasks(List<Task> tasks) {
        this.mTasks = tasks;
    }
    public void addTask(Task task) {
        mTasks.add(task);
    }
    public void removeTask(int position) {
        mTasks.remove(position);
    }
}
