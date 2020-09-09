package com.example.demo.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class Test {

    @Around("execution( * com.example.demo.controller.TestController.display(..))")
    public Object Test(ProceedingJoinPoint proceedingJoinPoint){
        Object result = null;
        try {
            System.out.println("before " + proceedingJoinPoint.getSignature().getName());
            result = proceedingJoinPoint.proceed();
            System.out.println("after " + proceedingJoinPoint.getSignature().getName());
        }
        catch (Throwable throwable) {
            System.out.println(throwable);
        }
        return result;
    }
}
