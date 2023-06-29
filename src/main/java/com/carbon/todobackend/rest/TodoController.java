package com.carbon.todobackend.rest;

import com.carbon.todobackend.domain.dto.CreateTodoRequest;
import com.carbon.todobackend.domain.dto.Todo;
import com.carbon.todobackend.domain.dto.TodoView;
import com.carbon.todobackend.domain.dto.UpdateTodoRequest;
import com.carbon.todobackend.exception.AlreadyExistException;
import com.carbon.todobackend.exception.NotExistingTodoException;
import com.carbon.todobackend.service.TodoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * REST controller for managing Todo.
 * 
 * @author christopher
 *
 */
@Slf4j
@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/api/todos")
public class TodoController {

	private final TodoService todoService;

	public TodoController(TodoService todoService) {
		this.todoService = todoService;
	}

	/**
	 * POST /todos : Create a new todo.
	 *
	 * @param todo the todo dto
	 * @return the ResponseEntity with status 201 (Created) and with body the new
	 *         todoDto, or with status 400 (Bad Request) if the todo has
	 *         already an ID
	 */
	@PostMapping
	@CrossOrigin(methods = RequestMethod.POST)
	public ResponseEntity<Todo> createTodo(@RequestBody CreateTodoRequest todo) throws AlreadyExistException {
		log.debug("REST Request to create todo: {}", todo);
		final Todo createdUser = todoService.save(todo);
		final URI location = ServletUriComponentsBuilder
	              .fromCurrentRequest()
	              .path("/{id}")
	              .buildAndExpand(createdUser.id())
	              .toUri();

		return ResponseEntity.created(location)
				.body(createdUser);
	}

	/**
	 * PUT /todos : Update a todo.
	 *
	 * @param todo the todo dto
	 * @return the ResponseEntity with status 200 (OK) and with body the
	 *         todoDto, or with status 400 (Bad Request) if the todo has not
	 *         already an ID
	 */
	@PutMapping("/{id}")
	@CrossOrigin(methods = RequestMethod.PUT)
	public ResponseEntity<Todo> updateTodo(@RequestBody UpdateTodoRequest todo, @PathVariable UUID id) throws AlreadyExistException, NotExistingTodoException {
		log.debug("REST Request to update todo: {}", todo);
		final Todo updatedUser = todoService.update(todo, id);
		return ResponseEntity.ok().body(updatedUser);
	}

	/**
	 * GET /todos : Get all the todos.
	 *
	 * @return the ResponseEntity with status 200 (Ok) and the list of todos in body
	 *
	 */
	@GetMapping
	@CrossOrigin(methods = RequestMethod.GET)
	public ResponseEntity<List<TodoView>> getAllTodos() {
		log.debug("REST Request to find all todos");
		final List<TodoView> todos = todoService.findAll().stream().map(this::toTodoView).toList();
		return ResponseEntity.ok().body(todos);
	}

	/**
	 * GET /todos/:id : Get the "id" todo.
	 *
	 * @param id the id
	 * @return the ResponseEntity with status 200 (Ok)
	 */
	@GetMapping("/{id}")
	@CrossOrigin(methods = RequestMethod.GET)
	public ResponseEntity<TodoView> getTodoById(@PathVariable UUID id) {
		log.debug("REST Request to get todo by id: {}", id);
		Optional<TodoView> todoView = todoService.findById(id).map(this::toTodoView);
		return ResponseEntity.of(todoView);
	}

	private TodoView toTodoView(Todo todo) {
		final URI location = ServletUriComponentsBuilder
				.fromCurrentRequest()
				.build()
				.toUri();

		return new TodoView(
				todo.id(),
				todo.title(),
				todo.completed(),
				todo.order(),
				location.toString());
	}

	/**
	 * DELETE /todos/:id : Delete the "id" todo.
	 *
	 * @param id the id of the todoDto to delete
	 * @return the ResponseEntity with status 204 (OK)
	 */
	@DeleteMapping("/{id}")
	@CrossOrigin(methods = RequestMethod.DELETE)
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void deleteById(@PathVariable UUID id) {
		log.debug("REST Request to delete todo by id: {}", id);
		todoService.deleteById(id);
	}

	/**
	 * DELETE /todos : Delete the todos.
	 *
	 * @return the ResponseEntity with status 204 (OK)
	 */
	@DeleteMapping
	@CrossOrigin(methods = RequestMethod.DELETE)
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void deleteAll() {
		log.debug("REST Request to delete all todos");
		todoService.deleteAll();
	}
}
