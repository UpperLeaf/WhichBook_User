package com.econovation.whichbook_user.infra.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;


@Profile({"test", "local"})
@Slf4j
@Component
public class TestRedisUtilsImpl implements RedisUtils{

    @Override
    public void putData(String key, String value) {
        log.info("put Data Key : " + key + " Value : " + value);
    }

    @Override
    public void putData(String key, String value, long milliseconds) {
        log.info("put Data Key : " + key + " Value : " + value);
    }

    @Override
    public void delete(String key) {
        log.info("Delete " + key);
    }
}
