package org.todolist.todolist.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.todolist.todolist.entity.User;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User , Long> {

    List<User> findUserByName(String name);

    Boolean existsUserByEmail(String email);

    @Query("SELECT u from User u where u.name like %?1%")
    List<User> findByNameContaining(String name);

    Optional<User> findByEmail(String email);
}
