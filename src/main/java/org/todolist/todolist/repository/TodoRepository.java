package org.todolist.todolist.repository;

import org.springframework.data.domain.Limit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.todolist.todolist.entity.Todo;
import org.todolist.todolist.entity.User;

import java.util.List;

@Repository
public interface TodoRepository extends JpaRepository<Todo, Long> {
    List<Todo> findByUserEmailOrderByCreatedAtDesc(String userEmail);

    @Query(
            "SELECT t FROM Todo t WHERE t.user = :user AND " +
                    "(LOWER(t.title) LIKE LOWER(CONCAT('%', :term, '%')) OR " +
                    "LOWER(t.description) LIKE LOWER(CONCAT('%', :term, '%')) OR " +
                    "CAST(t.createdAt AS string) LIKE CONCAT('%', :term, '%'))"
    )
    List<Todo> findByUserAndTitleContainingIgnoreCase(User user, String term);

    List<Todo> findByUserEmailAndCompleted(String userEmail, boolean completed);

    Long countByUserEmail(String userEmail);
}
