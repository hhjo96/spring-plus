package org.example.expert.domain.user.service;


import lombok.RequiredArgsConstructor;
import org.example.expert.domain.user.dto.response.UserResponse;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class UserCacheService {

    private final RedisTemplate<String, Object> redisTemplate;
    private static final String CACHE_USER_PREFIX = "user:nickname:";

    //캐시 조회
    public UserResponse getNicknameCache(String nickname) {
        String key = CACHE_USER_PREFIX + nickname;
        return (UserResponse) redisTemplate.opsForValue().get(key);
    }

    //캐시 저장
    public void saveNicknameCache(String nickname, UserResponse cached) {
        String key = CACHE_USER_PREFIX + nickname;
        redisTemplate.opsForValue().set(key, cached, 20, TimeUnit.MINUTES);

    }
    //닉네임 업데이트 기능이 없어서 캐시 삭제하는 기능은 안만듬
}
