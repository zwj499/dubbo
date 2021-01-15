package com.example.provider.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;


/**
 * @author zwj * @since 1.0
 */
@Aspect
@Component
public class LogAspect {
    Logger logger = LoggerFactory.getLogger(LogAspect.class);

    @Value("spring.application.name")
    private String applicationName;

    @Around("execution(* com.example.provider.service.*.*(..))")
    public Object xxx(ProceedingJoinPoint pj) {
        try {
            Object proceed = pj.proceed();
            logger.info(proceed.toString());
            return applicationName + proceed;
        } catch (Throwable throwable) {
            throwable.printStackTrace();
            return applicationName;
        }
    }

}
