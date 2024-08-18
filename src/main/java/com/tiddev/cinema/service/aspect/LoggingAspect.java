package com.tiddev.cinema.service.aspect;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;
import java.util.Arrays;
@Aspect
@Component
@Slf4j
public class LoggingAspect {
    @Pointcut("execution(* com.tiddev.cinema.web.*.*(..))")
    public void controllerMethods(){}
//    @Before("controllerMethods()")
//    public void logBefore(JoinPoint joinPoint) {
//        log.info("Executing method: " + joinPoint.getSignature().getName());
//        log.info("With arguments: " + Arrays.toString(joinPoint.getArgs()));
//    }
//    @After("controllerMethods()")
//    public void logAfter(JoinPoint joinPoint) {
//        log.info("Finished executing method: " + joinPoint.getSignature().getName());
//    }

    @Around("controllerMethods()")
    public Object logAround(ProceedingJoinPoint joinPoint) throws Throwable {
        log.info("Executing method: " + joinPoint.getSignature().getName());
        log.info("With arguments: " + Arrays.toString(joinPoint.getArgs()));
        Object result;
        try {
            result = joinPoint.proceed();
            return result;
        } finally {
            log.info("Finished executing method: " + joinPoint.getSignature().getName());
        }
    }
}
