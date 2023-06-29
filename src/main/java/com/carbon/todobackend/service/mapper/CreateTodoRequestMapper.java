package com.carbon.todobackend.service.mapper;

import com.carbon.todobackend.domain.dto.CreateTodoRequest;
import com.carbon.todobackend.domain.entities.TodoEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CreateTodoRequestMapper {

    @Mapping(source = "title", target = "title")
    @Mapping(target = "completed", constant = "false")
    TodoEntity toEntity(CreateTodoRequest createTodoRequest);
}
