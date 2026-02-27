package org.example.expert.domain.todo.repository;

import org.example.expert.domain.todo.entity.Todo;


public interface TodoCustomRepository {
    Todo findByIdWithUserDsl(Long todoId);
}
