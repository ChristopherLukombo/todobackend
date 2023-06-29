package com.carbon.todobackend.service;

import com.carbon.todobackend.domain.dto.CreateTodoRequest;
import com.carbon.todobackend.domain.dto.Todo;
import com.carbon.todobackend.domain.dto.UpdateTodoRequest;
import com.carbon.todobackend.exception.AlreadyExistException;
import com.carbon.todobackend.exception.NotExistingTodoException;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * The Interface TodoService.
 */
public interface TodoService {
	
	/**
	 * Save a todo.
	 *
	 * @param todo the entity to save
	 * @return the persisted entity
	 */
	Todo save(CreateTodoRequest todo) throws AlreadyExistException;
	
	/**
	 * Update a todo.
	 *
	 * @param todo the todo dto
	 * @return the todo dto
	 */
	Todo update(UpdateTodoRequest todo, UUID id) throws AlreadyExistException, NotExistingTodoException;
	
	/**
	 * Get all todos.
	 *
	 * @return the list of entities
	 */
	List<Todo> findAll();
	
	/**
	 * Get by the "id" todo.
	 *
	 * @param id the id
	 * @return the optional
	 */
	Optional<Todo> findById(UUID id);
	
	/**
	 * Delete the "id" todo.
	 *
	 * @param id the id
	 */
	void deleteById(UUID id);

	/**
	 * Delete all todos.
	 */
	void deleteAll();
}
