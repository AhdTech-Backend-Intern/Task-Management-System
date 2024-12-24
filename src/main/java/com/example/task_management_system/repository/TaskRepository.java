package com.example.task_management_system.repository;

import com.example.task_management_system.model.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
    @Query("SELECT t FROM Task t WHERE t.user.id = :userId")
    List<Task> findByUserId(@Param("userId") Long userId);

    List<Task> findByUserIdAndTitleContainingIgnoreCase(Long userId, String title);

    List<Task> findByUserIdAndCategoryContainingIgnoreCase(Long userId, String category);

    List<Task> findByUserIdAndStatus(Long userId, String status);

    List<Task> findByUserIdAndDueDateBetween(Long userId, Date startDate, Date endDate);
    List<Task> findByUserIdAndPriority(Long userId, String priority);

}
