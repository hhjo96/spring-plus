package org.example.expert.domain.manager.service;

import lombok.RequiredArgsConstructor;
import org.example.expert.domain.common.enums.LogStatus;
import org.example.expert.domain.manager.entity.ManagerRegisterLog;
import org.example.expert.domain.manager.repository.ManagerRegisterLogRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(propagation = Propagation.REQUIRES_NEW)
public class ManagerRegisterLogService {
    private final ManagerRegisterLogRepository logRepository;

    public void saveLog(LogStatus status, String message, Long todoId, Long managerId) {
        ManagerRegisterLog log = new ManagerRegisterLog(status, message, todoId, managerId);
        logRepository.save(log);
    }
}
