package com.example.consumer.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Date;


/**
 * @author zwj * @since 1.0
 */
@Aspect
@Component
public class LogAspect {
    Logger logger = LoggerFactory.getLogger(LogAspect.class);

    @Around("execution(* com.example.consumer.controller.*.*(..))")
    public Object xxx(ProceedingJoinPoint pj) {
        try {
            Object proceed = pj.proceed();
            logger.info(proceed.toString());
            return proceed;
        } catch (Throwable throwable) {
            throwable.printStackTrace();
            return null;
        }
    }

}
