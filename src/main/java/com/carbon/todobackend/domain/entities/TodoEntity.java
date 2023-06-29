package com.carbon.todobackend.domain.entities;

import jakarta.persistence.*;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "todo")
public class TodoEntity implements Serializable {

	@Serial
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	private UUID id;

	@Column(unique = true)
	private String title;
	private Boolean completed;
	private Integer order;

	public TodoEntity() {
		// Empty constructor.
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Boolean isCompleted() {
		return completed;
	}

	public void setCompleted(Boolean completed) {
		this.completed = completed;
	}

	public Integer getOrder() {
		return order;
	}

	public void setOrder(Integer order) {
		this.order = order;
	}

	public UUID getId() {
		return id;
	}

	public void setId(UUID id) {
		this.id = id;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		TodoEntity todo = (TodoEntity) o;
		return Objects.equals(id, todo.id) && Objects.equals(title, todo.title) && Objects.equals(completed, todo.completed) && Objects.equals(order, todo.order);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, title, completed, order);
	}
}