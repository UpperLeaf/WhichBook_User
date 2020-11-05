package com.econovation.whichbook_user.infra.utils;

import java.time.Duration;

public interface RedisUtils {
    void putData(String key, String value);
    void putData(String key, String value, long milliseconds);
    void delete(String key);
}
