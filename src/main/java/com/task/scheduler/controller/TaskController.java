package com.task.scheduler.controller;

import com.task.scheduler.model.TaskDetails;
import com.task.scheduler.service.TaskSchedulerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("task")
public class TaskController {

    @Autowired
    TaskSchedulerService taskSchedulerService;

    @PostMapping("/add")
    public TaskDetails createTask(@RequestBody TaskDetails taskDetails){
        return taskSchedulerService.createTask(taskDetails);
    }

    @GetMapping("/change/{name}/{state}")
    public void changeTaskStateToInactive(@PathVariable("name") String taskName, @PathVariable("state") String state){
        taskSchedulerService.changeState(taskName, state);
    }

    @GetMapping("/get/active/tasks/{name}")
    public List<TaskDetails> getActiveTasks(@PathVariable("name") String taskName){
        return taskSchedulerService.getAllActiveTasks(taskName);
    }

    @GetMapping("/get/execution/tasks/{T+5}/{T+10}")
    public List<TaskDetails> getAllTasksBetweenTime(@PathVariable("startTime") String startTime, @PathVariable("endTime") String endTime){
        return taskSchedulerService.getAllTasksBetween(startTime, endTime);
    }

}
