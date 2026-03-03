package org.example.expert.domain.todo.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.expert.client.WeatherClient;
import org.example.expert.domain.common.dto.AuthUser;
import org.example.expert.domain.common.exception.InvalidRequestException;
import org.example.expert.domain.todo.dto.request.TodoSaveRequest;
import org.example.expert.domain.todo.dto.request.TodoSearchRequest;
import org.example.expert.domain.todo.dto.response.TodoResponse;
import org.example.expert.domain.todo.dto.response.TodoSaveResponse;
import org.example.expert.domain.todo.dto.response.TodoSearchResponse;
import org.example.expert.domain.todo.entity.Todo;
import org.example.expert.domain.todo.repository.TodoRepository;
import org.example.expert.domain.user.dto.response.UserResponse;
import org.example.expert.domain.user.entity.User;
import org.example.expert.domain.user.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class TodoService {

    private final TodoRepository todoRepository;
    private final WeatherClient weatherClient;
    private final UserRepository userRepository;

    @Transactional
    public TodoSaveResponse saveTodo(AuthUser authUser, TodoSaveRequest todoSaveRequest) {
        User user = userRepository.findById((authUser.getId())).orElseThrow(() -> new InvalidRequestException("유저가 없음"));

        String weather = weatherClient.getTodayWeather();

        Todo newTodo = new Todo(
                todoSaveRequest.getTitle(),
                todoSaveRequest.getContents(),
                weather,
                user
        );
        Todo savedTodo = todoRepository.save(newTodo);

        return new TodoSaveResponse(
                savedTodo.getId(),
                savedTodo.getTitle(),
                savedTodo.getContents(),
                weather,
                new UserResponse(user.getId(), user.getEmail())
        );
    }

    @Transactional(readOnly = true)
    public Page<TodoResponse> getTodos(int page, int size, String weather, String modifiedStart, String modifiedEnd) {
        Pageable pageable = PageRequest.of(page - 1, size);

        //수정일이 범위 검색이므로 시작일과 종료일을 string으로 세팅하고, localdatetime으로 변환하기
        LocalDateTime modifiedStartL = null;
        LocalDateTime modifiedEndL = null;
        if(modifiedStart != null && !modifiedStart.isBlank()) {
            modifiedStartL = LocalDate.parse(modifiedStart).atStartOfDay();
        }
        if (modifiedEnd != null && !modifiedEnd.isBlank()) {
            modifiedEndL = LocalDate.parse(modifiedEnd).atTime(LocalTime.MAX);
        }

        Page<Todo> todos = todoRepository.findAllByWeatherAndModifiedDateOrderByModifiedAtDesc(weather, modifiedStartL, modifiedEndL,pageable);

        return todos.map(todo -> new TodoResponse(
                todo.getId(),
                todo.getTitle(),
                todo.getContents(),
                todo.getWeather(),
                new UserResponse(todo.getUser().getId(), todo.getUser().getEmail()),
                todo.getCreatedAt(),
                todo.getModifiedAt()
        ));
    }

    @Transactional(readOnly = true)
    public TodoResponse getTodo(long todoId) {

        Todo todo = todoRepository.findByIdWithUserDsl(todoId);
        if(todo == null) {
            throw new InvalidRequestException("todo가 없음");
        }
        log.info("query dsl 로 적용하였음");
        User user = todo.getUser();
        return new TodoResponse(
                todo.getId(),
                todo.getTitle(),
                todo.getContents(),
                todo.getWeather(),
                new UserResponse(user.getId(), user.getEmail()),
                todo.getCreatedAt(),
                todo.getModifiedAt()
        );
    }

    public Page<TodoSearchResponse> getTodosWithD(
            int page, int size, TodoSearchRequest request) {
        Pageable pageable = PageRequest.of(page - 1, size);

        //시작일이 범위 검색이므로 시작일과 종료일을 string으로 세팅하고, localdatetime으로 변환하기
        LocalDateTime createdStartL = null;
        LocalDateTime createdEndL = null;
        if(request.getCreatedStart() != null && !request.getCreatedStart().isBlank()) {
            createdStartL = LocalDate.parse(request.getCreatedStart()).atStartOfDay();
        }
        if (request.getCreatedEnd() != null && !request.getCreatedEnd().isBlank()) {
            createdEndL = LocalDate.parse(request.getCreatedEnd()).atTime(LocalTime.MAX);
        }

        Page<TodoSearchResponse> todos = todoRepository.findAllByD(request.getTitle(), createdStartL, createdEndL, request.getNickname(), pageable);

        return todos.map(todo -> new TodoSearchResponse(
                todo.getId(),
                todo.getTitle(),
                todo.getCreatedAt(),
                todo.getManagerSize(),
                todo.getCommentSize()


        ));
    }
}
