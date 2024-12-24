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

    @Query("SELECT t.status, COUNT(t) FROM Task t GROUP BY t.status")
    List<Object[]> countTasksByStatus();

    @Query("SELECT t.priority, COUNT(t) FROM Task t GROUP BY t.priority")
    List<Object[]> countTasksByPriority();

    @Query("SELECT t.category, COUNT(t) FROM Task t GROUP BY t.category")
    List<Object[]> countTasksByCategory();

    @Query("SELECT t.user.username, COUNT(t) FROM Task t GROUP BY t.user.username")
    List<Object[]> countTasksByUser();

    @Query("SELECT t.category, COUNT(t) FROM Task t WHERE t.user.id = :userId GROUP BY t.category")
    List<Object[]> countTasksByCategoryForUser(@Param("userId") Long userId);



    @Query("SELECT t FROM Task t WHERE t.user.id = :userId " +
            "AND (:title IS NULL OR LOWER(t.title) LIKE LOWER(CONCAT('%', :title, '%'))) " +
            "AND (:category IS NULL OR LOWER(t.category) LIKE LOWER(CONCAT('%', :category, '%'))) " +
            "AND (:status IS NULL OR LOWER(t.status) = LOWER(:status)) " +
            "AND (:priority IS NULL OR LOWER(t.priority) = LOWER(:priority)) " +
            "AND (:startDate IS NULL OR :endDate IS NULL OR t.dueDate BETWEEN :startDate AND :endDate)")
    List<Task> findByFilters(
            @Param("userId") Long userId,
            @Param("title") String title,
            @Param("category") String category,
            @Param("status") String status,
            @Param("priority") String priority,
            @Param("startDate") Date startDate,
            @Param("endDate") Date endDate
    );

    @Query("SELECT t FROM Task t " +
            "WHERE (:title IS NULL OR LOWER(t.title) LIKE LOWER(CONCAT('%', :title, '%'))) " +
            "AND (:category IS NULL OR LOWER(t.category) LIKE LOWER(CONCAT('%', :category, '%'))) " +
            "AND (:status IS NULL OR LOWER(t.status) = LOWER(:status)) " +
            "AND (:priority IS NULL OR LOWER(t.priority) = LOWER(:priority)) " +
            "AND (:startDate IS NULL OR :endDate IS NULL OR t.dueDate BETWEEN :startDate AND :endDate)")
    List<Task> findByAdminFilters(
            @Param("title") String title,
            @Param("category") String category,
            @Param("status") String status,
            @Param("priority") String priority,
            @Param("startDate") Date startDate,
            @Param("endDate") Date endDate
    );


}
