package com.econovation.whichbook_user.infra.utils;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;


@Profile({"test", "local"})
@Component
public class TestRedisUtilsImpl implements RedisUtils{

    @Override
    public void putData(String key, String value) {

    }

    @Override
    public void putData(String key, String value, long milliseconds) {

    }

    @Override
    public void delete(String key) {

    }
}
