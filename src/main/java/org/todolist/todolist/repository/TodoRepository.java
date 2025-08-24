package org.todolist.todolist.repository;

import org.springframework.data.domain.Limit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.todolist.todolist.entity.Todo;
import org.todolist.todolist.entity.User;

import java.util.List;

@Repository
public interface TodoRepository extends JpaRepository<Todo, Long> {
    List<Todo> findByUserEmail(String userEmail);

    List<Todo> findByUserAndTitleContainingIgnoreCase(User user, String title);

    List<Todo> findByUserEmailAndCompleted(String userEmail, boolean completed);

    Long countByUserEmail(String userEmail);
}
