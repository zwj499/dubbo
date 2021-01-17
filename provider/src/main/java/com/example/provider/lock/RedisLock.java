package com.example.provider.lock;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.concurrent.TimeUnit;

@Component
public class RedisLock {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    public void lock(String key, String id) {
        Boolean lock = stringRedisTemplate.opsForValue().setIfAbsent(key, id, 30, TimeUnit.SECONDS);
        if (!lock) {
            lock(key, id);
        }
    }

    public boolean unlock(String key, String id) {
        String script = "if redis.call('get', KEYS[1]) == ARGV[1] then return redis.call('del', KEYS[1]) else return 0 end";
        DefaultRedisScript<Boolean> redisScript = new DefaultRedisScript();
        redisScript.setResultType(Boolean.class);
        redisScript.setScriptText(script);
        return stringRedisTemplate.execute(redisScript, Collections.singletonList(key), id);
    }

}
