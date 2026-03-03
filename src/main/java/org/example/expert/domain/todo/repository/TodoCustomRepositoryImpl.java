package org.example.expert.domain.todo.repository;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.JPQLQueryFactory;
import lombok.RequiredArgsConstructor;
import org.example.expert.domain.todo.dto.response.TodoSearchResponse;
import org.example.expert.domain.todo.entity.Todo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;

import java.time.LocalDateTime;
import java.util.List;

import static org.example.expert.domain.manager.entity.QManager.manager;
import static org.example.expert.domain.todo.entity.QTodo.todo;
import static org.example.expert.domain.comment.entity.QComment.comment;

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

    private BooleanExpression titleContains(String title) {
        return title != null ? todo.title.contains(title) : null;
    }

    private BooleanExpression nicknameContains(String nickname) {
        return nickname != null ? manager.user.nickname.contains(nickname) : null;
    }

    private BooleanExpression createdDateBetween(LocalDateTime createdStartL, LocalDateTime createdEndL) {
        if(createdStartL != null && createdEndL != null) {
            return todo.createdAt.between(createdStartL, createdEndL);
        } else if (createdStartL != null) {
            return todo.createdAt.goe(createdStartL);
        } else if (createdEndL != null) {
            return todo.createdAt.loe(createdEndL);
        } else {
            return null;
        }
    }

    @Override
    public Page<TodoSearchResponse> findAllByD(
            String title, LocalDateTime createdStartL, LocalDateTime createdEndL, String nickname, Pageable pageable
    ) {
        //query dsl
        List<TodoSearchResponse> responses = queryFactory
                .select(Projections.constructor(
                        TodoSearchResponse.class, todo.id, todo.title, todo.createdAt, manager.id.countDistinct().intValue(), comment.id.countDistinct().intValue()
                ))
                .from(todo)
                .leftJoin(todo.comments, comment)
                .leftJoin(todo.managers, manager)
                .leftJoin(manager.user, user)
                .where(
                        titleContains(title)
                        , createdDateBetween(createdStartL, createdEndL)
                        , nicknameContains(nickname)
                )
                .groupBy(todo.id)
                .orderBy(todo.createdAt.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        //페이징을 위한 total 개수 구하기
        Long todoTotal = queryFactory
                .select(todo.id.countDistinct())
                .from(todo)
                .leftJoin(todo.managers, manager)
                .leftJoin(manager.user)
                .where(
                        titleContains(title)
                        , createdDateBetween(createdStartL, createdEndL)
                        , nicknameContains(nickname)
                )
                .fetchOne();

        return PageableExecutionUtils.getPage(
                responses,
                pageable,
                () -> todoTotal != null ? todoTotal : 0L
        );
    }
}
