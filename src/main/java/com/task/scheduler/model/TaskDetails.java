package com.task.scheduler.model;

import com.task.scheduler.TaskScheduler.RunTask;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name="task_details")
public class TaskDetails {
    public enum STATES {
        ACTIVE,
        INACTIVE,
        COMPLETED;
    }
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long id;
    @Column(unique = true)
    private String name;
    private String type;
    private String priority;
    private long duration;
    private String time;
    @Temporal(TemporalType.TIMESTAMP)
    private Date startTime;
    private String state;
    private Date dateCreated = new Date();
    private Date dateModified = new Date();

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public void setDateCreated(Date dateCreated) {
        this.dateCreated = dateCreated;
    }

    public void setDateModified(Date dateModified) {
        this.dateModified = dateModified;
    }

    @PreUpdate
    public void setDateModified() {  this.dateModified = new Date(); }

    public Date getDateCreated() {
        return dateCreated;
    }

    public Date getDateModified() {
        return dateModified;
    }

    public TaskDetails(String name, String type, String priority, long duration, String time, String state) {
        this.name = name;
        this.type = type;
        this.priority = priority;
        this.duration = duration;
        this.time = time;
        this.state = state;
    }
    public TaskDetails(){};

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}