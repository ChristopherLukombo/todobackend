package com.carbon.todobackend.domain.dto;

public record UpdateTodoRequest(String title, Boolean completed, Integer order) {

}
