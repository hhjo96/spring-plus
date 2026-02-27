package org.example.expert;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.example.expert.config.PasswordEncoder;
import org.example.expert.domain.todo.entity.Todo;
import org.example.expert.domain.todo.repository.TodoRepository;
import org.example.expert.domain.user.entity.User;
import org.example.expert.domain.user.enums.UserRole;
import org.example.expert.domain.user.repository.UserRepository;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class initData {
    private final TodoRepository todoRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    @PostConstruct
    @Transactional
    public void init() {
        //테스트용 데이터 생성

        User admin = new User("admin@example.com",passwordEncoder.encode("admin123"),  UserRole.ADMIN, "admin");
        User alice = new User("user@example.com", passwordEncoder.encode("1234"), UserRole.ADMIN, "alice");

        userRepository.save(admin);
        userRepository.save(alice);

        Todo todo1 = new Todo("제목2", "내용2", "Sunny", admin);
        Todo todo2 = new Todo("제목3", "내용3", "cool", alice);

        todoRepository.save(todo1);
        todoRepository.save(todo2);


    }
}
