package com.carbon.todobackend.dao;

import com.carbon.todobackend.domain.entities.TodoEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;
test
public interface TodoRepository extends JpaRepository<TodoEntity, UUID> {
    boolean existsByTitle(String title);
}
