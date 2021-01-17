package com.example.provider.service;

import com.example.api.constants.RedisConstant;
import com.example.api.service.DemoService;
import com.example.provider.lock.RedisLock;
import org.apache.dubbo.config.annotation.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.data.redis.core.script.RedisScript;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * @author zwj * @since 1.0
 */
@Service(weight = 2)
public class DemoServiceImpl implements DemoService {

    private Logger logger = LoggerFactory.getLogger(DemoServiceImpl.class);

    @Autowired
    private RedisLock redisLock;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Override
    public String sayHello(String name) {
        UUID uuid = UUID.randomUUID();
        try {
            redisLock.lock(RedisConstant.REDIS_LOCK_FOR_PRODUCT_NUM, uuid.toString());
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
            boolean unlock = redisLock.unlock(RedisConstant.REDIS_LOCK_FOR_PRODUCT_NUM, uuid.toString());
            logger.info(unlock ? "解锁成功" : "解锁失败");
        }
    }
}
