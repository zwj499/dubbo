package com.example.consumer.service;

import com.example.api.service.DemoService;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.stereotype.Service;

/**
 * @author zwj * @since 1.0
 */
@Service
public class ConsumerService {

    @Reference(loadbalance = "roundrobin")
    private DemoService demoService;

    public String getHello(String name) {
        return demoService.sayHello(name);
    }

}
