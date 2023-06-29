package com.carbon.todobackend.domain.dto;

import java.util.UUID;

public record Todo(UUID id, String title, Boolean completed, Integer order) {

}
