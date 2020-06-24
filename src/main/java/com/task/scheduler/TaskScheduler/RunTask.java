package com.task.scheduler.TaskScheduler;

import com.task.scheduler.model.TaskDetails;
import com.task.scheduler.repository.TaskDetailsRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

public class RunTask implements Runnable {

    public enum TaskPriority {
        HIGH,
        MEDIUM,
        LOW
    }

    private TaskDetailsRepository taskDetailsRepository;
    private long id;
    private String taskName;
    private long duration;
    private TaskPriority taskPriority;

    public void setId(long id) {
        this.id = id;
    }

    public void setTaskDetailsRepository(TaskDetailsRepository taskDetailsRepository) {
        this.taskDetailsRepository = taskDetailsRepository;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public void setTaskPriority(TaskPriority taskPriority) {
        this.taskPriority = taskPriority;
    }

    public String getTaskName() {
        return taskName;
    }

    public long getDuration() {
        return duration;
    }

    public TaskPriority getTaskPriority() {
        return taskPriority;
    }


    @Override
    public void run() {
        Optional<TaskDetails> details = taskDetailsRepository.findById(id);
        if(toRun(details.get())) {
            try {
                if(TaskDetails.STATES.ACTIVE.name().equalsIgnoreCase(details.get().getState())) {
                    System.out.println(taskName + " starting execution: " + taskPriority);
                    Thread.sleep(duration * 1000);
                    System.out.println(taskName + " execution completed: " + taskPriority);
                    if ("A".equalsIgnoreCase(details.get().getType())) {
                        details.get().setState(TaskDetails.STATES.COMPLETED.name());
                        taskDetailsRepository.save(details.get());
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private boolean toRun(TaskDetails taskDetails){
        if(TaskDetails.STATES.ACTIVE.name().equalsIgnoreCase(taskDetails.getState())){
            return true;
        } else if (TaskDetails.STATES.INACTIVE.name().equalsIgnoreCase(taskDetails.getState())){
            return false;
        }
        return false;
    }
}
