package com.task.scheduler.repository;

import com.task.scheduler.model.TaskDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.websocket.server.PathParam;
import java.util.Date;
import java.util.List;

@Repository
public interface TaskDetailsRepository extends JpaRepository<TaskDetails, Long> {

    @Transactional
    @Modifying
    @Query("UPDATE TaskDetails td SET td.state = :state WHERE LOWER(td.name) = LOWER(:name)")
    void changeState(@Param("name") String name, @Param("state") String state);

    @Query("SELECT td FROM TaskDetails td WHERE LOWER(td.name) = LOWER(:name)")
    TaskDetails getByName(@Param("name") String name);

    @Query("SELECT td FROM TaskDetails td WHERE td.startTime > :startTime AND td.state = :state")
    List<TaskDetails> getAllActiveTasksBeforeExecutionOfTask(@Param("startTime") Date startTime, @Param("state") String state);

    @Query("SELECT td FROM TaskDetails td WHERE td.state = :state AND td.startTime between :startTime AND :endTime")
    List<TaskDetails> getAllTasksBetweenTime(@Param("startTime") Date startTime, @Param("endTime") Date endTime, @Param("state") String state);
}
