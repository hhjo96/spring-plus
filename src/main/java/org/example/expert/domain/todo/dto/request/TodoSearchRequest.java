package org.example.expert.domain.todo.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class TodoSearchRequest {
    private String title;
    private String createdStart;
    private String createdEnd;
    private String nickname;
}
