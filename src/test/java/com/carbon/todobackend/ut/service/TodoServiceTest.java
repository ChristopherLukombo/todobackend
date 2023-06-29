package com.carbon.todobackend.ut.service;

import com.carbon.todobackend.dao.TodoRepository;
import com.carbon.todobackend.domain.dto.CreateTodoRequest;
import com.carbon.todobackend.domain.dto.Todo;
import com.carbon.todobackend.domain.dto.UpdateTodoRequest;
import com.carbon.todobackend.domain.entities.TodoEntity;
import com.carbon.todobackend.exception.AlreadyExistException;
import com.carbon.todobackend.exception.NotExistingTodoException;
import com.carbon.todobackend.service.impl.TodoRepositoryBaseService;
import com.carbon.todobackend.service.mapper.CreateTodoRequestMapper;
import com.carbon.todobackend.service.mapper.TodoMapper;
import com.carbon.todobackend.service.mapper.UpdateTodoRequestMapper;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.RollbackException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TodoServiceTest {

	@Mock
	private TodoMapper todoMapper;

	@Mock
	private CreateTodoRequestMapper createTodoRequestMapper;

	@Mock
	private UpdateTodoRequestMapper updateTodoRequestMapper;

	@Mock
	private TodoRepository todoRepository;

	@InjectMocks
	private TodoRepositoryBaseService todoService;
	
	@Captor
	private ArgumentCaptor<?> captor;

	@Nested
	@DisplayName("When Create Todo")
	class WhenCreateTodo {
		@Test
		void should_save_todo() throws AlreadyExistException {
			TodoEntity todoEntity = new TodoEntity();
			todoEntity.setTitle("coucou");
			var createTodoRequest = new CreateTodoRequest("coucou");
			var todo = new Todo(UUID.randomUUID(), "coucou", false, 1);

			when(todoRepository.existsByTitle(anyString())).thenReturn(false);
			when(createTodoRequestMapper.toEntity(any(CreateTodoRequest.class))).thenReturn(todoEntity);
			when(todoRepository.save(any(TodoEntity.class))).thenReturn(todoEntity);
			when(todoMapper.toDto(any(TodoEntity.class))).thenReturn(todo);

			assertThat(todoService.save(createTodoRequest)).isEqualTo(todo);
			verify(createTodoRequestMapper).toEntity(createTodoRequest);
			verify(todoRepository).save(todoEntity);
			verify(todoMapper).toDto(todoEntity);
			verify(todoRepository).existsByTitle("coucou");
			verifyNoMoreInteractions(todoRepository);
			verifyNoMoreInteractions(todoMapper);
		}

		@Test
		void should_reject_todo_when_title_already_exist_during_creation() {
			final TodoEntity todoEntity = new TodoEntity();
			todoEntity.setId(UUID.randomUUID());
			todoEntity.setTitle("coucou");
			var createTodoRequest = new CreateTodoRequest("coucou");

			when(todoRepository.existsByTitle(anyString())).thenReturn(true);

			assertThrows(
					AlreadyExistException.class,
					() -> todoService.save(createTodoRequest));
			verify(todoRepository).existsByTitle("coucou");
			verifyNoInteractions(todoMapper);
			verifyNoMoreInteractions(todoRepository);
		}

		@Test
		void should_throw_when_trying_to_save_todo() {
			final TodoEntity todoEntity = new TodoEntity();
			todoEntity.setId(UUID.randomUUID());
			todoEntity.setTitle("coucou");
			var createTodoRequest = new CreateTodoRequest("coucou");

			when(todoRepository.existsByTitle(anyString())).thenReturn(false);
			when(createTodoRequestMapper.toEntity(any(CreateTodoRequest.class))).thenReturn(todoEntity);
			doThrow(RollbackException.class).when(todoRepository).save(any(TodoEntity.class));

			assertThrows(
					RollbackException.class,
					() -> todoService.save(createTodoRequest));
			verify(todoRepository).existsByTitle("coucou");
			verify(createTodoRequestMapper).toEntity(createTodoRequest);
			verify(todoRepository).save(todoEntity);
			verifyNoMoreInteractions(todoMapper);
			verifyNoMoreInteractions(todoRepository);
		}
	}

	@Nested
	@DisplayName("When Update Todo")
	class WhenUpdateTodo {
		@Test
		void should_update_todo() throws NotExistingTodoException, AlreadyExistException {
			var todoEntity = new TodoEntity();
			UUID uuid = UUID.randomUUID();
			todoEntity.setId(uuid);
			var todo = new Todo(UUID.randomUUID(), "title", false, 1);
			var updateTodoRequest = new UpdateTodoRequest("coucou", true, 1);

			when(todoRepository.findById(any(UUID.class))).thenReturn(Optional.of(todoEntity));
			when(updateTodoRequestMapper.toEntity(any(UpdateTodoRequest.class), any(UUID.class))).thenReturn(todoEntity);
			when(todoRepository.existsByTitle(anyString())).thenReturn(false);
			when(todoRepository.saveAndFlush(any(TodoEntity.class))).thenReturn(todoEntity);
			when(todoMapper.toDto(any(TodoEntity.class))).thenReturn(todo);

			assertThat(todoService.update(updateTodoRequest, uuid)).isEqualTo(todo);
			verify(updateTodoRequestMapper).toEntity(updateTodoRequest, uuid);
			verify(todoRepository).existsByTitle("coucou");
			verify(todoRepository).saveAndFlush(todoEntity);
			verify(todoMapper, times(2)).toDto(todoEntity);
			verifyNoMoreInteractions(todoRepository);
			verifyNoMoreInteractions(todoMapper);
		}

		@Test
		void should_reject_a_non_existing_todo() {
			TodoEntity todoEntity = new TodoEntity();
			todoEntity.setId(UUID.randomUUID());
			var updateTodoRequest = new UpdateTodoRequest("coucou", true, 1);

			when(todoRepository.findById(any(UUID.class))).thenReturn(Optional.empty());

			assertThrows(
					NotExistingTodoException.class,
					() -> todoService.update(updateTodoRequest, todoEntity.getId()));
			verify(todoRepository).findById(todoEntity.getId());
			verifyNoMoreInteractions(todoRepository);
			verifyNoInteractions(todoMapper);
		}

		@Test
		void should_reject_updating_todo_when_title_already_exist() throws NotExistingTodoException, AlreadyExistException {
			TodoEntity todoEntity = new TodoEntity();
			UUID uuid = UUID.randomUUID();
			todoEntity.setId(uuid);
			var updateTodoRequest = new UpdateTodoRequest("coucou", true, 1);
			var todo = new Todo(uuid, "title", false, 1);

			when(todoRepository.findById(any(UUID.class))).thenReturn(Optional.of(todoEntity));
			when(todoMapper.toDto(any(TodoEntity.class))).thenReturn(todo);
			when(todoRepository.existsByTitle(anyString())).thenReturn(true);

			assertThrows(
					AlreadyExistException.class,
					() -> todoService.update(updateTodoRequest, uuid));
			verify(todoRepository).findById(uuid);
			verify(todoMapper).toDto(todoEntity);
			verify(todoRepository).existsByTitle("coucou");
			verifyNoMoreInteractions(todoRepository);
			verifyNoMoreInteractions(todoMapper);
		}

		@Test
		void should_throw_when_trying_to_update_todo() {
			final TodoEntity todoEntity = new TodoEntity();
			UUID uuid = UUID.randomUUID();
			todoEntity.setId(uuid);
			todoEntity.setTitle("coucou");
			var todo = new Todo(uuid, "coucou", false, 1);
			var updateTodoRequest = new UpdateTodoRequest("coucou", true, 1);

			when(todoRepository.findById(any(UUID.class))).thenReturn(Optional.of(todoEntity));
			when(todoMapper.toDto(any(TodoEntity.class))).thenReturn(todo);
			when(updateTodoRequestMapper.toEntity(any(UpdateTodoRequest.class), any(UUID.class))).thenReturn(todoEntity);
			doThrow(RollbackException.class).when(todoRepository).saveAndFlush(any(TodoEntity.class));

			assertThrows(
					RollbackException.class,
					() -> todoService.update(updateTodoRequest, uuid));
			verify(updateTodoRequestMapper).toEntity(updateTodoRequest, uuid);
			verify(todoRepository).saveAndFlush(todoEntity);
			verify(todoRepository).findById(uuid);
			verify(updateTodoRequestMapper).toEntity(updateTodoRequest, uuid);
			verifyNoMoreInteractions(todoMapper);
			verifyNoMoreInteractions(todoRepository);
		}
	}

	@Test
	void should_find_all_todos() {
		TodoEntity todoEntity = new TodoEntity();
		UUID uuid = UUID.randomUUID();
		todoEntity.setId(uuid);
		var todo = new Todo(uuid, "title", false, 1);

		when(todoRepository.findAll()).thenReturn(Collections.singletonList(todoEntity));
		when(todoMapper.toDto(any(TodoEntity.class))).thenReturn(todo);

		assertThat(todoService.findAll())
				.hasSize(1)
				.contains(todo);
		verify(todoRepository).findAll();
		verify(todoMapper).toDto(todoEntity);
		verifyNoMoreInteractions(todoRepository);
		verifyNoMoreInteractions(todoMapper);
	}
	
	@Test
	void should_throw_when_trying_to_retrieve_all_todos() {
		doThrow(EntityNotFoundException.class).when(todoRepository).findAll();

		assertThrows(
				EntityNotFoundException.class,
				() -> todoService.findAll());
		verifyNoMoreInteractions(todoRepository);
		verifyNoInteractions(todoMapper);
	}

	@Test
	void should_find_todo_by_id() {
		TodoEntity todoEntity = new TodoEntity();
		UUID uuid = UUID.randomUUID();
		todoEntity.setId(uuid);
		var todo = new Todo(uuid, "title", false, 1);

		when(todoRepository.findById(any(UUID.class))).thenReturn(Optional.of(todoEntity));
		when(todoMapper.toDto(any(TodoEntity.class))).thenReturn(todo);

		assertThat(todoService.findById(uuid)).hasValue(todo);
		verify(todoRepository).findById(uuid);
		verify(todoMapper).toDto(todoEntity);
		verifyNoMoreInteractions(todoRepository);
		verifyNoMoreInteractions(todoMapper);
	}
 
	@Test
	void should_delete_todo_by_id() {
		UUID id = UUID.randomUUID();
		doNothing().when(todoRepository).deleteById(any(UUID.class));

		todoService.deleteById(id);

		verify(todoRepository).deleteById((UUID) captor.capture());
		verifyNoMoreInteractions(todoRepository);	
		verifyNoInteractions(todoMapper);
		assertThat(UUID.fromString(captor.getValue().toString())).isNotNull();
	}

	@Test
	void should_delete_todos() {
		doNothing().when(todoRepository).deleteAll();

		todoService.deleteAll();

		verify(todoRepository).deleteAll();
		verifyNoMoreInteractions(todoRepository);
		verifyNoInteractions(todoMapper);
	}
}
