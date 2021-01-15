package com.example.provider.service;

import com.example.api.constants.RedisConstant;
import com.example.api.service.DemoService;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;

import java.util.Collections;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * @author zwj * @since 1.0
 */
@Service(weight = 2)
public class DemoServiceImpl implements DemoService {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Override
    public synchronized String sayHello(String name) {
        UUID uuid = UUID.randomUUID();
        try {
            Boolean lock = stringRedisTemplate.opsForValue().setIfAbsent(RedisConstant.REDIS_LOCK_FOR_PRODUCT_NUM, uuid.toString(), 5, TimeUnit.SECONDS);
            if (!lock) {
                sayHello(name);
            }
            String num = stringRedisTemplate.opsForValue().get(RedisConstant.PRODUCT_NUM);
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if (!(Long.valueOf(num) > 0)) {
                return "商品存量不足";
            }
            // do
            Long decrement = stringRedisTemplate.opsForValue().decrement(RedisConstant.PRODUCT_NUM);
            return "购买成功，购买后剩余商品数: " + decrement;
        } catch (Exception e) {
            return "商品购买失败，原因：" + e.getLocalizedMessage();
        } finally {
            String script = "if redis.call('get', KEYS[1]) == ARGV[1] then return redis.call('del', KEYS[1]) else return 0 end";
            DefaultRedisScript<Boolean> redisScript = new DefaultRedisScript();
            redisScript.setResultType(Boolean.class);
            redisScript.setScriptText(script);
            Boolean execute = stringRedisTemplate.execute(redisScript, Collections.singletonList(RedisConstant.REDIS_LOCK_FOR_PRODUCT_NUM),
                    uuid.toString());
        }
    }
}
