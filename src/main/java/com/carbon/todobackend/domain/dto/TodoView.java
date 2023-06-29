package com.carbon.todobackend.domain.dto;

import java.util.UUID;

public record TodoView(UUID id, String title, Boolean completed, Integer order, String url) {

}
