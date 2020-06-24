package com.task.scheduler.TaskScheduler;

import com.task.scheduler.repository.TaskDetailsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Comparator;
import java.util.concurrent.*;

import static java.util.concurrent.TimeUnit.MINUTES;
import static java.util.concurrent.TimeUnit.SECONDS;

@Component
public class TaskSchedulerManager {

    @Autowired
    TaskDetailsRepository taskDetailsRepository;

    private final int maxUsedThreads = 2;

    private final ScheduledExecutorService scheduledExecutorService =
            Executors.newSingleThreadScheduledExecutor();
    private PriorityBlockingQueue priorityBlockingQueue = new PriorityBlockingQueue(30, Comparator.comparing(RunTask::getTaskPriority));
    private final ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(
            maxUsedThreads,
            maxUsedThreads,
            50,
            MINUTES,
            priorityBlockingQueue);

    public void scheduleForSingleTimeRun(long id, String taskName, RunTask.TaskPriority priority, long duration, long period) {
        RunTask singleRun = new RunTask();
        singleRun.setDuration(duration);
        singleRun.setId(id);
        singleRun.setTaskName(taskName);
        singleRun.setTaskPriority(priority);
        singleRun.setTaskDetailsRepository(taskDetailsRepository);
        scheduleJob(singleRun);
        scheduledExecutorService.schedule(() -> {
            try {
                threadPoolExecutor.execute((Runnable) priorityBlockingQueue.take());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }, period, MINUTES);
    }

    public void scheduleForRecurringRun(long id, String taskName, RunTask.TaskPriority priority, long duration, long delay, long period, TimeUnit unit) {
        RunTask recurringRun = new RunTask();
        recurringRun.setDuration(duration);
        recurringRun.setId(id);
        recurringRun.setTaskName(taskName);
        recurringRun.setTaskPriority(priority);
        recurringRun.setTaskDetailsRepository(taskDetailsRepository);
//        priorityBlockingQueue.add(recurringRun);
//        scheduledExecutorService.scheduleAtFixedRate(() -> {
//            try {
//                threadPoolExecutor.execute((Runnable) priorityBlockingQueue.take());
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//        }, delay, period, unit);
        scheduledExecutorService.scheduleAtFixedRate(recurringRun, delay, period, unit);
    }

    public void scheduleJob(RunTask task) {
        priorityBlockingQueue.add(task);
    }
}
