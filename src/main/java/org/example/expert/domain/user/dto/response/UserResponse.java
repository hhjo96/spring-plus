package org.example.expert.domain.user.dto.response;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
public class UserResponse {

    private final Long id;
    private final String email;

    // noargsconstructor 없어서 레디스 캐시에 문제가 발생하여 내용 수정함
    @JsonCreator
    public UserResponse(@JsonProperty("id") Long id, @JsonProperty("email") String email) {
        this.id = id;
        this.email = email;
    }
}
