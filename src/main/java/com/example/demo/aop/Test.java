package com.example.demo.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.json.simple.JSONObject;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.lang.annotation.Annotation;
import java.util.*;

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
    @Around("execution( * com.example.demo.controller.TestController.XssTest(..))")
    public Object methodLogger(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        try {

            Object result = proceedingJoinPoint.proceed();
            HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest(); // request 정보를 가져온다.

            String controllerName = proceedingJoinPoint.getSignature().getDeclaringType().getSimpleName();
            String methodName = proceedingJoinPoint.getSignature().getName();

            Map<String, Object> params = new HashMap<>();

            try {
                params.put("controller", controllerName);
                params.put("method", methodName);
                params.put("params", getParams(request));
                params.put("log_time", new Date());
                params.put("request_uri", request.getRequestURI());
                params.put("http_method", request.getMethod());
            } catch (Exception e) {

            }
            Set<String> keys = params.keySet();

            for (String key : keys) {
                System.out.println(key);
                System.out.println(params.get(key));
            }

            return result;

        } catch (Throwable throwable) {
            throw throwable;
        }
    }
    private static JSONObject getParams(HttpServletRequest request) {
        JSONObject jsonObject = new JSONObject();
        Enumeration<String> params = request.getParameterNames();
        while (params.hasMoreElements()) {
            String param = params.nextElement();
            String replaceParam = param.replaceAll("\\.", "-");
            jsonObject.put(replaceParam, request.getParameter(param));
        }
        return jsonObject;
    }
}
