package com.carbon.todobackend.ut.rest;

import com.carbon.todobackend.domain.dto.CreateTodoRequest;
import com.carbon.todobackend.domain.dto.Todo;
import com.carbon.todobackend.domain.dto.TodoView;
import com.carbon.todobackend.domain.dto.UpdateTodoRequest;
import com.carbon.todobackend.rest.TodoController;
import com.carbon.todobackend.service.TodoService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TodoController.class)
class TodosControllerTest {

    @MockBean
    private TodoService todoService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void should_respond_200_when_todo_is_successfully_retrieved() throws Exception {
        UUID uuid = UUID.randomUUID();
        var todo = new Todo(uuid, "coucou", false, 1);
        var todoView = new TodoView(uuid, "coucou", false, 1, "http://localhost/api/todos/" + uuid.toString());
        when(todoService.findById(any(UUID.class))).thenReturn(Optional.of(todo));

        mockMvc.perform(
                        get("/api/todos/{id}", uuid.toString())
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(todoView), true));

        verify(todoService).findById(uuid);
        verifyNoMoreInteractions(todoService);
    }

    @Test
    void should_respond_404_when_todo_is_not_found() throws Exception {
        mockMvc.perform(
                        get("/api/todos/{id}", UUID.randomUUID().toString())
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void should_respond_201_when_todo_is_successfully_saved() throws Exception {
        UUID uuid = UUID.randomUUID();
        var todo = new Todo(uuid, "coucou", false, 1);
        var createTodoRequest = new CreateTodoRequest("coucou");
        when(todoService.save(any(CreateTodoRequest.class))).thenReturn(todo);

        mockMvc.perform(
                        post("/api/todos")
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(createTodoRequest)))
                .andExpect(status().isCreated())
                .andExpect(content().json(objectMapper.writeValueAsString(todo), true));

        verify(todoService).save(createTodoRequest);
        verifyNoMoreInteractions(todoService);
    }

    @Test
    void should_respond_204_when_todos_are_successfully_deleted() throws Exception {
        doNothing().when(todoService).deleteAll();

        mockMvc.perform(
                        delete("/api/todos"))
                .andExpect(status().isNoContent());

        verify(todoService).deleteAll();
        verifyNoMoreInteractions(todoService);
    }

    @Test
    void should_respond_204_when_todo_is_successfully_deleted() throws Exception {
        UUID uuid = UUID.randomUUID();
        doNothing().when(todoService).deleteById(any(UUID.class));

        mockMvc.perform(
                        delete("/api/todos/{id}", uuid.toString())
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        verify(todoService).deleteById(uuid);
        verifyNoMoreInteractions(todoService);
    }

    @Test
    void should_respond_200_when_todo_is_successfully_updated() throws Exception {
        var updatedTodo = new UpdateTodoRequest("test", true, 1);
        UUID uuid = UUID.randomUUID();
        var todo = new Todo(uuid, "coucou", false, 1);
        when(todoService.update(any(UpdateTodoRequest.class), any(UUID.class))).thenReturn(todo);

        mockMvc.perform(
                        put("/api/todos/{id}", uuid.toString())
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(updatedTodo)))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(todo), true));

        verify(todoService).update(updatedTodo, uuid);
        verifyNoMoreInteractions(todoService);
    }

    @Test
    void should_respond_200_when_todos_are_successfully_retrieved() throws Exception {
        UUID uuid = UUID.randomUUID();
        var todoView = new TodoView(uuid, "title", false, 1, "http://localhost/api/todos");
        var todo = new Todo(uuid, "title", false, 1);
        when(todoService.findAll()).thenReturn(List.of(todo));

        mockMvc.perform(
                        get("/api/todos")
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(List.of(todoView)), true));

        verify(todoService).findAll();
        verifyNoMoreInteractions(todoService);
    }
}
