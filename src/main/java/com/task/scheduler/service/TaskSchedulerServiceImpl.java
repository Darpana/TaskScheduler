package com.task.scheduler.service;

import com.task.scheduler.TaskScheduler.RunTask;
import com.task.scheduler.TaskScheduler.TaskSchedulerManager;
import com.task.scheduler.model.TaskDetails;
import com.task.scheduler.repository.TaskDetailsRepository;
import com.task.scheduler.util.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.TimeUnit;

@Service
public class TaskSchedulerServiceImpl implements TaskSchedulerService {

    @Autowired
    TaskSchedulerManager taskSchedulerManager;

    @Autowired
//    TaskDetailsRepository taskDetailsRepository;
    private final TaskDetailsRepository taskDetailsRepository;

    public TaskSchedulerServiceImpl(TaskDetailsRepository repository) {
        this.taskDetailsRepository = repository;
    }

    Map<String, String> daysMap = Collections.unmodifiableMap(
            new HashMap() {{put("MON","Monday");
                    put("TUE","Tuesday");
                    put("WED","Wednesday");
                    put("THUR","Thursday");
                    put("FRI","Friday");
                    put("SAT","Saturday");
                    put("SUN","Sunday");
            }});

    @Override
    public TaskDetails createTask(TaskDetails taskDetails){
        if("A".equalsIgnoreCase(taskDetails.getType())){
            createTypeATask(taskDetails);
        } else if("B".equalsIgnoreCase(taskDetails.getType())){
            createTypeBTask(taskDetails);
        }
        return null;
    }

    private TaskDetails createTypeATask(TaskDetails taskDetails){
        TaskDetails latestTaskDetails = taskDetailsRepository.getByName(taskDetails.getName());
        if(latestTaskDetails!=null){
            taskDetails.setId(latestTaskDetails.getId());
        }
        taskDetails.setState(TaskDetails.STATES.ACTIVE.name());
        String time = taskDetails.getTime();
        int min = Integer.valueOf(time.trim().substring(time.lastIndexOf("+")+1));
        Date startTime = DateUtil.addMinutes(new Date(), min);
        taskDetails.setStartTime(startTime);
        taskDetails = taskDetailsRepository.save(taskDetails);
        if(taskDetails.getId()!=null) {
            taskSchedulerManager.scheduleForSingleTimeRun(taskDetails.getId(), taskDetails.getName(), RunTask.TaskPriority.valueOf(taskDetails.getPriority()), taskDetails.getDuration(), min);
        }
        return taskDetails;
    }

    private TaskDetails createTypeBTask(TaskDetails taskDetails){
        taskDetails.setState(TaskDetails.STATES.ACTIVE.name());
        Date startTime = DateUtil.addMinutes(new Date(), 0);
        taskDetails.setStartTime(startTime);
        taskDetails = taskDetailsRepository.save(taskDetails);
        if(taskDetails.getId()!=null) {
            taskSchedulerManager.scheduleForRecurringRun(taskDetails.getId(), taskDetails.getName(), RunTask.TaskPriority.valueOf(taskDetails.getPriority()), taskDetails.getDuration(), 0, 5, TimeUnit.MINUTES);
        }
        return taskDetails;
    }

    private long convertCronExprToTime(String cronExp){
        char[] exp = cronExp.trim().toCharArray();
        int currentDay = DateUtil.getDayOfMonth(new Date());
        Calendar cal = Calendar.getInstance();
        return 0;
    }

    @Override
    public TaskDetails getTaskDetailsById(long id){
        Optional<TaskDetails> details = taskDetailsRepository.findById(id);
        return details.get();
    }

    @Override
    public void changeState(String taskName, String state) {
        taskDetailsRepository.changeState(taskName, state);
    }

    @Override
    public List<TaskDetails> getAllActiveTasks(String taskName) {
        TaskDetails taskDetails = taskDetailsRepository.getByName(taskName);
        if(taskDetails!=null) {
            return taskDetailsRepository.getAllActiveTasksBeforeExecutionOfTask(taskDetails.getStartTime(), TaskDetails.STATES.ACTIVE.name());
        }
        return null;
    }

    @Override
    public List<TaskDetails> getAllTasksBetween(String startTime, String endTime) {
        int start = Integer.valueOf(startTime.trim().substring(startTime.lastIndexOf("+")+1));
        Date startDate = DateUtil.addMinutes(new Date(), start);
        int end = Integer.valueOf(endTime.trim().substring(endTime.lastIndexOf("+")+1));
        Date endDate = DateUtil.addMinutes(new Date(), end);
        taskDetailsRepository.getAllTasksBetweenTime(startDate, endDate, TaskDetails.STATES.ACTIVE.name());
        return null;
    }
}
