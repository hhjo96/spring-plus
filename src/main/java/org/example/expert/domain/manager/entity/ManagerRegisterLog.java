package org.example.expert.domain.manager.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.example.expert.domain.common.entity.Timestamped;
import org.example.expert.domain.common.enums.LogStatus;

@Getter
@Entity
@NoArgsConstructor
@Table(name = "log")
public class ManagerRegisterLog extends Timestamped {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private LogStatus status;

    private String message;
    private Long todoId;
    private Long managerId;

    public ManagerRegisterLog(LogStatus status, String message, Long todoId, Long managerId) {
        this.status = status;
        this.message = message;
        this.todoId = todoId;
        this.managerId = managerId;
    }
}
