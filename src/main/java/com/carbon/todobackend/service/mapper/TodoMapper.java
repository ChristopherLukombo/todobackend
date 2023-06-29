package com.carbon.todobackend.service.mapper;

import com.carbon.todobackend.domain.dto.Todo;
import com.carbon.todobackend.domain.entities.TodoEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

/**
 * Mapper for the entity TodoEntity and its DTO called Todo.
 *
 * @author christopher
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface TodoMapper {

	@Mapping(source = "id", target = "id")
	@Mapping(source = "title", target = "title")
	@Mapping(source = "completed", target = "completed")
	@Mapping(source = "order", target = "order")
	Todo toDto(TodoEntity todoEntity);
}