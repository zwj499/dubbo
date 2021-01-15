package com.example.consumer.controller;

import com.example.api.constants.RedisConstant;
import com.example.consumer.service.ConsumerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author zwj * @since 1.0
 */
@RestController
@RequestMapping("/demo")
public class DemoController {

    @Autowired
    private ConsumerService consumerService;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @GetMapping("/hello")
    public String demo() {
        return consumerService.getHello("provider");
    }

    @GetMapping("/setProduct")
    public String setProduct() {
        stringRedisTemplate.opsForValue().set(RedisConstant.PRODUCT_NUM, "20");
        return "商品设置成功，商品数量：20";
    }

}
