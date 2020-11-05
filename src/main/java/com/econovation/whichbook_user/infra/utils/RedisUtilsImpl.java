package com.econovation.whichbook_user.infra.utils;

import org.springframework.context.annotation.Profile;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;

import java.time.Duration;

@Profile({"prod"})
@Component
public class RedisUtilsImpl implements RedisUtils{

    private final RedisTemplate<String, String> redisTemplate;

    public RedisUtilsImpl(RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Override
    public void putData(String key, String value) {
        ValueOperations<String, String> values = redisTemplate.opsForValue();
        values.set(key, value);
    }

    @Override
    public void putData(String key, String value, long milliseconds) {
        ValueOperations<String, String> values = redisTemplate.opsForValue();
        Duration duration = Duration.ofSeconds(milliseconds);
        values.set(key, value, duration);
    }

    @Override
    public void delete(String key) {
        redisTemplate.delete(key);
    }
}
