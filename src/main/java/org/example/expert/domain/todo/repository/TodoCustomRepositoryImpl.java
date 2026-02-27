package org.example.expert.domain.todo.repository;

import com.querydsl.jpa.JPQLQueryFactory;
import lombok.RequiredArgsConstructor;
import org.example.expert.domain.todo.entity.Todo;

import java.util.List;
import java.util.Optional;

import static org.example.expert.domain.todo.entity.QTodo.todo;
import static org.example.expert.domain.user.entity.QUser.user;

@RequiredArgsConstructor
public class TodoCustomRepositoryImpl implements TodoCustomRepository{

    private final JPQLQueryFactory queryFactory;

    //    @Query("SELECT t FROM Todo t " +
    //            "LEFT JOIN t.user " +
    //            "WHERE t.id = :todoId")
    @Override
    public Todo findByIdWithUserDsl(Long todoId) {
        return queryFactory
                .selectFrom(todo)
                .join(todo.user, user).fetchJoin()
                .where(todo.id.eq(todoId))
                .fetchOne();

    }
}
