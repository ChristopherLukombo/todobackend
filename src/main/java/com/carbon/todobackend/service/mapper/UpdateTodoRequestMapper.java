package com.carbon.todobackend.service.mapper;

import com.carbon.todobackend.domain.dto.UpdateTodoRequest;
import com.carbon.todobackend.domain.entities.TodoEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import java.util.UUID;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UpdateTodoRequestMapper {

    @Mapping(source = "updateTodoRequest.title", target = "title")
    @Mapping(source = "updateTodoRequest.completed", target = "completed")
    @Mapping(source = "updateTodoRequest.order", target = "order")
    @Mapping(source = "id", target = "id")
    TodoEntity toEntity(UpdateTodoRequest updateTodoRequest, UUID id);
}
