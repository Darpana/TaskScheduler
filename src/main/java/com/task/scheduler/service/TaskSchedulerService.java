package com.task.scheduler.service;

import com.task.scheduler.model.TaskDetails;

import java.util.List;

public interface TaskSchedulerService {

    TaskDetails createTask(TaskDetails taskDetails);

    TaskDetails getTaskDetailsById(long id);

    void changeState(String taskName, String state);

    List<TaskDetails> getAllActiveTasks(String taskName);
}
