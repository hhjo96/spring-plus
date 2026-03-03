package org.example.expert.domain.todo.dto.response;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class TodoSearchResponse {
    //todo.id, todo.title, todo.createdAt, todo.managers.size(), comment.countDistinct().intValue()
    private final Long id;
    private final String title;
    private final LocalDateTime createdAt;
    private final int managerSize;
    private final int commentSize;

    public TodoSearchResponse(Long id, String title, LocalDateTime createdAt, int managerSize, int commentSize) {
        this.id = id;
        this.title = title;
        this.createdAt = createdAt;
        this.managerSize = managerSize;
        this.commentSize = commentSize;
    }
}
