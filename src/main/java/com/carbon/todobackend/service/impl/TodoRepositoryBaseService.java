package com.carbon.todobackend.service.impl;

import com.carbon.todobackend.dao.TodoRepository;
import com.carbon.todobackend.domain.dto.CreateTodoRequest;
import com.carbon.todobackend.domain.dto.Todo;
import com.carbon.todobackend.domain.dto.UpdateTodoRequest;
import com.carbon.todobackend.domain.entities.TodoEntity;
import com.carbon.todobackend.exception.AlreadyExistException;
import com.carbon.todobackend.exception.NotExistingTodoException;
import com.carbon.todobackend.service.TodoService;
import com.carbon.todobackend.service.mapper.CreateTodoRequestMapper;
import com.carbon.todobackend.service.mapper.TodoMapper;
import com.carbon.todobackend.service.mapper.UpdateTodoRequestMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing Todo.
 */
@Slf4j
@AllArgsConstructor
@Service
@Transactional
public class TodoRepositoryBaseService implements TodoService {
	
	private final TodoMapper todoMapper;

	private final CreateTodoRequestMapper createTodoRequestMapper;

	private final UpdateTodoRequestMapper updateTodoRequestMapper;

	private final TodoRepository todoRepository;


	/**
	 * Save a todo.
	 *
	 * @param createTodoRequest the entity to save
	 * @return the persisted entity
	 */
	@Override
	public Todo save(CreateTodoRequest createTodoRequest) throws AlreadyExistException {
		log.debug("Request to save todo: {}", createTodoRequest);
		if (existsTitle(createTodoRequest.title())) {
			throw new AlreadyExistException("Title for the same value already exist");
		}
		TodoEntity todoEntity = createTodoRequestMapper.toEntity(createTodoRequest);
		todoEntity = todoRepository.save(todoEntity);
		return todoMapper.toDto(todoEntity);
	}

	/**
	 * Update a updateTodoRequest.
	 *
	 * @param updateTodoRequest the updateTodoRequest dto
	 * @return the updateTodoRequest dto
	 */
	@Override
	public Todo update(UpdateTodoRequest updateTodoRequest, UUID id) throws AlreadyExistException, NotExistingTodoException {
		log.debug("Request to update todo: {}", updateTodoRequest);
		Optional<Todo> todo = findById(id);
		if (todo.isEmpty()) {
			throw new NotExistingTodoException("Todo does not exist");
		}
		if (!isSameTodo(todo, updateTodoRequest) && existsTitle(updateTodoRequest.title())) {
			throw new AlreadyExistException(
					"Updating todo with a title which is already present is forbidden");
		}
		TodoEntity todoEntity = updateTodoRequestMapper.toEntity(updateTodoRequest, id);
		todoEntity = todoRepository.saveAndFlush(todoEntity);
		return todoMapper.toDto(todoEntity);
	}

	private boolean isSameTodo(Optional<Todo> todo, UpdateTodoRequest updateTodoRequest) {
		return todo
				.map(Todo::title)
				.filter(updateTodoRequest.title()::equals)
				.isPresent();
	}

	private boolean existsTitle(String title) {
		return todoRepository.existsByTitle(title);
	}

	/**
	 * Get all todos.
	 *
	 * @return the list of entities
	 */
	@Override
	@Transactional(readOnly = true)
	public List<Todo> findAll() {
		log.debug("Request to find all todos");
		return todoRepository.findAll().stream()
				.map(todoMapper::toDto)
				.collect(Collectors.toCollection(LinkedList::new));
	}

	/**
	 * Get by the "id" todo.
	 *
	 * @param id the id
	 * @return the optional
	 */
	@Override
	@Transactional(readOnly = true)
	public Optional<Todo> findById(UUID id) {
		log.debug("Request to find todo by id: {}", id);
		return todoRepository.findById(id)
				.map(todoMapper::toDto);
	}

	/**
	 * Delete the "id" todo.
	 *
	 * @param id the id
	 */
	@Override
	public void deleteById(UUID id) {
		log.debug("Request to delete todo by id: {}", id);
		todoRepository.deleteById(id);
	}

	/**
	 * Delete all todos.
	 */
	@Override
	public void deleteAll() {
		log.debug("Request to delete all todos");
		todoRepository.deleteAll();
	}
}
